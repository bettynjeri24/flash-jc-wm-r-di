package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.ffpendingpayments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.data.responses.FfPendingPaymentsData
import com.ekenya.rnd.cargillbuyer.databinding.FragmentFfPendingPaymentListBinding
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

class FFPendingPaymentListFragment :
    BaseCommonBuyerCargillDIFragment<FragmentFfPendingPaymentListBinding>(
        FragmentFfPendingPaymentListBinding::inflate
    ) {

    @Inject
    lateinit var viewModel: BuyerCargillViewModel
    private lateinit var ffPendingPaymentsListAdapter: FfPendingPaymentsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.pending_payments),
            resources.getString(com.ekenya.rnd.common.R.string.pending_payments_subttl),
            binding.mainLayoutToolbar,
            requireActivity()
        )
        getListOfFFPendingPaymentList()
    }

    private fun getListOfFFPendingPaymentList() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        val jsonObject = JSONObject()
        jsonObject.put("buyerPhonenumber", CURRENT_USER_PHONENUMBER)
        lifecycleScope.launch {
            try {
                val response = viewModel.requestFfPendingPayments(
                    jsonObject
                )
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    inflateRecyclerView(response.data!!)
                } else {
                    dismissCustomDialog()
                    // snackBarCustom(it.value.statusDescription)
                    Timber.e("******************registered==1** ${response.statusDescription}")
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

    private fun inflateRecyclerView(itemsRV: List<FfPendingPaymentsData>) {
        if (itemsRV.isEmpty()) {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvPendingPayments.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.tvErrorResponse.text = itemsRV.toString()
            ffPendingPaymentsListAdapter =
                FfPendingPaymentsListAdapter(itemsRV, onFfPendingPaymentsItemListener)
            binding.rvPendingPayments.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = ffPendingPaymentsListAdapter
                setHasFixedSize(true)
            }
            ffPendingPaymentsListAdapter.notifyDataSetChanged()
        }
    }

    private val onFfPendingPaymentsItemListener =
        object : OnFfPendingPaymentsItemListener {
            override fun onItemClicked(
                view: View,
                model: FfPendingPaymentsData
            ) {
                when (view.id) {
                    R.id.clRootPendingPayment -> {
                        val bundle = Bundle()
                        bundle.putParcelable("FfPendingPaymentsData", model)
                        // findNavController().navigate(R.id.ffPendingPaymentDetailsFragment, bundle)
                        findNavController().navigate(
                            FFPendingPaymentListFragmentDirections.actionFFPendingPaymentListFragmentToFfPendingPaymentDetailsFragment(model)
                        )
                    }
                    else -> {
                        Timber.e("FarmerHomeFragment")
                    }
                }
            }
        }
}
