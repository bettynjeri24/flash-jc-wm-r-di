package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.databinding.AccountSummaryListItemsBinding
import com.ekenya.rnd.tijara.network.model.TransactionDetails

class AccountSummaryAdapter(private val transList:List<TransactionDetails>)
    :RecyclerView.Adapter<AccountSummaryAdapter.AccountSummaryViewHolder>(

) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountSummaryViewHolder {
        return AccountSummaryViewHolder(AccountSummaryListItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AccountSummaryViewHolder, position: Int) {

        val accountDetails = transList[position]
        holder.bind(accountDetails)
    }


    class AccountSummaryViewHolder(private val binding: AccountSummaryListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(accTypes: TransactionDetails) {
            binding.accountItems = accTypes
            binding.tvAmountInSh.text=accTypes.balance
            binding.tvRefNumber.text=accTypes.refCode
            binding.tvDebitAmount.text=accTypes.debit
            binding.tvCreditAmnt.text=accTypes.credit
            binding.tvTDate.text=accTypes.transactionDate
        }

    }

    override fun getItemCount(): Int {
        return  transList.size
    }

}
