package com.ekenya.rnd.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.datadashboard.model.DashboardItem
import com.ekenya.rnd.onboarding.databinding.BudgetItemLayoutBinding

class BudgetBreakdownAdapter(context: Context) :
    RecyclerView.Adapter<BudgetBreakdownViewHolder>() {

    private var dashboardItems = mutableListOf<DashboardItem>()
    private var context = context

    fun setDataItems(dashboardItems: List<DashboardItem>) {
        this.dashboardItems = dashboardItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetBreakdownViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = BudgetItemLayoutBinding.inflate(inflater, parent, false)
        return BudgetBreakdownViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetBreakdownViewHolder, position: Int) {

        val dashboardItem = dashboardItems[position]

        holder.binding.tvAmountconsumed.text = dashboardItem.amount
        holder.binding.tvBillTitle.text = dashboardItem.title
        holder.binding.percentageConsumed.text = "${dashboardItem.icon}%"


    }

    override fun getItemCount(): Int {
        return dashboardItems.size
    }
}

class BudgetBreakdownViewHolder(val binding: BudgetItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

}