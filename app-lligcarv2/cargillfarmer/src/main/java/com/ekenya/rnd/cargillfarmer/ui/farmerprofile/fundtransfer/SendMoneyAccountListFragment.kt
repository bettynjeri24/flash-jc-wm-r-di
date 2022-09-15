package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.fundtransfer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillfarmer.R
import com.ekenya.rnd.cargillfarmer.data.responses.MyCashOutChannelsData
import com.ekenya.rnd.cargillfarmer.databinding.FragmentSendMoneyAccountListBinding
import com.ekenya.rnd.cargillfarmer.ui.adapters.MyCashOutChannelsAdapter
import com.ekenya.rnd.cargillfarmer.ui.adapters.OnMyCashOutChannelsItemClickedListener
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.MEDIA_TYPE_JSON
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonFarmerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.setToolbarTitle
import com.ekenya.rnd.common.utils.custom.toasty
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class SendMoneyAccountListFragment :
    BaseCommonFarmerCargillDIFragment<FragmentSendMoneyAccountListBinding>(
        FragmentSendMoneyAccountListBinding::inflate
    ),
    OnMyCashOutChannelsItemClickedListener {

    @Inject
    lateinit var viewModel: FarmerViewModel
    private lateinit var myCashOutChannelsAdapter: MyCashOutChannelsAdapter

    private var listData: ArrayList<MyCashOutChannelsData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
    }

    private fun setUpUi() {
        getFetchAPiMyCashOutChannels()
        binding.cardWallet2Wallet.setOnClickListener {
            findNavController().navigate(R.id.transferToCargillWallet)
        }
        setToolbarTitle(
            title = resources.getString(com.ekenya.rnd.common.R.string.transfer_funds),
            description = resources.getString(com.ekenya.rnd.common.R.string.transfer_money_tsubttle),
            mainLayoutToolbar = binding.mainLayoutToolbar,
            activity = requireActivity()
        )
    }

    private fun getFetchAPiMyCashOutChannels() {
        val requestLatestTransactions = JSONObject()
        requestLatestTransactions.put("phonenumber", CURRENT_USER_PHONENUMBER)
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        lifecycleScope.launch {
            try {
                val response = viewModel.requestMyCashOutChannelsData(
                    requestLatestTransactions.toString().toRequestBody(MEDIA_TYPE_JSON)
                )
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    Timber.e("RESPONSE: ${response.myCashOutChannelsData}")
                    inflateRecentTransaction(response.myCashOutChannelsData!!)
                } else {
                    dismissCustomDialog()
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

    private fun inflateRecentTransaction(itemsRV: List<MyCashOutChannelsData>) {
        if (itemsRV.isEmpty()) {
            listData.clear()
            listData.removeAll(itemsRV)
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rcvBeneficialyAcc.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.rcvBeneficialyAcc.visibility = View.VISIBLE
            myCashOutChannelsAdapter =
                MyCashOutChannelsAdapter(itemsRV, this)
            binding.rcvBeneficialyAcc.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = myCashOutChannelsAdapter
                setHasFixedSize(true)
            }
            myCashOutChannelsAdapter?.notifyDataSetChanged()
        }
    }

    override fun onItemClicked(view: View, model: MyCashOutChannelsData) {
        when (view.id) {
            R.id.rootMyCashOutCard -> {
                when (model.channelType) {
                    "Telco" -> {
                        val bundle = Bundle()
                        bundle.putParcelable("MyCashOutChannelsData", model)
                        findNavController().navigate(R.id.transferToTelcoFragment, bundle)
                    }
                    "Aggregator" -> {
                        var bundle = Bundle()
                        bundle.putString("account", model.accountholderphonenumber)
                        findNavController().navigate(R.id.transferToCargillWallet, bundle)
                    }
                    "Bank" -> {
                        var bundle = Bundle()
                        bundle.putString("account", model.accountholderphonenumber)
                        findNavController().navigate(R.id.transferToCargillWallet, bundle)
                    }
                    "Card" -> {
                        var bundle = Bundle()
                        bundle.putString("account", model.accountholderphonenumber)
                        findNavController().navigate(R.id.transferToCargillWallet, bundle)
                    }
                    else -> {
                        toasty("SendMoneyAccountListFragment Soon")
                    }
                }
            }
        }
    }
}

// 500+500+200= 1200
// 550+270= 820
