package com.ekenya.rnd.authcargill.ui.lookup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.databinding.FragmentUserPhoneLookupBinding
import com.ekenya.rnd.authcargill.ui.AuthCargillViewModel
 
import com.ekenya.rnd.authcargill.utils.handleApiError
import com.ekenya.rnd.common.data.network.ResourceNetwork
import com.ekenya.rnd.common.AUTH_TOKEN
import com.ekenya.rnd.common.utils.base.BaseCommonAuthCargillDIFragment
import com.ekenya.rnd.common.utils.custom.*
import com.ekenya.rnd.common.utils.custom.Coroutines.observeOnce
import org.json.JSONObject
import javax.inject.Inject

class UserPhoneLookupFragment : BaseCommonAuthCargillDIFragment<FragmentUserPhoneLookupBinding>
    (FragmentUserPhoneLookupBinding::inflate) {

  /*  @Inject
    lateinit var authCargillViewModel: AuthCargillViewModel*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AUTH_TOKEN = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sendLookupRequest()

        binding.ccp.setCcpClickable(false)

        binding.ccp.setCountryForPhoneCode(225)
        binding.btnContinue.setOnClickListener {
            sendPhoneLookUpRequest()
        }

    }

    private fun sendPhoneLookUpRequest() {
        if (validateRequiredField()) {
            val phone = "225${binding.phoneNumberEditText.text.toString()}"
            val jsonObject = JSONObject()
            jsonObject.put("phonenumber", phone)
            //authCargillViewModel.mobileNumberLookUp(jsonObject = jsonObject)
            showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        }
    }

/*

    private fun sendLookupRequest() {
        authCargillViewModel.mobileNumberLookUp.observeOnce(viewLifecycleOwner, Observer {
            when (it) {
                is ResourceNetwork.Success -> {
                    when (it.value.statusCode) {
                        1 -> {
                            dismissCustomDialog()
                            toasty("${it.value.toString()}")
                            */
/*  requireActivity().showCargillInternalCustomDialog(
                                  title = title.toString(),
                                  description = description,
                                  positiveButtonFunction = {
                                      sendPhoneLookUpRequest()
                                  },
                                  negativeButtonFunction = {}
                              )*//*

                        }
                        0 -> {
                            //First Time User
                            UtilPreferenceCommon().setLoggedPhoneNumber(
                                requireActivity(),
                                "225${binding.phoneNumberEditText.text.toString()}"
                            )
                            val bundle = Bundle()
                            bundle.putString(
                                getString(R.string.phone_number),
                                "225${binding.phoneNumberEditText.text.toString()}"
                            )
                            findNavController().navigate(R.id.companyIDLookupFragment, bundle)
                            dismissCustomDialog()
                        }
                        else -> {
                            dismissCustomDialog()
                            //snackBarCustom("Please contact the Support team to Register you")
                            snackBarCustom(it.value.statusDescription)
                            timber("******************registered==1** ${it.value.statusCode}")
                        }
                    }
                }

                is ResourceNetwork.Failure -> {
                    dismissCustomDialog()
                    snackBarCustom("${it.errorBody}")
                    handleApiError(it)
                }
                else -> {}
            }
        })

    }

*/

    private fun validateRequiredField(): Boolean {
        if (!binding.phoneNumberEditText.text.toString().isPhoneNumber()) {
            // binding.btnContinue.enable(false)
            binding.phoneNumberEditText.error = getString(com.ekenya.rnd.common.R.string.invalid_credentials)
            return false
        } else if (!binding.termsCheckBox.isChecked) {
            Toast.makeText(
                context,
                "Please Read and Accept both terms and conditions to proceed",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else {
            if (binding.termsCheckBox.isChecked) binding.btnContinue.enable(true)
            //  binding.btnContinue.enable(true)
            return true
        }
    }
}