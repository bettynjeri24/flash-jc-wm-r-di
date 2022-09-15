package com.ekenya.rnd.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsItem
import com.ekenya.rnd.onboarding.R
import kotlinx.android.synthetic.main.savingsaccount_item.view.*


class SavingsAccountAdapter(
    private val savingsAccounts: ArrayList<SavingsItem>
) : RecyclerView.Adapter<SavingsAccountAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(savingsaccount: SavingsItem) {
            itemView.tv_savingsTitle.text =savingsaccount.saving_purpose


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.savingsaccount_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = savingsAccounts.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(savingsAccounts[position])

    fun addData(list: List<SavingsItem>) {
        savingsAccounts.clear()
        savingsAccounts.addAll(list)
    }

}