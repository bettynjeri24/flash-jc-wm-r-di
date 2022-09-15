package io.eclectics.cargilldigital.ui.auth.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.utils.dk.handleApiError
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.network.ResourceNetwork
import io.eclectics.cargilldigital.databinding.FragmentLookupPhoneAccountIdBinding
import io.eclectics.cargilldigital.ui.farmforcedeeplink.viewmodel.MEDIA_TYPE_JSON
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.dk.BaseCommonCargillFragment
import io.eclectics.cargilldigital.utils.dk.showCargillCustomWarningDialog
import io.eclectics.cargilldigital.utils.dk.toast
import io.eclectics.cargilldigital.utils.isNumeric
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LookUpPhoneNumberCoopIdFragment :
    BaseCommonCargillFragment<FragmentLookupPhoneAccountIdBinding>
        (FragmentLookupPhoneAccountIdBinding::inflate) {

    @Inject
    lateinit var authViewModel: AuthViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        UtilPreference().setLoggedPhoneNumber(
//            requireActivity(),
//            "2250701686379"//response.accountIdData!!.phonenumber.toString()
//        )
        coopAccountIdCheckRequest2()
        binding.etPhoneNumber.setText("701686379")
        binding.edtCoopAccountID.setText("121212345")
        binding.btnContinue.setOnClickListener {
            // sendAccountIdLookUpCheckRequest()
            testAndAvoidLookUp()
        }
    }

    private fun testAndAvoidLookUp() {
        if (validateRequiredField()) {
            UtilPreference().setLoggedPhoneNumber(
                requireActivity(),
                binding.etPhoneNumber.text.toString() // response.accountIdData!!.phonenumber.toString()
            )
            findNavController().navigate(R.id.loginPinFragment)
        }
    }

    private fun sendAccountIdLookUpCheckRequest() {
        if (validateRequiredField()) {
            showCustomDialog(getString(R.string.sending_request_cargill))
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
                            //findNavController().navigate(R.id.action_lookUpPhoneNumberCoopIdFragment_to_otpVerificationFragment)
                        } else {
                            dismissCustomDialog()
                            Timber.e("RESPONSE 2 ==============\n\n ${response.data}")
                            toast(response.statusDescription!!.toString())
                            requireActivity().showCargillCustomWarningDialog(
                                title = response.statusDescription.toString(),
                                description = response.statusDescription!!.toString(),
                                btnConfirmText = getString(R.string.retry_request),
                                positiveButtonFunction = {}
                            )

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


    private fun validateRequiredField(): Boolean {
        if (!isNumeric(binding.etPhoneNumber.text.toString())) {
            binding.etPhoneNumber.error =
                getString(R.string.invalid_credentials)
            return false
        } else if (!binding.termsCheckBox.isChecked) {
            Toast.makeText(
                context,
                getString(R.string.accept_privacy_policy),
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (!isNumeric(binding.edtCoopAccountID.text.toString())) {
            binding.edtCoopAccountID.error =
                getString(R.string.invalid_credentials)
            return false
        } else {
            return true
        }
    }
}
