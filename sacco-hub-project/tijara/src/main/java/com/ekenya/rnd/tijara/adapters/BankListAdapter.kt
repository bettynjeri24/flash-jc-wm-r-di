package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.databinding.ViewBankInfoItemListsBinding
import com.ekenya.rnd.tijara.network.model.BankInfo
import com.ekenya.rnd.tijara.network.model.ViewBankInfoList

import kotlinx.android.synthetic.main.view_bank_info_item_lists.view.*
import java.util.*


class BankListAdapter(private val onClickListener: OnClickListener): ListAdapter<BankInfo, BankListAdapter.BankViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<BankInfo>() {
        override fun areItemsTheSame(oldItem: BankInfo, newItem: BankInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BankInfo, newItem: BankInfo): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        return BankViewHolder(ViewBankInfoItemListsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        val bankItems = getItem(position)
        holder.itemView.Cl_Bank.setOnClickListener {
            onClickListener.click(bankItems)
        }
        holder.bind(bankItems)
    }

    class BankViewHolder(private val binding: ViewBankInfoItemListsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bankItems: BankInfo) {
            binding.bankProprerties = bankItems
            val posone=bankItems.bank[0].toString().toUpperCase(Locale.ENGLISH)
            val postwo=bankItems.bank[1].toString().toUpperCase(Locale.ENGLISH)
            binding.initials.text=" $posone $postwo"
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedBank: BankInfo) -> Unit){
        fun click(selectedBank: BankInfo)=clickListener(selectedBank)
    }


}