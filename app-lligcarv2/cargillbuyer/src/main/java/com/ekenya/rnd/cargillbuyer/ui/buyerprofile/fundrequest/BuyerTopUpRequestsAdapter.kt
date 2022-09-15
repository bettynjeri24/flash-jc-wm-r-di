package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.fundrequest // ktlint-disable filename

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.data.responses.BuyerTopUpRequestsData
import com.ekenya.rnd.cargillbuyer.databinding.AdapterBuyerFundsrequestBinding
import com.ekenya.rnd.common.utils.custom.cashFormatter

class BuyerTopUpRequestsAdatper(
    private val model: List<BuyerTopUpRequestsData>
) : ListAdapter<BuyerTopUpRequestsData, BuyerTopUpRequestsAdatper.ViewHolder>(
    DIFF_UTIL
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = AdapterBuyerFundsrequestBinding.inflate(
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
        /* bind card details */
        val item = model[position]
        holder.binding.apply {
            tvBuyerName.text = item.firstName
            tvDescription.text = "${item.reasons}"
            tvAmount.text =
                " ${cashFormatter(item.amountRequested!!.toString())}" // //"CFA ${item.amountValue}"
            // ${ context.resources.getString(R.string.due_amount)
            //                }
            tvRequestdate.text = item.dateRequested

            if (item.status!! && !item.isArchived!!) {
                tvApprovalStatus.apply {
                    text = context.resources.getString(
                        com.ekenya.rnd.common.R.string.approved
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.primary_green
                        )
                    )
                }
            } else if (item.isArchived == true && !item.status!!) {
                tvApprovalStatus.apply {
                    text = context.resources.getString(
                        com.ekenya.rnd.common.R.string.request_rejected
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.reddish
                        )
                    )
                }
            } else {
                tvApprovalStatus.apply {
                    text = context.resources.getString(
                        com.ekenya.rnd.common.R.string.awaits_approved
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.color_awaits_approval
                        )
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    inner class ViewHolder(val binding: AdapterBuyerFundsrequestBinding) :
        RecyclerView.ViewHolder(binding.root)
}

private val DIFF_UTIL =
    object : DiffUtil.ItemCallback<BuyerTopUpRequestsData>() {
        override fun areItemsTheSame(
            oldItem: BuyerTopUpRequestsData,
            newItem: BuyerTopUpRequestsData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BuyerTopUpRequestsData,
            newItem: BuyerTopUpRequestsData
        ): Boolean {
            return oldItem == newItem
        }
    }
