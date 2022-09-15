package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.farmers

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.data.models.PaymentOptions
import com.ekenya.rnd.cargillbuyer.databinding.FragmentBuyerPayFarmerBinding
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.BuyerCargillViewModel
import com.ekenya.rnd.cargillbuyer.ui.printer.PrinterDataAdapter
import com.ekenya.rnd.common.*
import com.ekenya.rnd.common.auth.AuthResult
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.utils.base.BaseCommonBuyerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class BuyerPayFarmerFragment : BaseCommonBuyerCargillDIFragment<FragmentBuyerPayFarmerBinding>(
    FragmentBuyerPayFarmerBinding::inflate
) {
    private val args: BuyerPayFarmerFragmentArgs by navArgs()
    private lateinit var buyerPaymentOptionSpinnerAdatper: BuyerPaymentOptionSpinnerAdatper
    private var paymentReason: String = ""

    //
    private var jsonObject = JSONObject()
    private var hashMap: HashMap<String, String> = HashMap()

    @Inject
    lateinit var viewModel: BuyerCargillViewModel

    //
    override fun onResume() {
        super.onResume()
        inflateSpinner()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("requestKey") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result: AuthResult = bundle.get("authResult") as AuthResult
            val pin: String = bundle.get("pin") as String

            when (result) {
                AuthResult.AUTH_SUCCESS -> {
                    sendFarmerPaymentRequest()
                }
                AuthResult.AUTH_ERROR -> {
                    lifecycleScope.launch {
                        dismissCustomDialog()
                    }
                }
            }
        }

        inflateUi()
    }

    private fun sendFarmerPaymentRequest() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))

        val farmerObj = args.farmerDetailsData
        val endpoint = ApiEndpointObj.buyerPayFarmer
        jsonObject.put("amount", binding.etAirtimeAmount.text.toString())
        jsonObject.put("reasons", binding.etComments.text.toString())
        jsonObject.put("phonenumber", CURRENT_USER_PHONENUMBER)
        jsonObject.put("coopId", CARGILL_COOPERATIVEID)
        jsonObject.put("coopIndex", CARGILL_COOPERATIVEINDEX)
        jsonObject.put("farmerId", farmerObj.id.toString())
        jsonObject.put("farmerPhonenumber", farmerObj.phoneNumber)
        jsonObject.put("cooperativeid", CARGILL_COOPERATIVEID)
        jsonObject.put("farmForceRef", "TXN454522545224")
        jsonObject.put("buyerid", CARGILL_USER_ID)
        jsonObject.put("paymentType", paymentReason)
        jsonObject.put("endPoint", endpoint)
        jsonObject.put("farmername", "${farmerObj.firstName} ${farmerObj.lastName}")
        saveprintData()

        lifecycleScope.launch {
            try {
                val response = viewModel.requestFarmerPurchase(
                    jsonObject
                )
                if (response.statusCode == 0) {
                    dismissCustomDialog()

                    val createSuccessBundle = createSuccessBundle(
                        title = getString(com.ekenya.rnd.common.R.string.success),
                        subTitle = getString(com.ekenya.rnd.common.R.string.msg_request_was_successful),
                        cardTitle = getString(com.ekenya.rnd.common.R.string.status),
                        cardContent = " ${response.statusDescription}",
                        hashMap = hashMap
                    )
                    findNavController().navigate(
                        R.id.successfulFragment,
                        createSuccessBundle
                    )
                } else {
                    // val message = Gson().fromJson(response.data.toString(), JsonObject::class.java)
                    // Timber.e("******************data==** ${message.get("message").asString}")
                    Timber.e("******************data==** ${response.data}")
                    Timber.e("******************registered==1** ${response.statusDescription}")

                    dismissCustomDialog()
                    requireActivity().showCargillCustomWarningDialog(
                        description = response.statusDescription.toString()
                    )
                }
            } catch (e: ApiExceptions) {
                toast(e.message.toString())
                dismissCustomDialog()
                Log.e("ApiExceptions", e.toString())
            } catch (e: NetworkExceptions) {
                toast(e.message.toString())
                dismissCustomDialog()
                Log.e("NetworkExceptions", e.toString())
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
                toast(e.message.toString())
                dismissCustomDialog()
            }
        }
    }

    private fun inflateUi() {
        val farmerDetailsData = args.farmerDetailsData
        binding.tvProviderName.text =
            "${getString(com.ekenya.rnd.common.R.string.name)}: ${farmerDetailsData.firstName} ${farmerDetailsData.lastName}"

        inflateSpinner()

        setToolbarTitle(
            "${getString(com.ekenya.rnd.common.R.string.pay_farmer_title)}:",
            "${farmerDetailsData.firstName} ${farmerDetailsData.lastName}",
            binding.mainLayoutToolbar,
            requireActivity()
        )
        binding.btnPayFarmer.setOnClickListener { payFarmerReq() }
    }

    private fun payFarmerReq() {
        if (isValidFields()) {
            prepareRequest()
        }
    }

    private fun prepareRequest() {
        hashMap["amount:"] = "${binding.etAirtimeAmount.text} CFA"
        hashMap["comment:"] = binding.etComments.text.toString()

        showConfirmationDialog(
            "${getString(com.ekenya.rnd.common.R.string.confirm)}" +
                " ${getString(com.ekenya.rnd.common.R.string.payment)} ",
            "${getString(com.ekenya.rnd.common.R.string.pay)}, " +
                "${getString(com.ekenya.rnd.common.R.string.farmer)}: " +
                "${getString(com.ekenya.rnd.common.R.string.phone_number)} :" +
                "${args.farmerDetailsData.phoneNumber}",
            hashMap,
            dialogCallback
        )
    }

    private val dialogCallback = object : ConfirmDialogCallBacks {
        override fun confirm() {
            lifecycleScope.launch {
                findNavController().navigate(R.id.commonAuthFragment)
            }
        }

        override fun cancel() {
            Timber.e("cancel")
        }
    }

    private fun inflateSpinner() {
        buyerPaymentOptionSpinnerAdatper =
            BuyerPaymentOptionSpinnerAdatper(requireActivity(), PaymentOptions.getPaymentOptions())
        binding.spinnerProvider.adapter = buyerPaymentOptionSpinnerAdatper
        binding.spinnerProvider.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var paymentOption = buyerPaymentOptionSpinnerAdatper.getItem(position)
                    paymentReason = paymentOption.optionName
                }
            }
    }

    private fun isValidFields(): Boolean {
        val amount = binding.etAirtimeAmount.text.toString()
        val comments = binding.etComments.text.toString()
        val currentBalance = AVAILABLE_BALANCE
        if (!isValidAmount(amount)) {
            binding.tlAmount.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_amount)
            return false
        }
        if (!isValidComments(comments)) {
            binding.tlComments.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_comments)
            return false
        }
        if (amount.isNotEmpty()) {
            if (amount.toInt() > currentBalance.toInt()) {
                requireActivity().showCargillCustomWarningDialog(
                    description = resources.getString(com.ekenya.rnd.common.R.string.inssufficient_funds)
                )
                return false
            }
        }
        return true
    }

    // prepare printing
    private fun saveprintData() {
        var printerArrayList = ArrayList<PrinterDataAdapter.PrinterData>()
        var print1 = PrinterDataAdapter.PrinterData(
            "Name \t\t:\t${jsonObject.getString("farmername")}",
            1,
            1,
            true
        )
        var print2 = PrinterDataAdapter.PrinterData(
            "Phone Number\t:\t${jsonObject.getString("farmerPhonenumber")}",
            1,
            1,
            true
        )
        var print3 = PrinterDataAdapter.PrinterData(
            "Amount \t\t:\t\t${jsonObject.getString("amount")}",
            1,
            1,
            true
        )
        var print4 = PrinterDataAdapter.PrinterData(
            "Payment type \t:\t\t${jsonObject.getString("paymentType")}",
            1,
            1,
            true
        )
        var print5 = PrinterDataAdapter.PrinterData(
            "Reason \t:\t${jsonObject.getString("reasons")}",
            1,
            1,
            true
        )

        printerArrayList.add(print1)
        printerArrayList.add(print2)
        printerArrayList.add(print3)
        printerArrayList.add(print4)
        printerArrayList.add(print5)
        /* var jsonList = NetworkUtility.getJsonParser().toJson(printerArrayList).toString()
         UtillPreference().setPrintData(requireActivity(), jsonList)
         UtillPreference.buyerUsername = "${userData.firstName} ${userData.lastName}"
         UtillPreference.userSection = "${userData.getSection().sectionName}"
         UtillPreference.sectionCode = userData.getSection().sectionName*/
    }
}
