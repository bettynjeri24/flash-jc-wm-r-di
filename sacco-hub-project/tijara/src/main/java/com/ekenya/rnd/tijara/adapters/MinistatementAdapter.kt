package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.MinistatementItemListBinding
import com.ekenya.rnd.tijara.network.model.MiniStatementData
import kotlinx.android.synthetic.main.biller_categories_list.view.*
import kotlinx.android.synthetic.main.ministatement_item_list.view.*
import kotlinx.android.synthetic.main.statement_account_row.view.*


class MinistatementAdapter(): ListAdapter<MiniStatementData, MinistatementAdapter.MiniStatViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<MiniStatementData>() {
        override fun areItemsTheSame(oldItem: MiniStatementData, newItem: MiniStatementData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MiniStatementData, newItem: MiniStatementData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniStatViewHolder {
        return MiniStatViewHolder(
            MinistatementItemListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: MiniStatViewHolder, position: Int) {
        val miniSItems = getItem(position)
        holder.bind(miniSItems)
        if (miniSItems.transactionType=="Deposit"){
            holder.itemView.tAmount.setTextColor(
                ContextCompat.getColor(holder.itemView.tAmount.context,
                    R.color.ForestGreen))
        }else {
            holder.itemView.tAmount.setTextColor(
                ContextCompat.getColor(holder.itemView.tAmount.context,
                    R.color.textC))
        }
    }


   inner class MiniStatViewHolder(private val binding: MinistatementItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(miniStatData: MiniStatementData) {
            binding.ministatProperties = miniStatData
            binding.executePendingBindings()

        }


    }

}
