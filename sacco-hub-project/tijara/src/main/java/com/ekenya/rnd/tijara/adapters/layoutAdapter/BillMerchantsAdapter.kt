package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.network.model.BillerData
import com.ekenya.rnd.tijara.utils.bindImage
import com.ekenya.rnd.tijara.utils.callbacks.BillersCallBack
import com.ekenya.rnd.tijara.utils.loadImageSrc
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import kotlinx.android.synthetic.main.merchants_items_lists.view.*
import java.util.*

class BillMerchantsAdapter(val context: Context?, val billerList: List<BillerData>,private val callBack:BillersCallBack):RecyclerView.Adapter<BillMerchantsAdapter.BillerViewHolder>(){

    inner class BillerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var name1=""
        var posone=""
        var postwo=""
        fun bindItems(billerItems: BillerData){
            itemView.tv_titleHolder.text = billerItems.name
            if (billerItems.logoUrl.isNullOrEmpty()) {
                itemView.initials.makeVisible()
                val splited: List<String> = billerItems.name?.split("\\s".toRegex())
                if( splited.count() == 2){
                    val firstName=splited[0]
                    val name2=(firstName).toUpperCase(Locale.ENGLISH)
                    val lastName=splited[1]
                    name1=(lastName).toUpperCase(Locale.ENGLISH)
                    posone=name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo=name2[0].toString().toUpperCase(Locale.ENGLISH)
                    itemView.initials.text=" $postwo $posone"
                }else if (splited.count()===3){
                    val firstName=splited[0]
                    val name2=(firstName).toUpperCase(Locale.ENGLISH)
                    val lastName=splited[1]
                    name1=(lastName).toUpperCase(Locale.ENGLISH)
                    posone=name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo=name2[0].toString().toUpperCase(Locale.ENGLISH)
                    itemView.initials.text=" $postwo $posone"
                }else{
                    val names=billerItems.name
                    posone=names[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo=names[0].toString().toUpperCase(Locale.ENGLISH)
                    itemView.initials.text=" $postwo $posone"
                }

            } else {
                bindImage(itemView.iv_zukuIcon,billerItems.logoUrl)
            }
           itemView.cardBiller.setOnClickListener {
               callBack.onItemSelected(billerItems)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.merchants_items_lists,parent,false)
        return BillerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return billerList.size
    }


    override fun onBindViewHolder(holder: BillerViewHolder, position: Int) {
        val book=billerList[position]
        holder.bindItems(book)


    }
}