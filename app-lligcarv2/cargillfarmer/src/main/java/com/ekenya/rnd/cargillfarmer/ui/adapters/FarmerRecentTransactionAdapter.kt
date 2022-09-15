package com.ekenya.rnd.cargillfarmer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillfarmer.data.responses.FarmerLatestTransactionData
import com.ekenya.rnd.cargillfarmer.databinding.ItemAdapterMorerecentTransctionBinding
import com.ekenya.rnd.common.utils.custom.cashFormatter


/**
 * RECENT TRANSCTION
 */

class FarmerRecentTransactionAdapter(
    private val modelList: List<FarmerLatestTransactionData>,
    private val listener: OnFarmerRecentTransactionItemListener
) : ListAdapter<FarmerLatestTransactionData, FarmerRecentTransactionAdapter.RecentTransactionViewHolder>(
    DIFF_UTIL_RECENT
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentTransactionViewHolder {
        val binding = ItemAdapterMorerecentTransctionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentTransactionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecentTransactionViewHolder,
        position: Int
    ) {
        val item = modelList[position]
        holder.binding.apply {
            tvReceiverName.text = "${item.recipientName}"
            tvAccountNo.text = "Account: ${item.sendorPhoneNumber}"
            tvDescription.text = " ${item.reasons}"
            tvRequestdate.text = item.datecreated
            tvReferenceCode.text = item.farmForceReferenceCode

            if (item.status == true) {
                rootTransaction.isClickable = true
                rootTransaction.setOnClickListener {
                    listener.onItemClicked(it, item)
                }
            } else {
                rootTransaction.isClickable = false
            }

            if (item.status == true) {
                tvAmount.apply {
                    val imgs = ContextCompat.getDrawable(this.context, com.ekenya.rnd.common.R.drawable.ic_arrow_down)!!
                    setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                    imgs.setTint(ContextCompat.getColor(this.context, com.ekenya.rnd.common.R.color.reddish))
                    setTextColor(ContextCompat.getColor(this.context, com.ekenya.rnd.common.R.color.reddish))
                    text = "-${cashFormatter(item.amount!!)}"
                }
            } else {
                tvAmount.apply {
                    val imgs = ContextCompat.getDrawable(this.context, com.ekenya.rnd.common.R.drawable.ic_arrow_up)!!
                    setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                    imgs.setTint(ContextCompat.getColor(this.context, com.ekenya.rnd.common.R.color.primary_green))
                    setTextColor(ContextCompat.getColor(this.context, com.ekenya.rnd.common.R.color.primary_green))
                    text = cashFormatter(item.amount!!)
                }
            }

        }


    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    inner class RecentTransactionViewHolder(val binding: ItemAdapterMorerecentTransctionBinding) :
        RecyclerView.ViewHolder(binding.root)


}


private val DIFF_UTIL_RECENT = object : DiffUtil.ItemCallback<FarmerLatestTransactionData>() {
    override fun areItemsTheSame(
        oldItem: FarmerLatestTransactionData,
        newItem: FarmerLatestTransactionData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: FarmerLatestTransactionData,
        newItem: FarmerLatestTransactionData
    ): Boolean {
        return oldItem == newItem
    }

}

interface OnFarmerRecentTransactionItemListener {
    fun onItemClicked(view: View, model: FarmerLatestTransactionData)
}