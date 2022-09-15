package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.databinding.RecentLoanListItemsBinding
import com.ekenya.rnd.tijara.network.model.RecentLoanList

class RecentLoanAdapter(var lists:ArrayList<RecentLoanList>):RecyclerView.Adapter<RecentLoanAdapter.RecentLoanViewHolder>()  {

    inner class RecentLoanViewHolder(val binding: RecentLoanListItemsBinding) : RecyclerView.ViewHolder(binding.root)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentLoanViewHolder {
        return RecentLoanViewHolder(RecentLoanListItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount()= lists.size

    override fun onBindViewHolder(holder: RecentLoanViewHolder, position: Int) {
        holder.binding.apply {
            val list = lists[position]
            policeSacco.text=list.name
            tvDate.text=list.dateName
            KesLoan.text=list.amount
            tvPaid.text=list.paidName
            ivCall.setImageResource(list.logo)
        }
    }
}