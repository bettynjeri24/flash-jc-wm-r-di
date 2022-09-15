package com.ekenya.rnd.dashboard.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.datadashboard.model.DashboardItem
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.DashboarditemsLayoutBinding

class DashboardAdapter(context: Context) : RecyclerView.Adapter<MainViewHolder>() {

    private var dashboardItems = mutableListOf<DashboardItem>()
    private var context = context

    fun setDashBoardItems(dashboardItems: List<DashboardItem>) {
        this.dashboardItems = dashboardItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DashboarditemsLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val dashboardItem = dashboardItems[position]
        when (position) {
            0 -> {
                holder.binding.btnTopUpWallet.setOnClickListener {
                    it.findNavController().navigate(R.id.topUpWalletFragment2)
                    holder.binding.dashboardItemTitle.text = dashboardItem.title
                }
                holder.binding.btnViewTransactions.setOnClickListener{
                    it.findNavController().navigate(R.id.mobileWalletFragment)
                }
            }

        }

        holder.binding.dashboardItemTitle.text = dashboardItem.title
        holder.binding.tvAmount.text = dashboardItem.amount.toString()//formatAmount(dashboardItem.amount.toString())

        dashboardItem.icon?.let { holder.binding.dashboardItemIcon.setBackgroundResource(it) }
        dashboardItem.bgColor.let { holder.binding.bg.setBackgroundResource(it) }


    }

    override fun getItemCount(): Int {
        return dashboardItems.size
    }
}

class MainViewHolder(val binding: DashboarditemsLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

}