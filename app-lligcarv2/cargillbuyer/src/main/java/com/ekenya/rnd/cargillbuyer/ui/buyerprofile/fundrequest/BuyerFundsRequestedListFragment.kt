package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.fundrequest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.data.responses.BuyerTopUpRequestsData
import com.ekenya.rnd.cargillbuyer.databinding.FragmentFundsRequestedListBinding

import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.BuyerCargillViewModel
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonBuyerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.deviceSessionUUID
import com.ekenya.rnd.common.utils.custom.getDeviceId
import com.ekenya.rnd.common.utils.custom.setToolbarTitle
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class BuyerFundsRequestedListFragment :
    BaseCommonBuyerCargillDIFragment<FragmentFundsRequestedListBinding>(
        FragmentFundsRequestedListBinding::inflate
    ) {
    @Inject
    lateinit var viewModel: BuyerCargillViewModel

    private lateinit var buyerTopUpRequestsAdatper: BuyerTopUpRequestsAdatper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateUI()
    }

    private fun inflateUI() {
        setToolbarTitle(
            getString(com.ekenya.rnd.common.R.string.buyer_fund_requests),
            getString(com.ekenya.rnd.common.R.string.fund_request_ttle),
            binding.mainLayoutToolbar,
            requireActivity()
        )
        getLatestTransactionstList()
    }

    private fun getLatestTransactionstList() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        /*
        *  "userId": "7823-3224",
    "buyeruniqueid": "fc48b6da-b7ed-4aeb-8726-35af83a71d2e",
    "cooperativeid": "1415688e-da10-4947-9621-b5b8249d1165",
    "deviceUUId": "4094df06-a8f1-4360-b56b-a3979b50c118",
    "deviceId": "34036EBCEEFE71FE",
    "language": "en"*/
        val jsonObject = JSONObject()
        jsonObject.put("userId", "7823-3224")
        jsonObject.put("buyeruniqueid", "fc48b6da-b7ed-4aeb-8726-35af83a71d2e")
        jsonObject.put("cooperativeid", "1415688e-da10-4947-9621-b5b8249d1165")
        jsonObject.put("deviceUUId", deviceSessionUUID())
        jsonObject.put("deviceId", requireActivity().getDeviceId())
        jsonObject.put("language", "en")
        lifecycleScope.launch {
            try {
                val response = viewModel.requestbuyertopuprequests(
                    jsonObject
                )
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    inflateRecyclerView(response.data!!)
                    Timber.e("******************DATA==0**\n ${response.data}")
                } else {
                    dismissCustomDialog()
                    // snackBarCustom(it.value.statusDescription)
                    Timber.e("******************STATUSDESCRIPTION==1**\n ${response.statusDescription}")
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

    private fun inflateRecyclerView(itemsRV: List<BuyerTopUpRequestsData>) {
        if (itemsRV.isEmpty()) {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvBuyerTopUpRequests.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.tvErrorResponse.text = itemsRV.toString()
            buyerTopUpRequestsAdatper =
                BuyerTopUpRequestsAdatper(itemsRV)
            binding.rvBuyerTopUpRequests.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = buyerTopUpRequestsAdatper
                setHasFixedSize(true)
            }
            buyerTopUpRequestsAdatper.notifyDataSetChanged()
        }
    }
}
