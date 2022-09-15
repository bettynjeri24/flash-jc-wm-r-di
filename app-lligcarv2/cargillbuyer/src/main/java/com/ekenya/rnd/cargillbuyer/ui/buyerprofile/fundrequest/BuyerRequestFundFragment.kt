package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.fundrequest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.databinding.FragmentRequestFundBuyerBinding
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.BuyerCargillViewModel
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

class BuyerRequestFundFragment : BaseCommonBuyerCargillDIFragment<FragmentRequestFundBuyerBinding>(
    FragmentRequestFundBuyerBinding::inflate
) {
    private val jsonObject = JSONObject()
    private var hashMap: HashMap<String, String> = HashMap()

    @Inject
    lateinit var viewModel: BuyerCargillViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.buyer_fund_requests),
            resources.getString(com.ekenya.rnd.common.R.string.fund_request_ttle),
            binding.mainLayoutToolbar,
            requireActivity()
        )

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

    private fun inflateUi() {
        binding.btnRequestFunds.setOnClickListener {
            if (isValidData()) {
                prepareRequest()
            }
        }
    }

    private fun prepareRequest() {
        hashMap["amount:"] = "${binding.etRequestAmount.text} CFA"
        hashMap["comment:"] = binding.etComments.text.toString()

        showConfirmationDialog(
            "${getString(com.ekenya.rnd.common.R.string.confirm)} " +
                "${getString(com.ekenya.rnd.common.R.string.request_float)} ",
            "${getString(com.ekenya.rnd.common.R.string.fund_request)}: " +
                "${getString(com.ekenya.rnd.common.R.string.phone_number)} :" +
                "$CURRENT_USER_PHONENUMBER",
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

    private fun sendFarmerPaymentRequest() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        /*
        *  "amount": null,
  "buyerId": null,
  "farmerPhonenumber": null,
  "paymentType": null,
  "reasons": null,
  "farmForceRef": null*/

        val endpoint = ApiEndpointObj.agentRequestFund

        // jsonObject.put("phonenumber",requireArguments().getString("phone"))
        jsonObject.put("amountRequested", binding.etRequestAmount.text.toString())
        jsonObject.put("reasons", binding.etComments.text.toString())
        jsonObject.put("phonenumber", CURRENT_USER_PHONENUMBER)
        jsonObject.put("balanceOnRequest", AVAILABLE_BALANCE)
        jsonObject.put("cooperativeid", CARGILL_COOPERATIVEID)
        jsonObject.put("buyerIndex", CARGILL_USERINDEX)
        jsonObject.put("coopIndex", CARGILL_COOPERATIVEINDEX)
        jsonObject.put("buyerid", CARGILL_USER_ID)
        jsonObject.put("endPoint", endpoint)

        lifecycleScope.launch {
            try {
                val response = viewModel.requestFundsTopUp(
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

    private fun isValidData(): Boolean {
        val comments = binding.etComments.text.toString()
        if (binding.etRequestAmount.text.toString().isNullOrEmpty()) {
            binding.tlAmount.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_amount)
            return false
        }
        if (!isValidComments(comments)) {
            binding.tlComments.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_comments)
            return false
        }
        return true
    }
}
