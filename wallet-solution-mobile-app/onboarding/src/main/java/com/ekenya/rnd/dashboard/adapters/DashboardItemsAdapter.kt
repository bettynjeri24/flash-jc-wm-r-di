package com.ekenya.rnd.dashboard.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.rnd.dashboard.datadashboard.model.DashboardBillItem
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.DashboardItemLayoutBinding

class DashboardItemsAdapter(context: Context) : RecyclerView.Adapter<DashboardViewHolder>() {

    private var dashboardItems = mutableListOf<DashboardBillItem>()
    private var context = context

    fun setDashBoardItems(dashboardItems: List<DashboardBillItem>) {
        this.dashboardItems = dashboardItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DashboardItemLayoutBinding.inflate(inflater, parent, false)
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {

        val dashboardItem = dashboardItems[position]
        val mBundle = Bundle()
        val ctx = holder.itemView.context
        when (position) {
            0 -> {
                holder.itemView.setOnClickListener {
                    mBundle.putString("source","cashPickup")
                    val intent = Intent(ctx,LampMainActivity::class.java)
                    ctx.startActivity(intent.putExtras(mBundle))
                }
            }
            1 -> {
                holder.itemView.setOnClickListener {
                    mBundle.putString("source","sendMoney")
                    val intent = Intent(ctx,LampMainActivity::class.java)
                    ctx.startActivity(intent.putExtras(mBundle))
                }
            }

            2 -> {
                holder.binding.card.setOnClickListener {
                    it.findNavController().navigate(R.id.airtimeFragment)
                    holder.binding.dashboardItemTitle.text = dashboardItem.dashboardItemTitle
                }
            }
            3 -> holder.binding.card.setOnClickListener {
                it.findNavController().navigate(R.id.billPaymentsFragment)
            }
            4 -> holder.binding.card.setOnClickListener {
                it.findNavController().navigate(R.id.sendMoneyFragment2)
            }
            5 -> holder.binding.card.setOnClickListener {
               // it.findNavController().navigate(R.id.withdrawFragment2)
                it.findNavController().navigate(R.id.growthFragment)

            }
            6 -> holder.binding.card.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_payMerchantFragment2)


            }
            7 -> holder.binding.card.setOnClickListener {

                Toast.makeText(it.context, "Feature Coming Soon", Toast.LENGTH_SHORT).show()


            }
            8 -> holder.binding.card.setOnClickListener {

                context.startActivity(Intent(context, LampMainActivity::class.java))

            }
            9 -> holder.binding.card.setOnClickListener {

                it.findNavController().navigate(R.id.tolloLoyaltyProgram)


            }
            10 -> holder.binding.card.setOnClickListener {

                Toast.makeText(it.context, "Feature Coming Soon", Toast.LENGTH_SHORT).show()
            }
            11 -> holder.binding.card.setOnClickListener {
                it.findNavController().navigate(R.id.growthFragment)
                //Toast.makeText(it.context, "Feature Coming Soon", Toast.LENGTH_SHORT).show()


            }

        }




        holder.binding.dashboardItemTitle.text = dashboardItem.dashboardItemTitle
        holder.binding.dashboardItemIcon.setBackgroundResource(dashboardItem.dashboardItemIcon)
        // dashboardItem.icon?.let { holder.binding.dashboardItemIcon.setBackgroundResource(it) }
        //dashboardItem.bgColor?.let { holder.binding.bg.setBackgroundResource(it) }


    }

    override fun getItemCount(): Int {
        return dashboardItems.size
    }
}

class DashboardViewHolder(val binding: DashboardItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

}