package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.fundtransfer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.cargillfarmer.R
import com.ekenya.rnd.cargillfarmer.data.responses.MyCashOutChannelsData
import com.ekenya.rnd.cargillfarmer.databinding.FragmentTransferTotelcoBinding
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import com.ekenya.rnd.cargillfarmer.utils.getImageResource
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.SECRET_KEY
import com.ekenya.rnd.common.auth.AuthResult
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.utils.base.BaseCommonFarmerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.*
import com.ekenya.rnd.common.utils.custom.setToolbarTitle
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class TransferToTelcoFragment : BaseCommonFarmerCargillDIFragment<FragmentTransferTotelcoBinding>(
    FragmentTransferTotelcoBinding::inflate
) {

    @Inject
    lateinit var viewModel: FarmerViewModel
    private var hashMap: HashMap<String, String> = HashMap()
    private lateinit var myCashOutChannelsData: MyCashOutChannelsData
    private lateinit var userData: CargillUserLoginResponseData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            myCashOutChannelsData =
                bundle.getParcelable("MyCashOutChannelsData")!!
        }
        setFragmentResultListener("requestKey") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result: AuthResult = bundle.get("authResult") as AuthResult

            when (result) {
                AuthResult.AUTH_SUCCESS -> {
                    getFetchAPiMyCashOutChannels()
                }
                AuthResult.AUTH_ERROR -> {
                    lifecycleScope.launch {
                        dismissCustomDialog()
                    }
                }
            }
        }
        setUpUi()
    }

    private fun setUpUi() {
        Timber.e("$myCashOutChannelsData+++++++++++++++++++++++++++++++++++++++")
        setListeners()
        viewModel.getUserVmRoom().observe(
            viewLifecycleOwner,
            Observer {
                userData = it
            }
        )
        setToolbarTitle(
            title = resources.getString(com.ekenya.rnd.common.R.string.transfer_towallet),
            description = resources.getString(com.ekenya.rnd.common.R.string.transfer_to_cargill_wallet_subttle),
            mainLayoutToolbar = binding.mainLayoutToolbar,
            activity = requireActivity()
        )
        binding.etAmount.doOnTextChanged { text, start, before, count ->
            setCharge(text)
        }
        binding.tvProviderName.text = myCashOutChannelsData.beneficiaryName
        binding.tvAccountNo.text = "Account: ${myCashOutChannelsData.channelNumber}"
        binding.tvChannelType.text = myCashOutChannelsData.channelType
        binding.tvChannelName.text = myCashOutChannelsData.channelName
        var img = getImageResource(myCashOutChannelsData.channelName.toString())
        binding.imgChannel.setImageResource(img)

        binding.btnTransferToTelco.setOnClickListener {
            if (isvalidField()) {
                transferToTelcoRequest()
            }
        }
    }

    private fun isvalidField(): Boolean {
        val amount = binding.etAmount.text.toString()
        val comments = binding.etComments.text.toString()
        if (!isValidAmount(amount)) {
            binding.etAmount.requestFocus()
            binding.tlAmount.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_amount)
            return false
        }
        if (!isValidComments(comments)) {
            binding.etComments.requestFocus()
            binding.tlComments.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_comments)
            return false
        }
        if (amount.isNotEmpty()) {
            if (amount.toInt() > UtilPreferenceCommon().getCashBalance(requireActivity()).toInt()) {
                toast(getString(com.ekenya.rnd.common.R.string.inssufficient_funds))
                return false
            }
        }
        return true
    }

    private fun setListeners() {
        binding.tlComments.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlComments))
        binding.tlAmount.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlAmount))
    }

    private fun transferToTelcoRequest() {
        hashMap["amount:"] = "${binding.etAmount.text} CFA"
        hashMap["comment:"] = binding.etComments.text.toString()

        lifecycleScope.launch {
            showConfirmationDialog(
                "${getString(com.ekenya.rnd.common.R.string.confirm)} ${getString(com.ekenya.rnd.common.R.string.transfer_to_telco_wallet)} ",
                "${getString(com.ekenya.rnd.common.R.string.transfer_to_telco_wallet)}, " +
                    "${getString(com.ekenya.rnd.common.R.string.phone_number)} :" +
                    "${myCashOutChannelsData.accountholderphonenumber}",
                hashMap,
                dialogCallbackTelco
            )
        }
    }

    private fun setCharge(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            binding.tvCharges.text =
                "${resources.getString(com.ekenya.rnd.common.R.string.available_balance)}: 0"
            binding.tvTotalAmount.visibility = View.INVISIBLE
        } else {
        }
    }

    private fun getFetchAPiMyCashOutChannels() {
        val requestLatestTransactions = JSONObject()
        requestLatestTransactions.put(
            "farmerPhone",
            CURRENT_USER_PHONENUMBER
        )
        requestLatestTransactions.put(
            "amount",
            binding.etAmount.text.toString()
        )
        requestLatestTransactions.put(
            "cashoutChannel",
            myCashOutChannelsData.channeAbbreviation.toString()
        )
        requestLatestTransactions.put(
            "cashoutNumber",
            myCashOutChannelsData.channelNumber.toString()
        )
        requestLatestTransactions.put(
            "channelId",
            myCashOutChannelsData.channelId
        )
        requestLatestTransactions.put(
            "beneficiaryId",
            myCashOutChannelsData.id
        )
        requestLatestTransactions.put(
            "userIndex",
            userData.userIndex
        )
        requestLatestTransactions.put(
            "reasons",
            binding.etComments.text.toString()
        )
        requestLatestTransactions.put(
            "balanceOnRequest",
            "3000"
        )
        requestLatestTransactions.put(
            "cooperativeid",
            userData.cooperativeId
        )
        requestLatestTransactions.put(
            "buyerid",
            userData.cooperativeId
        )
        requestLatestTransactions.put(
            "endPoint",
            "apigateway\\/mobileapp\\/farmercashout"
        )
        requestLatestTransactions.put(
            "pin",
            SECRET_KEY
        )
        /*  requestLatestTransactions.put(
              "deviceUUId",
              deviceSessionUUID()
          )
          requestLatestTransactions.put(
              "deviceId",
              getDeviceId()
          )
          requestLatestTransactions.put(
              "language",
              "fr"
          )*/
        // loading.showLOADINGSweetAlert()

        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))

        lifecycleScope.launch {
            try {
                val response = viewModel.requestFarmerCashoutData(
                    requestLatestTransactions
                )
                Timber.e("RESPONSE: ${response.farmerCashOutData}")
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    hashMap["reasons"] = " ${response.statusDescription}"
                    val createSuccessBundle = createSuccessBundle(
                        title = getString(com.ekenya.rnd.common.R.string.transfer_to_telco_wallet),
                        subTitle = getString(com.ekenya.rnd.common.R.string.msg_request_was_successful),
                        cardTitle = " ${response.statusDescription}",
                        cardContent = " ${response.statusDescription}",
                        hashMap = hashMap
                    )
                    findNavController().navigate(
                        R.id.successfulFragmentWallet,
                        createSuccessBundle
                    )
                } else {
                    requireActivity().showCargillCustomWarningDialog(
                        title = response.statusDescription.toString(),
                        description = response.farmerCashOutData!!.message.toString(),
                        btnConfirmText = getString(com.ekenya.rnd.common.R.string.try_again),
                        action = {}
                    )
                    dismissCustomDialog()

                    toasty("Error:${response.statusDescription}")
                    Timber.e("******************registered==1** ${response.statusDescription}")
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

    private val dialogCallbackTelco = object : ConfirmDialogCallBacks {
        override fun confirm() {
            lifecycleScope.launch {
                findNavController().navigate(R.id.commonAuthFragment)
            }
        }

        override fun cancel() {
            Timber.e("cancel")
        }
    }
}
