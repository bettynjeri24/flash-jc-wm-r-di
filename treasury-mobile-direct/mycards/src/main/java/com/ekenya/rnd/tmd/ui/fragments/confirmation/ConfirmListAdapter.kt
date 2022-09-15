package com.ekenya.rnd.tmd.ui.fragments.confirmation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.mycards.databinding.ItemConfirmationBinding

class ConfirmListAdapter : ListAdapter<ConfirmItem, ConfirmListAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConfirmationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val confirmItem = getItem(position)
        holder.itemConfirmationBinding.apply {
            textViewLabel.text = confirmItem.label
            textViewValue.text =  confirmItem.value
        }
    }

    class ViewHolder(val itemConfirmationBinding: ItemConfirmationBinding) : RecyclerView.ViewHolder(itemConfirmationBinding.root)
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<ConfirmItem>() {
    override fun areItemsTheSame(oldItem: ConfirmItem, newItem: ConfirmItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ConfirmItem, newItem: ConfirmItem): Boolean {
        return oldItem == newItem
    }
}

data class ConfirmItem(
    val label: String,
    val value: String
)
