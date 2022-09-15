package com.ekenya.lamparam.ui.sendmoney

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
import com.ekenya.lamparam.ui.receiveMoney.navOptions
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.layout_bill_list.view.*


public interface OnItemClickListener{
    fun onItemClick(view: View, obj: MainDashboard, position:Int)
}
lateinit var navOptions: NavOptions
class FTAdapter (context: FragmentActivity, var mainDashboard:List<MainDashboard>):
        RecyclerView.Adapter<FTAdapter.DashboardViewHolder> (){



    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mItemClicked = clickListener
    }
    inner class DashboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var icon_ImageView = itemView.iv_bill_icon
        var title_TextView = itemView.iv_bill_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        navOptions = UtilityClass().getNavoptions()
        return DashboardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_bill_list,parent,false))
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        var dashboardItem:MainDashboard = mainDashboard[position]
        holder.title_TextView.text= dashboardItem.menuTitle
        holder.icon_ImageView.setImageResource(dashboardItem.imageDrawable)

        holder.itemView.setOnClickListener{v -> setOnCLickedListerner(v,dashboardItem)}
    }

    fun setOnCLickedListerner(v: View, dashboardItem: MainDashboard){

        var bundles = Bundle()
        bundles.putString("title",dashboardItem.menuTitle)
        bundles.putString("slogan",dashboardItem.menuSlogan)
        v.findNavController().navigate(R.id.nav_sendGeneral, bundles, navOptions)

    }


    override fun getItemCount(): Int {
        return  mainDashboard.size
    }

    companion object{
        lateinit var mItemClicked: OnItemClickListener
    }
}