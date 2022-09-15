package com.ekenya.rnd.tijara.adapters.spinnerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.SharesReceivedItemListBinding
import com.ekenya.rnd.tijara.network.model.RequestsReceived
import kotlinx.android.synthetic.main.shares_received_item_list.view.*
import kotlinx.android.synthetic.main.shares_received_item_list.view.CLPending
import kotlinx.android.synthetic.main.shares_received_item_list.view.ClApproved
import kotlinx.android.synthetic.main.shares_received_item_list.view.text_status
import kotlinx.android.synthetic.main.shares_received_item_list.view.tv_name
import kotlinx.android.synthetic.main.shares_sent_item_list.view.*

class RequestReceivedAdapter(private val onClickListener: OnClickListener): ListAdapter<RequestsReceived,
        RequestReceivedAdapter.ShareRequestViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<RequestsReceived>() {
        override fun areItemsTheSame(oldItem: RequestsReceived, newItem: RequestsReceived): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RequestsReceived, newItem: RequestsReceived): Boolean {
            return oldItem.transactionCode == newItem.transactionCode
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareRequestViewHolder {
        return ShareRequestViewHolder(SharesReceivedItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ShareRequestViewHolder, position: Int) {

        val shareItems = getItem(position)
        if (shareItems.status == "Approved" || shareItems.status == "Accepted"){
            holder.itemView.text_status.setTextColor(
                ContextCompat.getColor(holder.itemView.text_status.context,
                    R.color.ForestGreen))
            holder.itemView.CLPending.visibility=View.GONE
            holder.itemView.ClApproved.visibility=View.VISIBLE
            holder.itemView.tv_name.text="Shares Transferred"
         }else if (shareItems.status=="Rejected"){
            holder.itemView.text_status.setTextColor(
                ContextCompat.getColor(holder.itemView.text_status.context,
                    R.color.textColor))
            holder.itemView.CLPending.visibility=View.GONE
            holder.itemView.ClApproved.visibility=View.VISIBLE
            holder.itemView.tv_name.text="Shares To Transfer"

         }else{
            holder.itemView.tv_name.text="Shares To Transfer"
            holder.itemView.CLPending.visibility=View.VISIBLE
            holder.itemView.ClApproved.visibility=View.GONE
            holder.itemView.text_status.setTextColor(
                ContextCompat.getColor(holder.itemView.text_status.context,
                    R.color.textColor))

         }

        holder.itemView.btnAccept.setOnClickListener {
            Constants.ACCEPTCODE=shareItems.transactionCode
            it.findNavController().navigate(R.id.action_shareStatusFragment_to_acceptTransferFragment)
            onClickListener.click(shareItems)
        }
        holder.itemView.btnMore.setOnClickListener {
            Constants.FROMSHARESENT=1
           Constants.DETAILCODE=shareItems.transactionCode
            Constants.DTRANSTYPE=shareItems.transactionType
            Constants.DSHARETRANS= shareItems.numberOfShares.toString()
            Constants.DNAME=shareItems.memberName
            Constants.DMEMNUMBER=shareItems.memberNumber
            Constants.DPHONENUMBER=shareItems.memberPhone
            Constants.DSTATUS=shareItems.status
            it.findNavController().navigate(R.id.action_shareStatusFragment_to_shareRequestDetailsFragment)
            onClickListener.click(shareItems)
        }
        holder.itemView.btnAPPDetail.setOnClickListener {
            Constants.FROMSHARESENT=1
           Constants.DETAILCODE=shareItems.transactionCode
            Constants.DTRANSTYPE=shareItems.transactionType
            Constants.DSHARETRANS= shareItems.numberOfShares.toString()
            Constants.DNAME=shareItems.memberName
            Constants.DMEMNUMBER=shareItems.memberNumber
            Constants.DPHONENUMBER=shareItems.memberPhone
            Constants.DSTATUS=shareItems.status
            it.findNavController().navigate(R.id.action_shareStatusFragment_to_shareRequestDetailsFragment)
            onClickListener.click(shareItems)
        }
        holder.bind(shareItems)
    }


    class ShareRequestViewHolder(private val binding: SharesReceivedItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shareItems: RequestsReceived) {
            binding.shareReceived = shareItems
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (shareItems: RequestsReceived) -> Unit){
        fun click(shareItems: RequestsReceived)=clickListener(shareItems)
    }

}
