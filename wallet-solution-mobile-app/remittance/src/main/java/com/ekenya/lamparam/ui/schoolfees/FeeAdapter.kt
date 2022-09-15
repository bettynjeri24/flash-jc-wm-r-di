package com.ekenya.lamparam.ui.schoolfees

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.R
import com.ekenya.lamparam.ui.dashboard.MainDashboard
import com.ekenya.lamparam.ui.home.OnItemClickListener
import kotlinx.android.synthetic.main.adapter_dashboard.view.*
import kotlinx.android.synthetic.main.layout_school_list.view.*

public interface OnItemClickListener{
    fun onItemClick(view: View, obj: SchoolTypeModel, position:Int)
}
class FeeAdapter (context: FragmentActivity, var mainDashboard:List<SchoolTypeModel>):
    RecyclerView.Adapter<FeeAdapter.DashboardViewHolder> (){



    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mItemClicked = clickListener
    }
    inner class DashboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        var icon_ImageView = itemView.iv_menu_icon
        var title_TextView = itemView.title_TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_school_list,parent,false))
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        var dashboardItem:SchoolTypeModel = mainDashboard[position]
        holder.title_TextView.text= dashboardItem.title
       // holder.icon_ImageView.setImageResource(dashboardItem.imageDrawable)

        holder.itemView.setOnClickListener{v -> setOnCLickedListerner(v,position,dashboardItem)}
    }

    fun setOnCLickedListerner(v: View, position: Int, dashboardItem: SchoolTypeModel){
        v.findNavController().navigate(R.id.nav_createStudent)
       // when(dashboardItem.menuId){  //nav_schoolCategory   nav_createStudent
           // 1-> v.findNavController().navigate(R.id.nav_receiveFromEcobank)

            /* 4-> v.findNavController().navigate(R.id.nav_paymerchant)
           5-> v.findNavController().navigate(R.id.nav_paybill)//nav_loanMenu
          1 -> v.findNavController().navigate(R.id.nav_buyAirtime)
            2 -> v.findNavController().navigate(R.id.nav_fundWallet)//nav_myCards

            5-> v.findNavController().navigate(R.id.nav_myCards)
            7-> v.findNavController().navigate(R.id.nav_loanMenu)//
            8-> v.findNavController().navigate(R.id.nav_paybill)//nav_loanMenu
            9-> v.findNavController().navigate(R.id.nav_scanQRCode)//
            10-> v.findNavController().navigate(R.id.nav_requestMenu)//nav_requestMenu
            11-> v.findNavController().navigate(R.id.nav_sendMoneyMenu)//
            12-> v.findNavController().navigate(R.id.nav_withdrawalMenu)//nav_withdrawalMenu*/
       // }
    }
    override fun getItemCount(): Int {
        return  mainDashboard.size
    }

    companion object{
        lateinit var mItemClicked: OnItemClickListener
    }
}