package com.ekenya.rnd.tijara.adapters

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
import com.ekenya.rnd.tijara.databinding.SharesSentItemListBinding
import com.ekenya.rnd.tijara.network.model.RequestsReceived
import com.ekenya.rnd.tijara.network.model.RequestsSent
import kotlinx.android.synthetic.main.shares_received_item_list.view.*
import kotlinx.android.synthetic.main.shares_received_item_list.view.CLPending
import kotlinx.android.synthetic.main.shares_received_item_list.view.ClApproved
import kotlinx.android.synthetic.main.shares_received_item_list.view.text_status
import kotlinx.android.synthetic.main.shares_received_item_list.view.tv_name
import kotlinx.android.synthetic.main.shares_sent_item_list.view.*

class RequestSentAdapter(private val onClickListener: OnClickListener): ListAdapter<RequestsSent,
        RequestSentAdapter.ShareRequestViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<RequestsSent>() {
        override fun areItemsTheSame(oldItem: RequestsSent, newItem: RequestsSent): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RequestsSent, newItem: RequestsSent): Boolean {
            return oldItem.transactionCode == newItem.transactionCode
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareRequestViewHolder {
        return ShareRequestViewHolder(SharesSentItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ShareRequestViewHolder, position: Int) {

        val shareItems = getItem(position)
        if (shareItems.status == "Approved"){
            holder.itemView.text_status.setTextColor(
                ContextCompat.getColor(holder.itemView.text_status.context,
                    R.color.ForestGreen))
            holder.itemView.CLPending.visibility=View.GONE
            holder.itemView.ClApproved.visibility=View.VISIBLE
            holder.itemView.tv_name.text="Shares Transferred"
         }else if (shareItems.status == "Cancelled"){
            holder.itemView.text_status.setTextColor(
                ContextCompat.getColor(holder.itemView.text_status.context,
                    R.color.textColor))
            holder.itemView.CLPending.visibility=View.GONE
            holder.itemView.ClApproved.visibility=View.VISIBLE
            holder.itemView.tv_name.text="Shares Transferred"
        }else{
            holder.itemView.tv_name.text="Shares To Transfer"
            holder.itemView.CLPending.visibility=View.VISIBLE
            holder.itemView.ClApproved.visibility=View.GONE
            holder.itemView.text_status.setTextColor(
                ContextCompat.getColor(holder.itemView.text_status.context,
                    R.color.textColor))
  }

        holder.itemView.btnCancel.setOnClickListener {
            Constants.CANCELCODE=shareItems.transactionCode
            Constants.TOREJECT=1
            it.findNavController().navigate(R.id.action_shareStatusFragment_to_cancelTransferFragment)
            onClickListener.click(shareItems)
        }
        holder.itemView.btnSMore.setOnClickListener {
            Constants.FROMSHARESENT=0
           // Constants.DETAILCODE=shareItems.transactionCode
            Constants.DSentTRANSTYPE=shareItems.transactionType
            Constants.DSentSHARETRANS= shareItems.numberOfShares.toString()
            Constants.DSentNAME=shareItems.memberName
            Constants.DSentMEMNUMBER=shareItems.memberNumber
            Constants.DSentPHONENUMBER=shareItems.memberPhone
            Constants.DSentSTATUS=shareItems.status
            it.findNavController().navigate(R.id.action_shareStatusFragment_to_shareRequestDetailsFragment)
            onClickListener.click(shareItems)
        }
        holder.itemView.btnAPPDetails.setOnClickListener {
            Constants.FROMSHARESENT=0
            // Constants.DETAILCODE=shareItems.transactionCode
            Constants.DSentTRANSTYPE=shareItems.transactionType
            Constants.DSentSHARETRANS= shareItems.numberOfShares.toString()
            Constants.DSentNAME=shareItems.memberName
            Constants.DSentMEMNUMBER=shareItems.memberNumber
            Constants.DSentPHONENUMBER=shareItems.memberPhone
            Constants.DSentSTATUS=shareItems.status
            it.findNavController().navigate(R.id.action_shareStatusFragment_to_shareRequestDetailsFragment)
            onClickListener.click(shareItems)
        }
        holder.bind(shareItems)
        /*holder.itemView.btnApply.setOnClickListener {
            it.findNavController().navigate(R.id.action_loanProductFragment_to_loanOptionFragment)
            onClickListener.click(shareItems)
        }*/
    }


    class ShareRequestViewHolder(private val binding: SharesSentItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shareItems: RequestsSent) {
            binding.shareSent = shareItems
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (shareItems: RequestsSent) -> Unit){
        fun click(shareItems: RequestsSent)=clickListener(shareItems)
    }

}
