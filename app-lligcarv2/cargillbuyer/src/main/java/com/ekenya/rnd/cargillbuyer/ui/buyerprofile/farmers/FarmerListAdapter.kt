package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.farmers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillbuyer.data.responses.FarmerDetailsData
import com.ekenya.rnd.cargillbuyer.databinding.ItemAdapterFarmersBinding

class FarmerListAdapter(
    private val model: List<FarmerDetailsData>,
    private val listener: OnFarmerListListener
) : ListAdapter<FarmerDetailsData, FarmerListAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAdapterFarmersBinding.inflate(
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
        /* bind card details */
        val item = model[position]
        holder.binding.apply {
            tvFarmer.text = "${item.firstName} ${item.lastName}"
            tvLocation.text = item.address
            tvCall.text = item.phoneNumber

            btnFarmDetails.setOnClickListener {
                listener.onItemClicked(it, item)
            }
            btnPayFarmer.setOnClickListener {
                listener.onItemClicked(it, item)
            }
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    inner class ViewHolder(val binding: ItemAdapterFarmersBinding) :
        RecyclerView.ViewHolder(binding.root)
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<FarmerDetailsData>() {
    override fun areItemsTheSame(
        oldItem: FarmerDetailsData,
        newItem: FarmerDetailsData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: FarmerDetailsData,
        newItem: FarmerDetailsData
    ): Boolean {
        return oldItem == newItem
    }
}

interface OnFarmerListListener {
    fun onItemClicked(view: View, model: FarmerDetailsData)
}
