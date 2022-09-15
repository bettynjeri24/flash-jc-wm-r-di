package io.eclectics.cargilldigital.ui.farmforcedeeplink.saveddatatosync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import io.eclectics.cargilldigital.databinding.ItemAdapterSavedForSyncBinding

class SavedDataForSyncListAdapter(
    private val model: List<FarmForceData>
) : ListAdapter<FarmForceData, SavedDataForSyncListAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAdapterSavedForSyncBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = model[position]
        // assign drawable based on the existence of the object in the set
        // itemview clicked
        holder.binding.apply {
            tvFarmerPhoneNumber.text = "Farmer Phone Number :${item.farmerPhonenumber.toString()}"
            tvAmountToPay.text = "Amount :${item.amount.toString()}"
            tvPurchaseID.text = "ID :${item.purchaseId.toString()}"
        }
    }

    override fun getItemCount() = model.size

    inner class ViewHolder(val binding: ItemAdapterSavedForSyncBinding) :
        RecyclerView.ViewHolder(binding.root)
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<FarmForceData>() {
    override fun areItemsTheSame(
        oldItem: FarmForceData,
        newItem: FarmForceData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: FarmForceData,
        newItem: FarmForceData
    ): Boolean {
        return oldItem == newItem
    }
}

interface OnBlueToothListListener {
    fun onItemClicked(view: View, model: FarmForceData)
}
