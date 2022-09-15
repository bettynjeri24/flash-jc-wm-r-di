package com.ekenya.rnd.dashboard.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.databinding.LayoutBillListBinding
import com.ekenya.rnd.dashboard.datadashboard.model.DashboardBillItem

class SendReceiveAdapter(val ctx:Context) : RecyclerView.Adapter<VHolder>() {

    private var sendReceiveItems = mutableListOf<DashboardBillItem>()

    fun setDashBoardItems(dashboardItems: List<DashboardBillItem>) {
        this.sendReceiveItems = dashboardItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutBillListBinding.inflate(inflater, parent, false)
        return VHolder(binding)
    }

    override fun onBindViewHolder(holder:VHolder, position: Int) {

        val dashboardItem = sendReceiveItems[position]
        val bundle = Bundle()


        holder.binding.ivBillName.text = dashboardItem.dashboardItemTitle
        holder.binding.ivBillIcon.setImageResource(dashboardItem.dashboardItemIcon)

    }

    override fun getItemCount(): Int {
        return sendReceiveItems.size
    }

}

class VHolder(val binding: LayoutBillListBinding) :
    RecyclerView.ViewHolder(binding.root) {

}