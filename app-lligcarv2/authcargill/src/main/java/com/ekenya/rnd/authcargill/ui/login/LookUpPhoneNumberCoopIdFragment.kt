package com.ekenya.rnd.authcargill.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.data.network.AuthApiClientService
import com.ekenya.rnd.authcargill.databinding.FragmentLookupPhoneAccountIdBinding
import com.ekenya.rnd.authcargill.ui.AuthCargillViewModel
import com.ekenya.rnd.authcargill.utils.handleApiError
import com.ekenya.rnd.common.MEDIA_TYPE_JSON
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.network.ResourceNetwork
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonAuthCargillDIFragment
import com.ekenya.rnd.common.utils.custom.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class LookUpPhoneNumberCoopIdFragment :
    BaseCommonAuthCargillDIFragment<FragmentLookupPhoneAccountIdBinding>
    (FragmentLookupPhoneAccountIdBinding::inflate) {

    @Inject
    lateinit var authViewModel: AuthCargillViewModel

    @Inject
    lateinit var authApiClientService: AuthApiClientService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        UtilPreferenceCommon().setLoggedPhoneNumber(
//            requireActivity(),
//            "2250701686379"//response.accountIdData!!.phonenumber.toString()
//        )
        coopAccountIdCheckRequest2()
        binding.etPhoneNumber.setText("701686379")
        binding.edtCoopAccountID.setText("121212345")
        binding.btnContinue.setOnClickListener {
            // sendAPI()
            // sendAccountIdLookUpCheckRequest()
            testAndAvoidLookUp()
        }
    }

    private fun testAndAvoidLookUp() {
        if (validateRequiredField()) {
            UtilPreferenceCommon().setLoggedPhoneNumber(
                requireActivity(),
                binding.etPhoneNumber.text.toString() // response.accountIdData!!.phonenumber.toString()
            )
            findNavController().navigate(R.id.loginPinFragment)
        }
    }

    private fun sendAPI() {
        lifecycleScope.launch {
            try {
                val coopAccountId = binding.edtCoopAccountID.text.toString().trim()
                val phoneNumber = binding.etPhoneNumber.text.toString().trim()
                val jsonObject = JSONObject()
                jsonObject.put("phonenumber", phoneNumber)
                jsonObject.put("accountid", coopAccountId)

                val res = authApiClientService.cooparativeIdLookUp3(
                    jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
                )
                val error = res.string()
                Timber.d("BASE REPOSITORY----->>>>>>>>>>>>>>>>>> $error")
                try {
                    val data = JSONObject(error).getString("data")
                    val message = JSONObject(data).getString("message")
                    toasty("RES $message")
                } catch (e: JSONException) {
                    toast(e.localizedMessage)
                }
            } catch (e: Exception) {
                Timber.e("TABDGD ${e.message}")
            }
        }
    }

    private fun sendAccountIdLookUpCheckRequest() {
        if (validateRequiredField()) {
            showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
            initiatecoopAccountId()
        }
    }

    private fun initiatecoopAccountId() {
        val coopAccountId = binding.edtCoopAccountID.text.toString().trim()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()

        val jsonObject = JSONObject()
        jsonObject.put("phonenumber", phoneNumber)
        jsonObject.put("accountid", coopAccountId)

        authViewModel.sendCooparativeIdLookUp(
            jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        )
    }

    private fun coopAccountIdCheckRequest2() {
        /**
         * Logging Observer
         */
        authViewModel.responseCooparativeIdLookUp.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is ResourceNetwork.Success -> {
                        val response = it.value
                        if (response.statusCode == 0) {
                            dismissCustomDialog()
                            UtilPreferenceCommon().setLoggedPhoneNumber(
                                requireActivity(),
                                binding.etPhoneNumber.text.toString().trim()
                            )
                            findNavController().navigate(R.id.action_lookUpPhoneNumberCoopIdFragment_to_otpVerificationFragment)
                        } else {
                            Timber.e("RESPONSE 2 ==============\n\n ${response.data}")
                            toast(response.statusDescription!!.toString())
                            requireActivity().showCargillCustomWarningDialog(
                                title = response.statusDescription.toString(),
                                description = response.statusDescription!!.toString(),
                                btnConfirmText = getString(com.ekenya.rnd.common.R.string.retry_request),
                                action = {}
                            )
                            dismissCustomDialog()
                        }
                    }
                    is ResourceNetwork.Failure -> {
                        dismissCustomDialog()
                        handleApiError(it)
                        Timber.d(" handleApiError  $it")
                    }
                    else -> {}
                }
            }
        )
    }

    private fun coopAccountIdCheckRequest() {
        lifecycleScope.launch {
            try {
                val coopAccountId = binding.edtCoopAccountID.text.toString().trim()
                val phoneNumber = binding.etPhoneNumber.text.toString().trim()

                val jsonObject = JSONObject()
                jsonObject.put("phonenumber", phoneNumber)
                jsonObject.put("accountid", coopAccountId)
                val response = authViewModel.cooparativeIdLookUp(jsonObject)
                Timber.e("RESPONSE 1 ==============\n\n $response")
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    UtilPreferenceCommon().setLoggedPhoneNumber(
                        requireActivity(),
                        phoneNumber // response.accountIdData!!.phonenumber.toString()
                    )
                    findNavController().navigate(R.id.action_lookUpPhoneNumberCoopIdFragment_to_otpVerificationFragment)
                } else {
                    Timber.e("RESPONSE 2 ==============\n\n $response")
                    toast(response.statusDescription!!.toString())
                    requireActivity().showCargillCustomWarningDialog(
                        title = response.statusDescription.toString(),
                        description = response.statusDescription!!.toString(),
                        btnConfirmText = getString(com.ekenya.rnd.common.R.string.retry_request),
                        action = {}
                    )
                    dismissCustomDialog()
                }
            } catch (e: ApiExceptions) {
                snackBarCustom(msg = e.toString()) { }
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                snackBarCustom(msg = e.toString()) { }
                dismissCustomDialog()
            }
        }
    }

    private fun validateRequiredField(): Boolean {
        if (!isNumeric(binding.etPhoneNumber.text.toString())) {
            binding.etPhoneNumber.error =
                getString(com.ekenya.rnd.common.R.string.invalid_credentials)
            return false
        } else if (!binding.termsCheckBox.isChecked) {
            Toast.makeText(
                context,
                getString(com.ekenya.rnd.common.R.string.accept_privacy_policy),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (!isNumeric(binding.edtCoopAccountID.text.toString())) {
            binding.edtCoopAccountID.error =
                getString(com.ekenya.rnd.common.R.string.invalid_credentials)
            return false
        } else {
            return true
        }
    }
}
