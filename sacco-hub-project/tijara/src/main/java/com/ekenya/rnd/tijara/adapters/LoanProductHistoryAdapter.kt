package com.ekenya.rnd.tijara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.LoanProductDetailHistoryItemListBinding
import com.ekenya.rnd.tijara.network.model.LoanDataHistory
import com.ekenya.rnd.tijara.utils.FormatDigit.Companion.formatDigits
import com.ekenya.rnd.tijara.utils.formatDateName
import kotlinx.android.synthetic.main.loan_product_detail_history_item_list.view.*


class LoanProductHistoryAdapter(private val context: Context,private val onClickListener: OnClickListener): ListAdapter<LoanDataHistory, LoanProductHistoryAdapter.LoanProductHistoryViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<LoanDataHistory>() {
        override fun areItemsTheSame(oldItem: LoanDataHistory, newItem: LoanDataHistory): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LoanDataHistory, newItem: LoanDataHistory): Boolean {
            return oldItem.loanId == newItem.loanId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanProductHistoryViewHolder {
        return LoanProductHistoryViewHolder(
            LoanProductDetailHistoryItemListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: LoanProductHistoryViewHolder, position: Int) {

        val loanItems = getItem(position)
        holder.itemView.cl_card.setOnClickListener {
            if (holder.itemView.cl_history.visibility== View.GONE) {
                holder.itemView.cl_history.visibility = View.VISIBLE
                holder.itemView.tv_amont.setTextColor(ContextCompat.getColor(it.context, R.color.ForestGreen))
                holder.itemView.tv_status.setTextColor(ContextCompat.getColor(it.context, R.color.ForestGreen))
                holder.itemView.imageView4.setImageDrawable(ContextCompat.getDrawable(it.context, R.drawable.bank_arrow))
                var angle = 0f
                angle += 90f
                holder.itemView.imageView4.animate().setDuration(300).rotation(angle).start()
                holder.itemView.imageView3.setImageResource(R.drawable.ic_loan_history_active)

            }else {
                holder.itemView.cl_history.visibility = View.GONE
                holder.itemView.tv_amont.setTextColor(ContextCompat.getColor(it.context, R.color.textColor))
                holder.itemView.imageView3.setImageResource(R.drawable.ic_history_icon)
                holder.itemView.imageView4.setImageResource(R.drawable.bank_arrow)
                var angle = 90f
                angle += -90f
                holder.itemView.imageView4.animate().setDuration(300).rotation(angle).start()
            }
        }
        holder.bind(loanItems)
    }


    inner class LoanProductHistoryViewHolder(private val binding: LoanProductDetailHistoryItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loanHistory: LoanDataHistory) {
            binding.loanHistory = loanHistory
           // binding.tvDate.formatDateName(loanHistory.applicationDate)
            binding.tvDate.text=(loanHistory.applicationDate)
            binding.tvDisburedDate.text=(loanHistory.disbursementDate)
            binding.tvInterest.text= formatDigits(loanHistory.amountApplied)
            binding.tvAmont.text= formatDigits(loanHistory.loanBalance)
            binding.tvAmountPaid.text= formatDigits(loanHistory.amountRepaid)
            binding.tvAmountDisbured.text= formatDigits(loanHistory.amountDisbursed)
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedLoan: LoanDataHistory) -> Unit){
        fun click(selectedLoan: LoanDataHistory)=clickListener(selectedLoan)
    }

}
