package io.eclectics.cargilldigital.ui.farmforcedeeplink

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.dk.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import io.eclectics.cargilldigital.data.network.ResourceNetwork
import io.eclectics.cargilldigital.databinding.FragmentFarmForceBuyerBinding
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.ui.farmforcedeeplink.viewmodel.FarmForceCargillViewModel
import io.eclectics.cargilldigital.utils.dk.*
import io.eclectics.cargilldigital.utils.sweetAlertDialogWarningType
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FarmForceBuyerFragment : BaseCommonCargillFragment<FragmentFarmForceBuyerBinding>(
    FragmentFarmForceBuyerBinding::inflate
) {

    private lateinit var pdialog: SweetAlertDialog
    private lateinit var PAYMENTKEY: String
    private lateinit var PAYMENTKEYFROMFF: String
    val jsonObject = JSONObject()

    @Inject
    lateinit var viewModel: FarmForceCargillViewModel

    private var onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                errorTransactionCanceled(messageToShow = getString(R.string.cancel_transaction))
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        pdialog.titleText = getString(R.string.msg_sending_request)//"Loading"
        pdialog.setCancelable(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        checkPermissions()
        fetchApiResponse()
        getDataFromFFApplication()
        setUpUi()
        Timber.e("FarmForceBuyerFragment VIEWMODEL.UNIXTIMESTAMP == ${FFUNIXTIMESTAMP}")
    }

    private fun setUpUi() {

        binding.toolbarDescription.setOnClickListener {
            findNavController().navigate(R.id.savedDataForSyncFragment)
        }
        binding.btnHome.setOnClickListener {
            uploadData()
            //testRoomDb()
        }
    }

    private fun saveDataInRoomDataBase() {
        pdialog.dismiss()
        Timber.e("SAVING DAD IN ROOM DATABASE")
        lifecycleScope.launch {
            viewModel.saveUserVmRoom(
                FarmForceData(
                    purchaseId = binding.etPurchaseId.text.toString(),
                    paymentType = 1,
                    amount = binding.etAmount.text.toString(),
                    buyerPhonenumber = binding.etBuyerPhonenumber.text.toString(),
                    farmerPhonenumber = binding.etFarmerPhoneNumber.text.toString(),
                    paymentKey = binding.etPaymentKey.text.toString(),
                    txnDate = binding.etDateTime.text.toString(),
                    comments = binding.etComments.text.toString()
                )
            )
            successTransactionReceived()
        }
    }

    private fun uploadData() {
        if (isValidField()) {
            if (
                comparePassKey(
                    myUnixTimeStamp = FFUNIXTIMESTAMP,
                    passKeyFromFF = PAYMENTKEYFROMFF,
                    binding.etAmount.text.toString()
                )
            ) {
                Timber.e("GOOD")
                if (isConnectionAvailable()) {
                    initiateRequestPaymentTransactions()
                } else {
                    useUssdToSendDataWhenNoInternet()
                }
            } else {
                //Time_elapsed_and_we_couldnt_validate_the_request
                requireActivity().sweetAlertDialogWarningType(
                    getString(R.string.error_session_timeout),
                    action = {
                        val intent = Intent()
                        intent.putExtra(
                            getString(R.string.status_code_message_to_farmforce),
                            getString(R.string.error_session_timeout)
                        )
                        requireActivity().setResult(Activity.RESULT_OK, intent)
                        requireActivity().finish()
                    })
            }
        }
    }

    private fun getDataFromFFApplication() {
        val data: Uri? = requireActivity().intent?.data
        // Figure out what to do based on the intent type
        if (data != null) {
            val parameter: List<String> = data.pathSegments
            val parameter2 = data.query
            val params = parameter[parameter.size - 1]

            // binding.etPurchaseId.setText(params.toString() )
            binding.tvTitle.text = "SEND PAYMENT DATA" // data.query.toString()
            binding.etPurchaseId.setText("${data.getQueryParameter("purchaseId")}")
            binding.etPaymentType.setText("${data.getQueryParameter("paymentType")}")
            binding.etAmount.setText("${data.getQueryParameter("amount")}")
            binding.etBuyerPhonenumber.setText("${data.getQueryParameter("buyerPhonenumber")}")
            binding.etFarmerPhoneNumber.setText("${data.getQueryParameter("farmerPhonenumber")}")
            binding.etPaymentKey.setText("${data.getQueryParameter("paymentKey")}")
            binding.etDateTime.setText("${data.getQueryParameter("DateTime")}")
            binding.etComments.setText("${data.getQueryParameter("comments")}")
            //
            PAYMENTKEYFROMFF = "${data.getQueryParameter("paymentKey")}".replace(" ", "+")
            PAYMENTKEY = "O9SA0rDtq3BvMIodRiwwdgO3YFACwSCQCZhmwk4utKk="
        } else {
            binding.btnHome.isEnabled = false
        }
    }

    private fun fetchApiResponse() {
        /**
         * Logging Observer
         */
        viewModel.getResponsePaymentTransactions.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is ResourceNetwork.Success -> {
                        pdialog.dismiss()
                        if (it.value.status == 0) {
                            FFUNIXTIMESTAMP = 0L
                            successTransactionReceived()
                        } else {
                            //errorNetworkConnectionFailed()
                            successTransactionReceived()
                        }
                    }
                    is ResourceNetwork.Failure -> {
                        Timber.e("ResourceNetwork.Failure 187 $it\n\n")
                        handleApiErrorAndRouteToUssd(
                            failure = it,
                            handleNetworkFailure = {
                                useUssdToSendDataWhenNoInternet()
                            },
                            handleOtherErrors = {
                                saveDataInRoomDataBase()
                            }
                        )
                    }
                    else -> {
                        Timber.e("ELSE SOMETHING ELSE HAS ISSUES $it")
                        saveDataInRoomDataBase()
                    }
                }
            }
        )

        /**
         * OBSERVE OFFLINE MODE USSD RESPONSE MESSAGE
         */

        viewModel.responseMessage.observe(viewLifecycleOwner, Observer {
            pdialog.dismiss()
            Timber.e("OFFLINE MODE $it")
            // sendDataUsingUssdOfflineMode(it)
            if (it != null) {
                try {
                    if (it.startsWith("{")) {
                        val response = Gson().fromJson(it, JsonObject::class.java)
                        Timber.e("OFFLINE MODE USSD RESPONSE 2 $it\n\n")
                        Timber.e("response ${response.get("response")}")
                        if (response.get("message").asString == getString(R.string.success_transaction_recieved)) {
                            FFUNIXTIMESTAMP = 0L
                            successTransactionReceived(response.get("message").asString)
                        } else if (response.get("responseCode").asInt.toString() == "0".trim()) {
                            FFUNIXTIMESTAMP = 0L
                            successTransactionReceived()
                        } else if (response.get("responseCode").asInt.toString() == "1".trim()) {
                            errorBuyerAccountDoesntExist(messageToShow = response.get("response").asString)
                        } else {
                            saveDataInRoomDataBase()
                        }
                    } else if (it == "-1") {
                        saveDataInRoomDataBase()
                    } else {
                        saveDataInRoomDataBase()
                    }
                } catch (e: JSONException) {
                    Timber.e("JSONException ${e.message}")
                }
            } else {
                Timber.e("RESPONSE IS EMPTY")
            }

        })
    }

    private fun useUssdToSendDataWhenNoInternet() {
        viewModel.offlineUssdModeBackGround(
            ApiEndpointObj.FARMFORCEFARMERDATA.toString() +
                    trimPhoneNumber(binding.etBuyerPhonenumber.text.toString()) +
                    trimPhoneNumber(binding.etFarmerPhoneNumber.text.toString()) +
                    binding.etPurchaseId.text.toString() +
                    addLeadingZeroesToNumber(
                        binding.etAmount.text.toString()
                    )
        )
    }

    private fun sendDataUsingUssdOfflineMode(it: String?) {
        Timber.e("OFFLINE MODE USSD RESPONSE 1 $it\n\n")
        if (it != null) {
            try {
                if (it.startsWith("{")) {
                    val response = Gson().fromJson(it, JsonObject::class.java)
                    Timber.e("OFFLINE MODE USSD RESPONSE 2 $it\n\n")
                    Timber.e("response ${response.get("response")}")
                    //Checking where message is String
                    if (response.get("message").asString == getString(R.string.success_transaction_recieved)) {
                        FFUNIXTIMESTAMP = 0L
                        successTransactionReceived(response.get("message").asString)
                    } else if (response.get("message").asString == getString(R.string.error_buyer_account_disabled)) {
                        errorCustomerFailure(response.get("message").asString)
                    } else if (response.get("message").asString == getString(R.string.error_farmer_account_disabled)) {
                        errorCustomerFailure(response.get("message").asString)
                    } else if (response.get("message").asString == getString(R.string.error_buyer_account_doesnt_exist)) {
                        errorCustomerFailure(response.get("message").asString)
                    } else if (response.get("message").asString == getString(R.string.error_farmer_account_doesnt_exist)) {
                        errorCustomerFailure(response.get("message").asString)
                    } else if (response.get("message").asString == getString(R.string.error_transcation_canceled)) {
                        errorCustomerFailure(response.get("message").asString)
                    } else if (response.get("message").asString == getString(R.string.error_duplicate_purchaseid)) {
                        errorCustomerFailure(response.get("message").asString)
                    }
                    //Checking where responseCode exists
                    else if (response.get("responseCode").asInt.toString() == "0") {
                        FFUNIXTIMESTAMP = 0L
                        successTransactionReceived()
                    } else if (response.get("responseCode").asInt.toString() == " 1") {
                        errorBuyerAccountDoesntExist(messageToShow = response.get("response").asString)
                    }
                    //
                    else {
                        // errorNetworkConnectionFailed()
                        saveDataInRoomDataBase()
                    }
                } else if (it == "-1") {
                    //errorNetworkConnectionFailed()
                    saveDataInRoomDataBase()
                } else {
                    //errorNetworkConnectionFailed()
                    saveDataInRoomDataBase()
                }
            } catch (e: JSONException) {
                Timber.e("JSONException ${e.message}")
            }
        } else {
            Timber.e("RESPONSE IS EMPTY")
        }
    }


    private fun initiateRequestPaymentTransactions() {
        jsonObject.put("purchaseId", binding.etPurchaseId.text.toString())
        jsonObject.put("paymentType", 1)
        jsonObject.put("amount", binding.etAmount.text.toString())
        jsonObject.put("buyerPhonenumber", binding.etBuyerPhonenumber.text.toString())
        jsonObject.put("farmerPhonenumber", binding.etFarmerPhoneNumber.text.toString())
        jsonObject.put("paymentKey", PAYMENTKEY)
        jsonObject.put("txnDate", "0001-01-01T00:00:00")
        jsonObject.put("comments", binding.etComments.text.toString())

        viewModel.requestPaymentTransactions(
            jsonObject
        )
        pdialog.show()
    }

    private fun isValidField(): Boolean {
        val purchaseId = binding.etPurchaseId.text.toString().trim()
        val buyerPhoneNumber = binding.etBuyerPhonenumber.text.toString().trim()
        val farmerPhoneNumber = binding.etFarmerPhoneNumber.text.toString().trim()

        if (buyerPhoneNumber.isEmpty()) {
            binding.etBuyerPhonenumber.error = getString(R.string.input_required)
            return false
        } else if (!buyerPhoneNumber.startsWith("225")) {
            errorTransactionCanceled(
                messageToShow = "${
                    getString(R.string.invalid_phonenumber_cancel_transaction_and_try_again)
                }"
            )
            return false
        } else if (purchaseId.isEmpty()) {
            binding.etBuyerPhonenumber.error = getString(R.string.input_required)
            return false
        } else if (purchaseId.length != 16) {
            errorTransactionCanceled(
                messageToShow = "${
                    getString(R.string.invalid_purchase_id)
                }"
            )
            return false
        } else if (farmerPhoneNumber.isEmpty()) {
            binding.etFarmerPhoneNumber.error = getString(R.string.input_required)
            return false
        } else if (!farmerPhoneNumber.startsWith("225")) {
            errorTransactionCanceled(
                messageToShow = "${
                    getString(R.string.invalid_phonenumber_cancel_transaction_and_try_again)
                }"
            )
            return false
        } else {
            return true
        }
    }

}
