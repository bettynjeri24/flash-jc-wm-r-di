package com.ekenya.rnd.cargillfarmer.ui.otp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.cargillfarmer.R
import com.ekenya.rnd.cargillfarmer.databinding.FragmentOtpBinding
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import com.ekenya.rnd.cargillfarmer.ui.farmerprofile.manageaccounts.hashMapAddAccount
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.SECRET_KEY
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.utils.base.BaseCommonFarmerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.createSuccessBundle
import com.ekenya.rnd.common.utils.custom.isValidOTP
import com.ekenya.rnd.common.utils.custom.setToolbarTitle
import com.ekenya.rnd.common.utils.custom.showCargillInternalCustomDialog
import com.ekenya.rnd.common.utils.custom.timber
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.HashMap
import javax.inject.Inject

class OTPFragment : BaseCommonFarmerCargillDIFragment<FragmentOtpBinding>(
    FragmentOtpBinding::inflate
) {
    private var hashMap: HashMap<String, String> = HashMap()

    @Inject
    lateinit var viewModel: FarmerViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.verification_code),
            resources.getString(com.ekenya.rnd.common.R.string.add_beneficiary_subbtle),
            binding.mainLayoutToolbar,
            requireActivity()
        )
        val recipientNumber =
            hashMapAddAccount[resources.getString(com.ekenya.rnd.common.R.string.phone_number)]
        binding.tvPhoneNo.text = recipientNumber
        binding.btnVerifyOtp.setOnClickListener {
            if (validData()) {
                showConfirmationToConfirmSendRequest()
            }
        }
    }

    private fun showConfirmationToConfirmSendRequest() {
        lifecycleScope.launch {
            showConfirmationDialog(
                "${getString(com.ekenya.rnd.common.R.string.confirm)} ${getString(com.ekenya.rnd.common.R.string.remove_beneficiary_acc)} ",
                "${getString(com.ekenya.rnd.common.R.string.remove_beneficiary_acc)}, " +
                    binding.tvPhoneNo.text.toString(),
                hashMap,
                dialogCallbackVeridyAccount
            )
        }
    }

    private val dialogCallbackVeridyAccount = object : ConfirmDialogCallBacks {
        override fun confirm() {
            sendVerifyBeneficairyAccount()
        }

        override fun cancel() {
            timber("cancel")
        }
    }

    private fun sendVerifyBeneficairyAccount() {
        lifecycleScope.launch {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("pin", SECRET_KEY)
                jsonObject.put(
                    "newNumber",
                    hashMapAddAccount[resources.getString(com.ekenya.rnd.common.R.string.phone_number)]
                )
                jsonObject.put("otp", binding.edtOtp.text.toString())
                jsonObject.put("accountholderphonenumber", CURRENT_USER_PHONENUMBER)
                val response = viewModel.requestVerifyAddAccountData(jsonObject)
                if (response.statusCode == 0) {
                    // loading.dismissSweetAlert()
                    timber("RESPONSE: ${response.verifyAddAccountData}")
                    hashMapAddAccount["Message"] =
                        response.statusDescription.toString()
                    val createSuccessBundle = createSuccessBundle(
                        title = getString(com.ekenya.rnd.common.R.string.verify),
                        subTitle = getString(com.ekenya.rnd.common.R.string.msg_request_was_successful),
                        cardTitle = " ${response.statusDescription}",
                        cardContent = response.statusDescription.toString(),
                        hashMap = hashMapAddAccount
                    )
                    findNavController().navigate(
                        R.id.successfulFragmentWallet,
                        createSuccessBundle
                    )
                } else {
                    dismissCustomDialog()
                    requireActivity().showCargillInternalCustomDialog(
                        title = response.statusDescription.toString(),
                        description = response.statusDescription.toString(),
                        btnConfirmText = getString(com.ekenya.rnd.common.R.string.retry_request),
                        positiveButtonFunction = { timber("") },
                        negativeButtonFunction = { timber("") }
                    )
                    timber("******************registered==1** $response")
                    timber("******************registered==1** ${response.statusDescription}")
                }
            } catch (e: ApiExceptions) {
                toast(e.toString())
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                toast(e.toString())
                dismissCustomDialog()
            }
        }
    }

    private fun submitRequest() {
        val createSuccessBundle = createSuccessBundle(
            title = getString(com.ekenya.rnd.common.R.string.add_beneficiary),
            subTitle = getString(com.ekenya.rnd.common.R.string.msg_request_was_successful),
            cardTitle = getString(com.ekenya.rnd.common.R.string.phone_number),
            cardContent = hashMapAddAccount[resources.getString(com.ekenya.rnd.common.R.string.phone_number)].toString(),
            hashMap = hashMapAddAccount
        )
        findNavController().navigate(
            R.id.successfulFragmentWallet,
            createSuccessBundle
        )
    }

    private fun validData(): Boolean {
        val otp = binding.edtOtp.text.toString()
        if (!isValidOTP(otp)) {
            binding.tvEnterotp.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_phoneNo)
            return false
        }
        return true
    }
}
