package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.latestTransactions

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillbuyer.data.responses.LatestTransactionsData
import com.ekenya.rnd.cargillbuyer.databinding.FragmentBuyerRecentTranactionBinding
import com.ekenya.rnd.cargillbuyer.ui.adapter.LatestTransactionsAdapter
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.BuyerCargillViewModel
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonBuyerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.setToolbarTitle
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class BuyerRecentTransactionsFragment :
    BaseCommonBuyerCargillDIFragment<FragmentBuyerRecentTranactionBinding>(
        FragmentBuyerRecentTranactionBinding::inflate
    ) {
    @Inject
    lateinit var viewModel: BuyerCargillViewModel

    private lateinit var latestTransactionsAdapter: LatestTransactionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.recent_transactions),
            resources.getString(com.ekenya.rnd.common.R.string.recent_transactions),
            binding.mainLayoutToolbar,
            requireActivity()
        )

        getLatestTransactionstList()
    }

    private fun getLatestTransactionstList() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        val jsonObject = JSONObject()
        jsonObject.put("phoneNumber", CURRENT_USER_PHONENUMBER)
        lifecycleScope.launch {
            try {
                val response = viewModel.requestlatestTransactions(
                    jsonObject
                )
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    inflateRecyclerView(response.data!!)
                    Timber.e("******************data==0**\n ${response.data}")
                } else {
                    dismissCustomDialog()
                    // snackBarCustom(it.value.statusDescription)
                    Timber.e("******************statusDescription==1**\n ${response.statusDescription}")
                }
            } catch (e: ApiExceptions) {
                toast(e.message.toString())
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                toast(e.message.toString())
                dismissCustomDialog()
            } catch (e: Exception) {
                toast(e.message.toString())
                dismissCustomDialog()
            }
        }
    }

    private fun inflateRecyclerView(itemsRV: List<LatestTransactionsData>) {
        if (itemsRV.isEmpty()) {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rcLatestTransactions.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.tvErrorResponse.text = itemsRV.toString()
            latestTransactionsAdapter =
                LatestTransactionsAdapter(itemsRV)
            binding.rcLatestTransactions.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = latestTransactionsAdapter
                setHasFixedSize(true)
            }
            latestTransactionsAdapter.notifyDataSetChanged()
        }
    }
}
