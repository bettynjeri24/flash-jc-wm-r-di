package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.ffpendingpayments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ekenya.rnd.cargillbuyer.CargillBuyerMainActivity
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.data.responses.FfPendingPaymentsData
import com.ekenya.rnd.cargillbuyer.databinding.FragmentPendingPaymentDetailsBinding
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

class FfPendingPaymentDetailsFragment :
    BaseCommonBuyerCargillDIFragment<FragmentPendingPaymentDetailsBinding>(
        FragmentPendingPaymentDetailsBinding::inflate
    ) {
    private val lookupJson = JSONObject()
    private var hashMap: HashMap<String, String> = HashMap()
    private lateinit var ffPendingPaymentsData: FfPendingPaymentsData
    private val args: FfPendingPaymentDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: BuyerCargillViewModel
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

        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.pay_farmer),
            resources.getString(com.ekenya.rnd.common.R.string.pay_or_scan),
            binding.mainLayoutToolbar,
            requireActivity()
        )

        inflateUiData()

        binding.btnPayFarmer.setOnClickListener { payFarmerReq() }
    }

    private fun sendFarmerPaymentRequest() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        /*
        *  "amount": null,
  "buyerId": null,
  "farmerPhonenumber": null,
  "paymentType": null,
  "reasons": null,
  "farmForceRef": null*/
        val jsonObject = JSONObject()
        jsonObject.put("amount", binding.etPayAmount.text.toString())
        jsonObject.put("buyerId", CARGILL_USER_ID.toString())
        jsonObject.put("farmerPhonenumber", ffPendingPaymentsData.farmerPhonenumber.toString())
        jsonObject.put("paymentType", ffPendingPaymentsData.paymentDescription.toString())
        jsonObject.put("reasons", binding.etComments.text.toString())
        jsonObject.put("farmForceRef", ffPendingPaymentsData.farmForceRef.toString())
        //
        jsonObject.put("endPoint", ApiEndpointObj.payPendingffPayment)
        jsonObject.put("buyerPhonenumber", CURRENT_USER_PHONENUMBER.toString())
        jsonObject.put("userId", CARGILL_PROVIDEDUSERID.toString())
        jsonObject.put("cooperativeid", CARGILL_COOPERATIVEID.toString())
        jsonObject.put("printable", true)
        jsonObject.put("pin", SECRET_KEY)
        jsonObject.put("deviceUUId", deviceSessionUUID())
        jsonObject.put("deviceId", requireActivity().getDeviceId())
        jsonObject.put("language", "en")
        jsonObject.put("farmername", CARGILL_FULL_NAME)

        lifecycleScope.launch {
            try {
                val response = viewModel.requestffPayments(
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

    private fun inflateUiData() {
        ffPendingPaymentsData = args.ffPendingPaymentsData
        // requireActivity().intent.getParcelableExtra("FfPendingPaymentsData")!!

        binding.tvCustName.text =
            "${ffPendingPaymentsData.firstName}${ffPendingPaymentsData.lastName}"
        binding.tvOutType.text =
            "${getString(com.ekenya.rnd.common.R.string.type)}:${ffPendingPaymentsData.paymentDescription}"
        binding.tvTotalAmount.text =
            "${getString(com.ekenya.rnd.common.R.string.total_amount)}:${ffPendingPaymentsData.fullAmount}"
        binding.tvPaidAmount.text =
            "${getString(com.ekenya.rnd.common.R.string.paid_amount)}:${ffPendingPaymentsData.amountPaid}"
        binding.tvBalance.text =
            "${getString(com.ekenya.rnd.common.R.string.balance_amount)}:${ffPendingPaymentsData.fullAmount!!.toInt() - ffPendingPaymentsData.amountPaid!!.toInt()}"
        binding.tvReferenceNumber.text = "Ref No: ${ffPendingPaymentsData.farmForceRef}"
        binding.tvDate.text = "Date: ${ffPendingPaymentsData.date}"
    }

    private fun prepareRequest() {
        hashMap["amount:"] = "${binding.etPayAmount.text} CFA"
        hashMap["comment:"] = binding.etComments.text.toString()

        showConfirmationDialog(
            "${getString(com.ekenya.rnd.common.R.string.confirm)}" +
                " ${getString(com.ekenya.rnd.common.R.string.payment)} ",
            "${getString(com.ekenya.rnd.common.R.string.pay)}, " +
                "${getString(com.ekenya.rnd.common.R.string.farmer)}: " +
                "${getString(com.ekenya.rnd.common.R.string.phone_number)} :" +
                "${ffPendingPaymentsData.farmerPhonenumber}",
            hashMap,
            dialogCallback
        )
    }

    private fun payFarmerReq() {
        if (validFields()) {
            prepareRequest()
        }
    }

    private fun validFields(): Boolean {
        if (binding.etPayAmount.text.toString().isNullOrEmpty()) {
            binding.etPayAmount.error = getString(com.ekenya.rnd.common.R.string.enter_amount)
            return false
        }
        if (binding.etComments.text.toString().isNullOrEmpty()) {
            binding.etComments.error = getString(com.ekenya.rnd.common.R.string.enter_comments)
            return false
        } else {
            return true
        }
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

    // BYPASS PIN TO TEST PRINTING
    private fun printtest() {
        var intent = Intent(requireActivity(), CargillBuyerMainActivity::class.java)
        intent.putExtra("printerdata", "rttrer")
        intent.putExtra("isSettings", false)
        requireActivity().startActivity(intent)
    }

    private fun saveprintData(requestjson: JSONObject) {
        val printerArrayList = ArrayList<PrinterDataAdapter.PrinterData>()
        val print1 = PrinterDataAdapter.PrinterData(
            "Name \t\t:\t${requestjson.getString("farmername")}",
            1,
            1,
            true
        )
        val print2 = PrinterDataAdapter.PrinterData(
            "Phone Number\t:\t${lookupJson.getString("farmerPhonenumber")}",
            1,
            1,
            true
        )
        val print3 = PrinterDataAdapter.PrinterData(
            "Amount \t\t:\t\t${lookupJson.getString("amount")}",
            1,
            1,
            true
        )
        val print4 = PrinterDataAdapter.PrinterData(
            "Payment type \t:\t\t${lookupJson.getString("paymentType")}",
            1,
            1,
            true
        )
        val print5 = PrinterDataAdapter.PrinterData(
            "Reason \t:\t${requestjson.getString("reasons")}",
            1,
            1,
            true
        )

        printerArrayList.add(print1)
        printerArrayList.add(print2)
        printerArrayList.add(print3)
        printerArrayList.add(print4)
        printerArrayList.add(print5)
    }
}
