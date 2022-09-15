package com.ekenya.rnd.baseapp.ui.bixolon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.baseapp.R
import com.ekenya.rnd.baseapp.databinding.ItemAdapterBluetoothDevicesBinding

class BlueToothListAdapter(
    private val blueToothData: List<BlueToothData>,
    private val listener: OnBlueToothListListener
) : ListAdapter<BlueToothData, BlueToothListAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAdapterBluetoothDevicesBinding.inflate(
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
        val item = blueToothData[position]
        // assign drawable based on the existence of the object in the set
        // itemview clicked
        holder.binding.apply {
            tvBlueToothName.text = item.name.toString()
            imageViewBlueToothIndicate.setImageResource(R.drawable.ic_notselected_wallet)
            rootTransaction.setOnClickListener {
                listener.onItemClicked(it, item!!)
                // maintain a set of selected contacts in UI using a higher order function
                // if (!onContactSelected.invoke(item)) {
                // object exists
                // imageViewBlueToothIndicate.setImageResource(R.drawable.ic_notselected_wallet)
                // } else {
                // object doesn't exist
                imageViewBlueToothIndicate.setImageResource(R.drawable.ic_selected_wallet)
                // }
            }
        }
    }

    override fun getItemCount(): Int {
        return blueToothData.size
    }

    inner class ViewHolder(val binding: ItemAdapterBluetoothDevicesBinding) :
        RecyclerView.ViewHolder(binding.root)
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<BlueToothData>() {
    override fun areItemsTheSame(
        oldItem: BlueToothData,
        newItem: BlueToothData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: BlueToothData,
        newItem: BlueToothData
    ): Boolean {
        return oldItem == newItem
    }
}

interface OnBlueToothListListener {
    fun onItemClicked(view: View, model: BlueToothData)
}
