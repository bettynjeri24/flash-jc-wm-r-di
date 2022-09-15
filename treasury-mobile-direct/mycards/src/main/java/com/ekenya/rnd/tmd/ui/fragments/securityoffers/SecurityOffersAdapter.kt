package com.ekenya.rnd.tmd.ui.fragments.securityoffers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.mycards.databinding.ItemSecurityOfferBinding
import com.ekenya.rnd.tmd.data.network.response.ContentItem

class SecurityOffersAdapter(val onclick: (ContentItem) -> Unit) : ListAdapter<ContentItem, SecurityOffersAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSecurityOfferBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemSecurityOfferBinding.apply {
            textView63.text = item.name
            textView64.text = item.bondReferenceNumber
            root.setOnClickListener {
                onclick(item)
            }
        }
    }

    class ViewHolder(val itemSecurityOfferBinding: ItemSecurityOfferBinding) : RecyclerView.ViewHolder(itemSecurityOfferBinding.root)
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<ContentItem>() {
    override fun areItemsTheSame(oldItem: ContentItem, newItem: ContentItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ContentItem, newItem: ContentItem): Boolean {
        return oldItem == newItem
    }
}
