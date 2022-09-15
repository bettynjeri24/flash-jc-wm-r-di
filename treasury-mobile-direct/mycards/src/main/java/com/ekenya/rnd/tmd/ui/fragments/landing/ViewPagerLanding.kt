package com.ekenya.rnd.tmd.ui.fragments.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.mycards.databinding.ItemLandingBinding

class ViewPagerLanding : ListAdapter<Pager, ViewPagerLanding.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLandingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemLandingBinding.apply {
            imageView3.setImageResource(getItem(position).image)
        }
    }

    class ViewHolder(val itemLandingBinding: ItemLandingBinding) : RecyclerView.ViewHolder(itemLandingBinding.root)
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<Pager>() {
    override fun areItemsTheSame(oldItem: Pager, newItem: Pager): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Pager, newItem: Pager): Boolean {
        return oldItem == newItem
    }
}
