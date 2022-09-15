package com.ekenya.rnd.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.datadashboard.model.DashboardBillItem
import com.ekenya.rnd.onboarding.R

class PhotoAdapter(var context: Context) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    var dataList = emptyList<DashboardBillItem>()

    internal fun setDataList(dataList: List<DashboardBillItem>) {
        this.dataList = dataList
    }

    // Provide a direct reference to each of the views with data items
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var title: TextView
       // var desc: TextView

        init {
            image = itemView.findViewById(R.id.dashboard_item_icon)
            title = itemView.findViewById(R.id.dashboard_item_title)
           // desc = itemView.findViewById(R.id.desc)
        }

    }

  // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.ViewHolder {
    
      // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item_layout, parent, false)
        return ViewHolder(view)
    }

 // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: PhotoAdapter.ViewHolder, position: Int) {
    
     // Get the data model based on position
        var data = dataList[position]

       // Set item views based on your views and data model
        holder.title.text = data.dashboardItemTitle
        //holder.desc.text = data.desc

        holder.image.setImageResource(data.dashboardItemIcon)
    }

 //  total count of items in the list
    override fun getItemCount() = dataList.size
}