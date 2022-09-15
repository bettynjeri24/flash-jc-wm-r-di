package com.ekenya.rnd.authcargill.ui.lookup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.databinding.FragmentCompanyIdLookupBinding
import com.ekenya.rnd.authcargill.ui.AuthCargillViewModel
 
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonAuthCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon.Companion.phonenumber
import com.ekenya.rnd.common.utils.custom.isNumeric
import com.ekenya.rnd.common.utils.custom.greetingsDependingOnTimeOfDay
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class CompanyIDLookupFragment : BaseCommonAuthCargillDIFragment<FragmentCompanyIdLookupBinding>
    (FragmentCompanyIdLookupBinding::inflate) {

    private lateinit var phoneNumber: String

  /*  @Inject
    lateinit var authCargillViewModel: AuthCargillViewModel*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvGreeting.text = requireContext().greetingsDependingOnTimeOfDay()

        phoneNumber = requireArguments().getString(getString(com.ekenya.rnd.common.R.string.phone_number)).toString()
        binding.btnContinue.setOnClickListener {
            sendAccountIdCheckRequest()
        }
    }


    private fun sendAccountIdCheckRequest() {
        if (validateRequiredField()) {
            showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
            //coopAccountIdCheckRequest()
        }
    }

/*
    private fun coopAccountIdCheckRequest() {
        lifecycleScope.launch {
            try {
                val coopAccountId = binding.edtCoopAccountCheck.text.toString().trim()
                val jsonObject = JSONObject()
                jsonObject.put("phonenumber", phoneNumber)
                jsonObject.put("accountid", coopAccountId)
                val response = authCargillViewModel.cooparativeIdLookUp(jsonObject)
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    findNavController().navigate(R.id.otpVerificationFragment)
                } else {
                    toast(response.toString())
                    dismissCustomDialog()
                    dismissCustomDialog()
                }

            } catch (e: ApiExceptions) {
                toast(e.toString())
                dismissCustomDialog()
                Log.e("Exception", phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                toast(e.toString())
                dismissCustomDialog()
            }


        }


    }
*/


    private fun validateRequiredField(): Boolean {
        if (!isNumeric(binding.edtCoopAccountCheck.text.toString())) {
            //binding.btnContinue.enable(false)
            binding.edtCoopAccountCheck.error = getString(com.ekenya.rnd.common.R.string.invalid_credentials)
            return false
        } else {
            //binding.btnContinue.enable(true)
            return true
        }
    }


}