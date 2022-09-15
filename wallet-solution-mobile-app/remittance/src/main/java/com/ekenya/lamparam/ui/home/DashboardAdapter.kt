package com.ekenya.lamparam.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.R
import com.ekenya.lamparam.ui.dashboard.MainDashboard
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.adapter_dashboard.view.*


public interface OnItemClickListener{
    fun onItemClick(view:View, obj: MainDashboard, position:Int)
}

lateinit var navOptions: NavOptions
class DashboardAdapter(context:FragmentActivity,var mainDashboard:List<MainDashboard>):RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> (){


    private val mContext = context

    fun setOnItemClickListener(clickListener:OnItemClickListener){
        mItemClicked = clickListener
    }
    inner class DashboardViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var icon_ImageView = itemView.iv_menu_icon
        var title_TextView = itemView.tv_menu_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        navOptions = UtilityClass().getNavoptions()
        return DashboardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_dashboard,parent,false))
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        var dashboardItem:MainDashboard = mainDashboard[position]
        holder.title_TextView.text= dashboardItem.menuTitle
        holder.icon_ImageView.setImageResource(dashboardItem.imageDrawable)

        holder.itemView.setOnClickListener{v -> setOnCLickedListerner(v,position,dashboardItem)}
    }

    fun setOnCLickedListerner(v: View, position: Int, dashboardItem: MainDashboard){

        when(dashboardItem.menuId){
            1 -> v.findNavController().navigate(R.id.nav_receiveMoneyMenu,null, navOptions)
            2 -> v.findNavController().navigate(R.id.nav_sendMoney)//nav_myCards
            3-> v.findNavController().navigate(R.id.nav_buyAirtime,null, navOptions)
            4-> v.findNavController().navigate(R.id.nav_paymerchant,  null, navOptions)
            5-> v.findNavController().navigate(R.id.nav_paybill)//nav_loanMenu
            6 -> v.findNavController().navigate(R.id.nav_schoolCategory,null, navOptions)
            7 -> v.findNavController().navigate(R.id.nav_cashDeposit,null, navOptions)
            8 -> v.findNavController().navigate(R.id.nav_cashwithdraw,
                null,
                navOptions)
            9 -> Toast.makeText(mContext, "Coming soon", Toast.LENGTH_SHORT).show()
            10 -> v.findNavController().navigate(R.id.nav_microLoans,
                null,
                navOptions)
            11-> v.findNavController().navigate(R.id.nav_funds_transfer)//
            //nav_microLoans
           /*)//nav_feeMenu   nav_createStudent
            2 -> v.findNavController().navigate(R.id.nav_sendMoney)//nav_myCards

            5-> v.findNavController().navigate(R.id.nav_myCards)
            7-> v.findNavController().navigate(R.id.nav_loanMenu)//
            8-> v.findNavController().navigate(R.id.nav_paybill)//nav_loanMenu
            9-> v.findNavController().navigate(R.id.nav_scanQRCode)//
            10-> v.findNavController().navigate(R.id.nav_requestMenu)//nav_requestMenu

            12-> v.findNavController().navigate(R.id.nav_withdrawalMenu)//nav_withdrawalMenu*/
        }
    }
    override fun getItemCount(): Int {
      return  mainDashboard.size
    }

    companion object{
        lateinit var mItemClicked:OnItemClickListener

    }
   /* init {
        navOptions = UtilityClass().getNavoptions()
    }*/
}