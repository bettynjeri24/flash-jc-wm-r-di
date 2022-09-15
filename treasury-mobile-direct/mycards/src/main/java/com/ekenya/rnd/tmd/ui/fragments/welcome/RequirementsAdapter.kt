package com.ekenya.rnd.tmd.ui.fragments.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.mycards.databinding.ItemRequirementsBinding

class RequirementsAdapter : ListAdapter<Requirements, RequirementsAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRequirementsBinding = ItemRequirementsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemRequirementsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemRequirementsBinding.apply {
            textViewTitle.text = item.title
            textViewSubTitle.text = item.subtitle
        }
    }

    class ViewHolder(val itemRequirementsBinding: ItemRequirementsBinding) : RecyclerView.ViewHolder(itemRequirementsBinding.root)
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<Requirements>() {
    override fun areItemsTheSame(oldItem: Requirements, newItem: Requirements): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Requirements, newItem: Requirements): Boolean {
        return oldItem == newItem
    }
}
