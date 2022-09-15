package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.manageaccounts

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import com.ekenya.rnd.cargillfarmer.R
import com.ekenya.rnd.cargillfarmer.data.responses.MyCashOutChannelsData
import com.ekenya.rnd.cargillfarmer.databinding.FragmentFarmerLinkedAccountBinding
import com.ekenya.rnd.cargillfarmer.ui.adapters.FarmerLinkedAccountAdapter
import com.ekenya.rnd.cargillfarmer.ui.adapters.OnFarmerLinkedAccountListener
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.auth.AuthResult
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.utils.base.BaseCommonFarmerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.*
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.setToolbarTitle
import com.huawei.hms.support.api.entity.common.CommonConstant.RETKEY.USERID
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.time.LocalDateTime
import java.util.HashMap
import javax.inject.Inject

class FarmerLinkedAccountFragment :
    BaseCommonFarmerCargillDIFragment<FragmentFarmerLinkedAccountBinding>(
        FragmentFarmerLinkedAccountBinding::inflate
    ) {
    lateinit var userData: CargillUserLoginResponseData
    private var hashMap: HashMap<String, String> = HashMap()

    @Inject
    lateinit var viewModel: FarmerViewModel
    private var listData: ArrayList<MyCashOutChannelsData> = arrayListOf()
    private lateinit var farmerLinkedAccountAdapter: FarmerLinkedAccountAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("requestKey") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result: AuthResult = bundle.get("authResult") as AuthResult

            when (result) {
                AuthResult.AUTH_SUCCESS -> {
                    sendRemoveBeneficiaryRequest(
                        beneficiaryaccount = hashMap["beneficiaryaccount"].toString()
                    )
                }
                AuthResult.AUTH_ERROR -> {
                    dismissCustomDialog()
                }
            }
        }
        setUpUI()
    }

    private fun setUpUI() {
        getFetchMyCashOutChannelsFromRoom()
        // apiResponseObserver()
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.my_accounts),
            resources.getString(com.ekenya.rnd.common.R.string.mt_account_tsubbtle),
            mainLayoutToolbar = binding.mainLayoutToolbar,
            activity = requireActivity()
        )
        binding.btnAddMore.setOnClickListener {
            findNavController().navigate(R.id.farmerAddBeneficiaryFragment)
        }
    }

    private fun getFetchMyCashOutChannelsFromRoom() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        lifecycleScope.launch {
            try {
                val requestInJsonFormat = JSONObject()
                requestInJsonFormat.put("phonenumber", CURRENT_USER_PHONENUMBER)
                requestInJsonFormat.put("userId", USERID)
                val response =
                    viewModel.requestMyCashOutChannelsLisFromRoom(jsonObject = requestInJsonFormat)
                        .await()
                response.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissCustomDialog()
                        Timber.e("RESPONSE ROOM: $it")
                        if (it.isEmpty()) {
                            binding.rvBeneficiaryAcc.visibilityView(false)
                            binding.tvErrorResponse.visibilityView(true)
                            binding.tvErrorResponse.text =
                                getString(com.ekenya.rnd.common.R.string.no_beneficiary_list)
                        } else {
                            Timber.e("ITEMS 1 $it \n \nTIME ${LocalDateTime.now()}")
                            inflateRecyclerView(it)
                        }
                    }
                )
            } catch (e: ApiExceptions) {
                dismissCustomDialog()
                binding.rvBeneficiaryAcc.visibilityView(false)
                binding.tvErrorResponse.visibilityView(true)
                binding.tvErrorResponse.text = "${e.message}"
                snackBarCustom(e.message.toString()) {}
                Timber.d("ApiExceptions ${e.message}")
            } catch (e: NetworkExceptions) {
                dismissCustomDialog()
                binding.rvBeneficiaryAcc.visibilityView(false)
                binding.tvErrorResponse.visibilityView(true)
                binding.tvErrorResponse.text = "${e.message}"
                snackBarCustom(e.message.toString()) { }
                Timber.d("NetworkExceptions ${e.message}")
            } catch (e: Exception) {
                dismissCustomDialog()
                binding.rvBeneficiaryAcc.visibilityView(false)
                binding.tvErrorResponse.visibilityView(false)
                binding.tvErrorResponse.text = "${e.message}"
                Timber.d("Exceptions ${e.message}")
            }
        }
    }

    private fun sendRemoveBeneficiaryRequest(beneficiaryaccount: String) {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        lifecycleScope.launch {
            try {
                // JSONONJECT
                val removeJson = JSONObject()
                removeJson.put("beneficiaryaccount", beneficiaryaccount)
                removeJson.put("accountholderphonenumber", CURRENT_USER_PHONENUMBER)
                val response = viewModel.requestRemoveBeneficiary(removeJson)
                if (response.statusCode == 0) {
                    // loading.dismissSweetAlert()
                    Timber.e("RESPONSE REMOVE: ${response.data}")
                    hashMapAddAccount["Message"] =
                        response.statusDescription.toString()

                    val createSuccessBundle = createSuccessBundle(
                        title = getString(com.ekenya.rnd.common.R.string.remove_beneficiary_acc),
                        subTitle = getString(com.ekenya.rnd.common.R.string.msg_request_was_successful),
                        cardTitle = response.statusDescription.toString(),
                        cardContent = response.statusDescription.toString(),
                        hashMap = hashMapAddAccount
                    )
                    dismissCustomDialog()
                    findNavController().navigate(
                        R.id.successfulFragmentWallet,
                        createSuccessBundle
                    )
                } else {
                    dismissCustomDialog()
                    requireActivity().showCargillInternalCustomDialog(
                        title = response.statusDescription.toString(),
                        description = response.statusDescription.toString(),
                        positiveButtonFunction = { Timber.e("") },
                        negativeButtonFunction = { Timber.e("") }
                    )
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

    private fun inflateRecyclerView(itemsRV: List<MyCashOutChannelsData>) {
        if (itemsRV.isEmpty()) {
            listData.clear()
            listData.removeAll(itemsRV)
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvBeneficiaryAcc.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.rvBeneficiaryAcc.visibility = View.VISIBLE
            farmerLinkedAccountAdapter =
                FarmerLinkedAccountAdapter(itemsRV, onFarmerLinkedAccountListener)
            binding.rvBeneficiaryAcc.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = farmerLinkedAccountAdapter
                setHasFixedSize(true)
            }
            farmerLinkedAccountAdapter?.notifyDataSetChanged()
        }
    }

    val onFarmerLinkedAccountListener = object : OnFarmerLinkedAccountListener {
        override fun onItemClicked(view: View, model: MyCashOutChannelsData) {
            when (view.id) {
                R.id.rootMaterialCardView -> {
                    Timber.e(model.beneficiaryName.toString())
                }
                R.id.btnRemove -> {
                    hashMap["beneficiaryaccount"] = model.channelNumber.toString()
                    showConfirmationToConfirmSendRequest(
                        beneficiaryaccount = model.channelNumber.toString()
                    )
                }
                else -> {
                    toasty("FarmerHomeFragment")
                }
            }
        }
    }

    private fun showConfirmationToConfirmSendRequest(
        beneficiaryaccount: String
    ) {
        hashMap["Remove Account:"] = "$beneficiaryaccount"
        lifecycleScope.launch {
            showConfirmationDialog(
                "${getString(com.ekenya.rnd.common.R.string.confirm)} ${getString(com.ekenya.rnd.common.R.string.remove_beneficiary_acc)} ",
                "${getString(com.ekenya.rnd.common.R.string.remove_beneficiary_acc)}, " +
                    "$beneficiaryaccount",
                hashMap,
                dialogCallbackRemoveAccount
            )
        }
    }

    private val dialogCallbackRemoveAccount = object : ConfirmDialogCallBacks {
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
