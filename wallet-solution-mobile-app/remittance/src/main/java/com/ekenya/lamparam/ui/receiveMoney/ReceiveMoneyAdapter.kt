package com.ekenya.lamparam.ui.receiveMoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.R
import com.ekenya.lamparam.ui.dashboard.MainDashboard
import com.ekenya.lamparam.ui.home.OnItemClickListener
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.layout_bill_list.view.*

public interface OnItemClickListener {
    fun onItemClick(view: View, obj: MainDashboard, position: Int)
}

lateinit var navOptions: NavOptions

class ReceiveMoneyAdapter(context: FragmentActivity, var mainDashboard: List<MainDashboard>) :
    RecyclerView.Adapter<ReceiveMoneyAdapter.DashboardViewHolder>() {

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mItemClicked = clickListener
    }

    inner class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon_ImageView = itemView.iv_bill_icon
        var title_TextView = itemView.iv_bill_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        navOptions = UtilityClass().getNavoptions()
        return DashboardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_bill_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        var dashboardItem: MainDashboard = mainDashboard[position]
        holder.title_TextView.text = dashboardItem.menuTitle
        holder.icon_ImageView.setImageResource(dashboardItem.imageDrawable)

        holder.itemView.setOnClickListener { v ->
            setOnCLickedListerner(
                v,
                position,
                dashboardItem
            )
        }
    }

    fun setOnCLickedListerner(v: View, position: Int, dashboardItem: MainDashboard) {

        when (dashboardItem.menuId) {
            // 1-> v.findNavController().navigate(R.id.nav_receiveFromEcobank)
            1 -> {
                var bundles = Bundle()
                bundles.putString("title", "ECOBANK")
                bundles.putString("slogan", ":THE PAN AFRICAN BANK")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            2 -> {
                var bundles = Bundle()
                bundles.putString("title", "Money Gram")
                bundles.putString("slogan", ":Bringing you closer")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            3 -> {
                var bundles = Bundle()
                bundles.putString("title", "MTN Money")
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            4 -> {
                var bundles = Bundle()
                bundles.putString("title", "Monammon")
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            5 -> {
                var bundles = Bundle()
                bundles.putString("title", "Orange Money")
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            6 -> {
                var bundles = Bundle()
                bundles.putString("title", "Ria Money")
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            7 -> {
                var bundles = Bundle()
                bundles.putString("title", "Western Union")
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            8 -> {
                var bundles = Bundle()
                bundles.putString("title", "Wari")
                bundles.putString("slogan", ":It Serves Everyone ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            9 -> {
                var bundles = Bundle()
                bundles.putString("title", "Real Transfer")
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            10 -> {
                var bundles = Bundle()
                bundles.putString("title", "Small World")
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            11 -> {
                var bundles = Bundle()
                bundles.putString("title", "World Remit")
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            else -> {
                var bundles = Bundle()
                bundles.putString("title", dashboardItem.menuTitle)
                bundles.putString("slogan", ". ")
                v.findNavController().navigate(R.id.nav_setAmount, bundles, navOptions)
            }
            /*5-> v.findNavController().navigate(R.id.nav_paybill)//nav_loanMenu
           1 -> v.findNavController().navigate(R.id.nav_buyAirtime)
             2 -> v.findNavController().navigate(R.id.nav_fundWallet)//nav_myCards

             5-> v.findNavController().navigate(R.id.nav_myCards)
             7-> v.findNavController().navigate(R.id.nav_loanMenu)//
             8-> v.findNavController().navigate(R.id.nav_paybill)//nav_loanMenu
             9-> v.findNavController().navigate(R.id.nav_scanQRCode)//
             10-> v.findNavController().navigate(R.id.nav_requestMenu)//nav_requestMenu
             11-> v.findNavController().navigate(R.id.nav_sendMoneyMenu)//
             12-> v.findNavController().navigate(R.id.nav_withdrawalMenu)//nav_withdrawalMenu*/
        }
    }

    override fun getItemCount(): Int {
        return mainDashboard.size
    }

    companion object {
        lateinit var mItemClicked: OnItemClickListener
    }
}