package com.ekenya.rnd.tmd.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.mycards.databinding.ItemMinistatementCardsBinding
import com.ekenya.rnd.tmd.data.network.response.TransactionItem

class TransactionAdapter : ListAdapter<TransactionItem, TransactionAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMinistatementCardsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemMinistatementCardsBinding.apply {
            textViewTitle.text = item.narration
            textViewAmount.text = "Ksh " + item.amount.toString()
            textViewDate.text = item.valueDate
            textView52.text = "Ksh +42.09(2.05%)"
        }
    }

    class ViewHolder(val itemMinistatementCardsBinding: ItemMinistatementCardsBinding) :
        RecyclerView.ViewHolder(itemMinistatementCardsBinding.root){

    }

}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<TransactionItem>(){
    override fun areItemsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
        return oldItem == newItem
    }

}
