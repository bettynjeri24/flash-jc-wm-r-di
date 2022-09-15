package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.fundtransfer

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import com.ekenya.rnd.cargillfarmer.databinding.FragmentTransferToCargillWalletBinding
import com.ekenya.rnd.common.utils.base. BaseCommonCargillFragment
import com.ekenya.rnd.common.utils.custom.setToolbarTitle
import com.ekenya.rnd.common.COUNTRY_CODE
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.R
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.utils.custom.CustomTextWatcher
import com.ekenya.rnd.common.utils.custom.isValidAmount
import com.ekenya.rnd.common.utils.custom.isValidComments
import com.ekenya.rnd.common.utils.custom.isValidPhone
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject

class TransferToCargillWallet :  BaseCommonCargillFragment<FragmentTransferToCargillWalletBinding>(
    FragmentTransferToCargillWalletBinding::inflate
) {
    private lateinit var pickContactCallback: ActivityResultCallback<Uri?>
    private lateinit var pickContactLauncher: ActivityResultLauncher<Uri>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        setToolbarTitle(
            resources.getString(R.string.transfer_towallet),
            resources.getString(R.string.transfer_to_cargill_wallet_subttle),
            binding.mainLayoutToolbar,
            requireActivity()
        )

        setListeners()

        binding.btnBuyAirtime.setOnClickListener {
            //  findNavController().navigate(R.id.nav_verifyOtp)
            if (ifValidField()) {
                sendTransferRequest()
            }
        }

    }

    private fun setListeners() {
        binding.tlPhonenumber.endIconMode = TextInputLayout.END_ICON_CUSTOM
        binding.tlPhonenumber.endIconDrawable = resources.getDrawable(com.ekenya.rnd.common.R.drawable.ic_phonebook)

        binding.tlComments.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlComments))
        binding.tlAmount.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlAmount))
    }

    private fun sendTransferRequest() {
        var transAmount = binding.etAmount.text.toString()
        var phoneNumber = binding.etPhoneNumber.text.toString()
        var recipientPhoneNumber = "${COUNTRY_CODE}${phoneNumber}"

        var lookupJson = JSONObject()
        lookupJson.put("sendorPhoneNumber", CURRENT_USER_PHONENUMBER)
        lookupJson.put("recipientPhoneNumber", recipientPhoneNumber)
        lookupJson.put("amount", transAmount)
        lookupJson.put("userIndex", "")
        lookupJson.put("farmerId", "")

        lookupJson.put("reason", binding.etComments.text.toString())
        lookupJson.put("cooperativeid", "")
        lookupJson.put("userid", "")

        //findNavController().navigate(R.id.nav_transactionConfirmation)
    }


    private fun ifValidField(): Boolean {
        val accountNumber = binding.etPhoneNumber.text.toString()
        val amount = binding.etAmount.text.toString()
        val comments = binding.etComments.toString()
        if (isValidPhone(accountNumber)) {
            binding.etPhoneNumber.requestFocus()
            binding.tlPhonenumber.error =
                resources.getString(com.ekenya.rnd.common.R.string.validation_phone_number)
            return false
        }
        if (!isValidAmount(amount)) {
            binding.etAmount.requestFocus()
            binding.tlAmount.error = resources.getString(com.ekenya.rnd.common.R.string.validation_amount)
            return false
        }
        if (!isValidComments(comments)) {
            binding.etComments.requestFocus()
            binding.tlComments.error = resources.getString(com.ekenya.rnd.common.R.string.enter_comments)
            return false
        }
        if (amount.isNotEmpty()) {
            if (amount.toInt() > 10000) {
                toast(
                    resources.getString(com.ekenya.rnd.common.R.string.inssufficient_funds)
                )
                return false
            }
        }
        return true
    }


}
