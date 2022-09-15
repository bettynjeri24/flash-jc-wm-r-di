package com.ekenya.rnd.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.databinding.LayoutRecentTransactionsBinding
import com.ekenya.rnd.dashboard.datadashboard.model.Transaction
import com.ekenya.rnd.onboarding.R

class RecentTransactionsAdapter() : RecyclerView.Adapter<RecentTransItemsVHolder>() {

    private var recentTransItems = mutableListOf<Transaction>()

    fun setDashBoardItems(recentTransItems: List<Transaction>) {
        this.recentTransItems = recentTransItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentTransItemsVHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutRecentTransactionsBinding.inflate(inflater, parent, false)
        return RecentTransItemsVHolder(binding)
    }

    override fun onBindViewHolder(holder:RecentTransItemsVHolder, position: Int) {

        val item = recentTransItems[position]

        holder.binding.tvTransName.text = item.transactionName
        holder.binding.tvNarration.text = item.narration
        holder.binding.txtAmount.text = item.currency+" "+item.amount
        holder.binding.txtTimestamp.text = item.transactiondate

//        if(item.transactionType == "D")
//        {
//            holder.binding.icon.setImageResource(R.drawable.sendmoney_custom)
//        }
//        else
//        {
            holder.binding.icon.setImageResource(R.drawable.ic_send_money)
//        }


    }

    override fun getItemCount(): Int {
        return recentTransItems.size
    }

}

class RecentTransItemsVHolder(val binding: LayoutRecentTransactionsBinding) :
    RecyclerView.ViewHolder(binding.root) {

}