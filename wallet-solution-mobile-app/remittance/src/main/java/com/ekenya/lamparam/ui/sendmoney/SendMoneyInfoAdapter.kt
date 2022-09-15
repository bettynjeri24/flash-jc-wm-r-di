package com.ekenya.lamparam.ui.sendmoney

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.R
import com.ekenya.lamparam.model.ModelSenderReceiverInfo
import com.ekenya.lamparam.ui.home.OnItemClickListener
import com.ekenya.lamparam.ui.receiveMoney.navOptions
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.layout_bill_list.view.*
import kotlinx.android.synthetic.main.layout_sender_receiver_info.view.*


class SendMoneyInfoAdapter (var context: Context, var senderReceiverItem:List<ModelSenderReceiverInfo>):
        RecyclerView.Adapter<SendMoneyInfoAdapter.SendMoneyVHolder> (){


    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mItemClicked = clickListener
    }

    inner class SendMoneyVHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var senderReceiverTitle = itemView.tv_sender_label
        var id = itemView.tv_id_val
        var phone = itemView.tv_phone_val
        var name = itemView.tv_name_val
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMoneyVHolder {
        navOptions = UtilityClass().getNavoptions()
        return SendMoneyVHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_sender_receiver_info,parent,false))
    }

    override fun onBindViewHolder(holder:SendMoneyVHolder,position: Int) {
        var item:ModelSenderReceiverInfo = senderReceiverItem[position]
        holder.senderReceiverTitle.text = item.title
        holder.id.text = item.idNo.toString()
        holder.phone.text = item.phone.toString()
        holder.name.text = item.name

        if(item.title.equals("Recipient"))
        {
            holder.senderReceiverTitle.setTextColor(ContextCompat.getColor(context,R.color.colorRecipient));

        }
        else
        {
            holder.senderReceiverTitle.setTextColor(ContextCompat.getColor(context,R.color.colorSender));
        }

    }

    private fun setOnCLickedListerner(v: View, pos: Int, item: ModelSenderReceiverInfo){
    }

    override fun getItemCount(): Int {
        return  senderReceiverItem.size
    }

    companion object{
        lateinit var mItemClicked: OnItemClickListener
    }
}