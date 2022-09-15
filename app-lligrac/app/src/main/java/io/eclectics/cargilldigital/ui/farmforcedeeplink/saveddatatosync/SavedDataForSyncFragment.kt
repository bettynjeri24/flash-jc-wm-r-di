package io.eclectics.cargilldigital.ui.farmforcedeeplink.saveddatatosync

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import io.eclectics.cargilldigital.databinding.FragmentSavedDataForSyncBinding
import io.eclectics.cargilldigital.ui.farmforcedeeplink.viewmodel.FarmForceCargillViewModel
import io.eclectics.cargilldigital.utils.dk.BaseCommonCargillFragment
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SavedDataForSyncFragment : BaseCommonCargillFragment<FragmentSavedDataForSyncBinding>(
    FragmentSavedDataForSyncBinding::inflate
) {

    @Inject
    lateinit var viewModel: FarmForceCargillViewModel

    private lateinit var savedDataForSyncListAdapter: SavedDataForSyncListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserData()
    }

    private fun getUserData() {
        viewModel.getUserVmRoom().observe(
            viewLifecycleOwner
        ) {
            Timber.e("FarmForceData LIST DATA === \n ${it}")
            inflateRecentTransaction(it)
        }
    }

    private fun inflateRecentTransaction(itemsRV: List<FarmForceData>) {
        if (itemsRV.isEmpty()) {
            binding.cvNoDataFound.visibility = View.VISIBLE
            binding.rvFarmer.visibility = View.GONE
        } else {
            binding.cvNoDataFound.visibility = View.GONE
            savedDataForSyncListAdapter =
                SavedDataForSyncListAdapter(itemsRV)
            binding.rvFarmer.apply {
                layoutManager = LinearLayoutManager(this.context!!)
                adapter = savedDataForSyncListAdapter
                setHasFixedSize(true)
            }
            savedDataForSyncListAdapter.notifyDataSetChanged()
        }
    }
}