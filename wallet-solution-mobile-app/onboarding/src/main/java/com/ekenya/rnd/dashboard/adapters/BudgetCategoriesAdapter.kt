package com.ekenya.rnd.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.datadashboard.model.DashboardItem
import com.ekenya.rnd.onboarding.databinding.PaymentCategoryItemBinding


class BudgetCategoriesAdapter(context: Context) :
    RecyclerView.Adapter<BudgetCategoriesViewHolder>() {

    private var dashboardItems = mutableListOf<DashboardItem>()
    private var context = context

    fun setDataItems(dashboardItems: List<DashboardItem>) {
        this.dashboardItems = dashboardItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetCategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = PaymentCategoryItemBinding.inflate(inflater, parent, false)
        return BudgetCategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetCategoriesViewHolder, position: Int) {

        val dashboardItem = dashboardItems[position]

        holder.binding.tvCategory.text = dashboardItem.title
        /* when (position) {
             0 -> {
                 holder.binding.btnTopUpWallet.setOnClickListener {
                     it.findNavController().navigate(R.id.topUpWalletFragment2)
                     holder.binding.dashboardItemTitle.text = dashboardItem.title
                 }
                 holder.binding.btnViewTransactions.setOnClickListener{
                     it.findNavController().navigate(R.id.transactionsFragment)
                 }
             }

         }

         holder.binding.dashboardItemTitle.text = dashboardItem.title
         holder.binding.tvAmount.text = dashboardItem.amount.toString()
         dashboardItem.icon?.let { holder.binding.dashboardItemIcon.setBackgroundResource(it) }
         dashboardItem.bgColor?.let { holder.binding.bg.setBackgroundResource(it) }*/


    }

    override fun getItemCount(): Int {
        return dashboardItems.size
    }
}

class BudgetCategoriesViewHolder(val binding: PaymentCategoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

}