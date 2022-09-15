package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.statements

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillfarmer.data.responses.FarmerLatestTransactionData
import com.ekenya.rnd.cargillfarmer.databinding.FragmentFarmerMinistatementBinding
import com.ekenya.rnd.cargillfarmer.ui.adapters.MiniStatementAccountWalletAdapter
import com.ekenya.rnd.cargillfarmer.ui.adapters.OnFarmerRecentTransactionItemListener
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import com.ekenya.rnd.common.AVAILABLE_BALANCE
import com.ekenya.rnd.common.utils.base.BaseCommonFarmerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.MEDIA_TYPE_JSON
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.TOTAL_MONTHLY_CASHOUT
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.custom.setBackButton
import com.huawei.hms.support.api.entity.common.CommonConstant.RETKEY.USERID
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class FarmerMiniStatementCargillFragment :
    BaseCommonFarmerCargillDIFragment<FragmentFarmerMinistatementBinding>(
        FragmentFarmerMinistatementBinding::inflate
    ) {
    @Inject
    lateinit var viewModel: FarmerViewModel
    private lateinit var miniStatementAccountWalletAdapter: MiniStatementAccountWalletAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAvailableBalanceAmount.text = AVAILABLE_BALANCE
        binding.tvActualBalanceAmount.text = TOTAL_MONTHLY_CASHOUT
        binding.clToolBar.toolbar.setBackButton(
            (com.ekenya.rnd.common.R.string.mini_statement),
            requireActivity()
        )
        getFetchAPiRecentTransaction()
    }

    /**
     * LatestTransactionsResponse
     */

    private fun getFetchAPiRecentTransaction() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        lifecycleScope.launch {
            try {
                val requestLatestTransactions = JSONObject()
                requestLatestTransactions.put("phonenumber", CURRENT_USER_PHONENUMBER)
                requestLatestTransactions.put("userId", USERID)
                //loading.showLOADINGSweetAlert()

                val response = viewModel.requestLatestTransactionsResponse(
                    requestLatestTransactions.toString().toRequestBody(MEDIA_TYPE_JSON)
                )
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    Timber.e(
                        "RESPONSE: ${
                            response.farmerLatestTransactionData
                        }"
                    )
                    inflateRecentTransaction(response.farmerLatestTransactionData!!)
                    lifecycleScope.launch {
                        viewModel.saveVmRoom(
                            response.farmerLatestTransactionData!!
                        )
                    }
                } else {
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


    private fun inflateRecentTransaction(itemsRV: List<FarmerLatestTransactionData>) {
        if (itemsRV.isEmpty()) {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvMiniStatement.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.tvErrorResponse.setText(itemsRV.toString())
            miniStatementAccountWalletAdapter =
                MiniStatementAccountWalletAdapter(
                    itemsRV.reversed()
                )
            binding.rvMiniStatement.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = miniStatementAccountWalletAdapter
                setHasFixedSize(true)
            }
            miniStatementAccountWalletAdapter?.notifyDataSetChanged()
        }
    }

    private val onRecentTransactionItemClickedListener =
        object : OnFarmerRecentTransactionItemListener {
            override fun onItemClicked(view: View, model: FarmerLatestTransactionData) {
                when (view.id) {

                    else -> {
                        Timber.e("FarmerHomeFragment")
                    }
                }
            }
        }


}