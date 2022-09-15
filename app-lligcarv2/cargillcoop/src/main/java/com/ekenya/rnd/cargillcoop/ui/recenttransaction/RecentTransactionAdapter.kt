package com.ekenya.rnd.cargillcoop.ui.recenttransaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillcoop.databinding.AdapterCoopDisbursmentBinding
import com.ekenya.rnd.common.data.db.entity.CargillUserTransactionData
import com.ekenya.rnd.common.utils.custom.cashFormatter
import com.ekenya.rnd.common.utils.custom.formatRequestDate

class RecentTransactionAdapter(
    private val modelList: List<CargillUserTransactionData>,
    private val listener: OnCoopTransactionItemListener
) : ListAdapter<CargillUserTransactionData, RecentTransactionAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = AdapterCoopDisbursmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = modelList[position]
        holder.binding.apply {
            tvTransaction.text = "${item.recipientName}"
            tvDescription.text = "Account: ${item.recipientPhoneNumber}"
            // val dateInString = "2014-10-05T15:23:01Z"
            tvDate.text =
                "${tvDate.resources.getString(com.ekenya.rnd.common.R.string.date_label)}: ${
                // item.datecreated
                formatRequestDate("${item.datecreated}Z")
                // formatRequestDate(item.datecreated)
                }"

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
                    val imgs = ContextCompat.getDrawable(
                        this.context,
                        com.ekenya.rnd.common.R.drawable.ic_arrow_down
                    )!!
                    setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                    imgs.setTint(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.reddish
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.reddish
                        )
                    )
                    text = "-${cashFormatter(item.amount!!)}"
                }
            } else {
                tvAmount.apply {
                    val imgs = ContextCompat.getDrawable(
                        this.context,
                        com.ekenya.rnd.common.R.drawable.ic_arrow_up
                    )!!
                    setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                    imgs.setTint(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.primary_green
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.primary_green
                        )
                    )
                    text = cashFormatter(item.amount!!)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    inner class ViewHolder(val binding: AdapterCoopDisbursmentBinding) :
        RecyclerView.ViewHolder(binding.root)
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<CargillUserTransactionData>() {
    override fun areItemsTheSame(
        oldItem: CargillUserTransactionData,
        newItem: CargillUserTransactionData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: CargillUserTransactionData,
        newItem: CargillUserTransactionData
    ): Boolean {
        return oldItem == newItem
    }
}

interface OnCoopTransactionItemListener {
    fun onItemClicked(view: View, model: CargillUserTransactionData)
}
