package com.ekenya.lamparam.ui.paybill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.R
import com.ekenya.lamparam.ui.dashboard.MainDashboard

import kotlinx.android.synthetic.main.layout_bill_list.view.*

class PaybillAdapter (context: FragmentActivity, var mainDashboard:List<MainDashboard>):
    RecyclerView.Adapter<PaybillAdapter.DashboardViewHolder> (){



    fun setOnItemClickListener(clickListener: AdapterView.OnItemClickListener){
        mItemClicked = clickListener
    }
    inner class DashboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var icon_ImageView = itemView.iv_bill_icon
        var title_TextView = itemView.iv_bill_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_bill_list,parent,false))
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        var dashboardItem: MainDashboard = mainDashboard[position]
        holder.title_TextView.text= dashboardItem.menuTitle
        holder.icon_ImageView.setImageResource(dashboardItem.imageDrawable)

        holder.itemView.setOnClickListener{v -> setOnCLickedListerner(v,position,dashboardItem)}
    }

    fun setOnCLickedListerner(v: View, position: Int, dashboardItem: MainDashboard){

        v.findNavController().navigate(R.id.nav_payUmeme)
    }
    override fun getItemCount(): Int {
        return  mainDashboard.size
    }

    companion object{
        lateinit var mItemClicked: AdapterView.OnItemClickListener
    }
}