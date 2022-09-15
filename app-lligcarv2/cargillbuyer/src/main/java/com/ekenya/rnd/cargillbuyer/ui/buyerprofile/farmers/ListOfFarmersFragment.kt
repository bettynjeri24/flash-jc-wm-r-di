package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.farmers

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.data.responses.FarmerDetailsData
import com.ekenya.rnd.cargillbuyer.databinding.FragmentListOfFarmersBinding
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.BuyerCargillViewModel
import com.ekenya.rnd.cargillbuyer.utils.setPayfarmerToolbarTitle
import com.ekenya.rnd.common.CARGILL_COOPERATIVEID
import com.ekenya.rnd.common.CARGILL_SECTION
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonBuyerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [ListOfFarmersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ListOfFarmersFragment : BaseCommonBuyerCargillDIFragment<FragmentListOfFarmersBinding>(
    FragmentListOfFarmersBinding::inflate
) {
    private lateinit var farmerListAdapter: FarmerListAdapter

    @Inject
    lateinit var viewModel: BuyerCargillViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpUi()
    }

    private fun setUpUi() {
        getFarmersList()
        setPayfarmerToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.pay_farmer_directly_title),
            resources.getString(com.ekenya.rnd.common.R.string.pay_or_scan),
            binding.mainLayoutToolbar,
            requireActivity()
        )

        binding.tvFarmerNo.text = Html.fromHtml(
            getString(
                com.ekenya.rnd.common.R.string.total_no,
                "12344",
                "#000000"
            )
        )

        binding.ivFilter.setOnClickListener {
            val filter = binding.etSearch.text.toString()
            Toast.makeText(requireContext(), filter, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFarmersList() {
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
        val jsonObject = JSONObject()
        jsonObject.put("cooperativeid", CARGILL_COOPERATIVEID)
        jsonObject.put("sectionid", CARGILL_SECTION)
        jsonObject.put("phonenumber", CURRENT_USER_PHONENUMBER)
        jsonObject.put("endPoint", "apigateway/mobileapp/getfarmerslist")
        lifecycleScope.launch {
            try {
                val response = viewModel.requestGetFarmersList(
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

    private fun inflateRecyclerView(itemsRV: List<FarmerDetailsData>) {
        if (itemsRV.isEmpty()) {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvFarmers.visibility = View.GONE
        } else {
            binding.tvErrorResponse.visibility = View.GONE
            binding.tvErrorResponse.text = itemsRV.toString()
            farmerListAdapter = FarmerListAdapter(itemsRV, onFarmerListListener)
            binding.rvFarmers.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = farmerListAdapter
                setHasFixedSize(true)
            }
            farmerListAdapter.notifyDataSetChanged()
        }
    }

    private val onFarmerListListener =
        object : OnFarmerListListener {
            override fun onItemClicked(
                view: View,
                model: FarmerDetailsData
            ) {
                when (view.id) {
                    R.id.btnPayFarmer -> {
                        findNavController().navigate(
                            ListOfFarmersFragmentDirections.actionListOfFarmersFragmentToBuyerPayFarmerFragment(
                                model
                            )
                        )
                    }
                    R.id.btn_farm_details -> {
                        findNavController().navigate(
                            ListOfFarmersFragmentDirections.actionListOfFarmersFragmentToFarmerDetailsBottomSheetFragment(
                                model
                            )
                        )
                    }
                    else -> {
                        Timber.e("FarmerHomeFragment")
                    }
                }
            }
        }
}
