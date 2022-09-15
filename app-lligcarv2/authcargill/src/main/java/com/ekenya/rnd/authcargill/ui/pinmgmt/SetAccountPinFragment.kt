package com.ekenya.rnd.authcargill.ui.pinmgmt

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.databinding.FragmentSetAccountPinBinding
import com.ekenya.rnd.authcargill.ui.AuthCargillViewModel

import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonAuthCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.isWeakPin
import com.ekenya.rnd.common.utils.custom.timber
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


class SetAccountPinFragment : BaseCommonAuthCargillDIFragment<FragmentSetAccountPinBinding>
    (FragmentSetAccountPinBinding::inflate) {

    @Inject
    lateinit var authCargillViewModel: AuthCargillViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {
            if (isValidFields()) {
                showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
                setNewAccountPinRequest()
            }
        }
    }

    private fun setNewAccountPinRequest() {
        lifecycleScope.launch {
            try {
                val phonenumber =  UtilPreferenceCommon().getLoggedPhoneNumber(requireActivity())
                val jsonObject = JSONObject()
                jsonObject.put("pin", binding.etPassword.text.toString().trim())
                jsonObject.put("phonenumber", phonenumber.trim())
                val response = authCargillViewModel.setNewAccountPin(jsonObject)
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    UtilPreferenceCommon().setTirstTimeLogin(requireActivity(), false)
                    findNavController().navigate(R.id.action_setAccountPinFragment_to_loginPinFragment)
                } else {
                    timber(response.toString())
                    dismissCustomDialog()
                }
            } catch (e: ApiExceptions) {
                timber(e.toString())
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                timber(e.toString())
                dismissCustomDialog()
            }
        }
    }
    private fun isValidFields(): Boolean {
        val pin = binding.etPassword.text.toString()
        val confirmPin = binding.etConfirmPassword.text.toString()

        if (pin.isWeakPin()) {
            // binding.etPassword.requestFocus()
            binding.tlPassword.error = resources.getString(com.ekenya.rnd.common.R.string.enter_secure_pin)
            return false
        }
        if (pin.isWeakPin()) {
            // binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(com.ekenya.rnd.common.R.string.enter_secure_pin)
            return false
        }
        if (!pin.trim().contentEquals(confirmPin.trim())) {
            //binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(com.ekenya.rnd.common.R.string.new_confirm_notmatch)
            return false
        }
 /*       if (isValidPIN(pin)) {
            // binding.etPassword.requestFocus()
            binding.tlPassword.error = resources.getString(R.string.enter_secure_pin)
            return false
        }
        if (isValidPIN(pin)) {
            // binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(R.string.enter_secure_pin)
            return false
        }
        if (!pin.trim().contentEquals(confirmPin.trim())) {
            //binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(R.string.new_confirm_notmatch)
            return false
        }*/

        return true

    }

}