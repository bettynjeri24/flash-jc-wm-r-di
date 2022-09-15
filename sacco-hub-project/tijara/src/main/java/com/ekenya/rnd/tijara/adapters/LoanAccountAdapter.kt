package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.databinding.LoanAccountListItemsBinding
import com.ekenya.rnd.tijara.network.model.ActivesLoan

import kotlinx.android.synthetic.main.loan_account_list_items.view.*

class LoanAccountAdapter (private val onClickListener: OnClickListener): ListAdapter<ActivesLoan, LoanAccountAdapter.LoanProductViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<ActivesLoan>() {
        override fun areItemsTheSame(oldItem: ActivesLoan, newItem: ActivesLoan): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ActivesLoan, newItem: ActivesLoan): Boolean {
            return oldItem.loanId == newItem.loanId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanProductViewHolder {
        return LoanProductViewHolder(LoanAccountListItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: LoanProductViewHolder, position: Int) {

        val loandDetails = getItem(position)
        holder.itemView.btn_view_summary.setOnClickListener {
            onClickListener.click(loandDetails)
        }
        holder.bind(loandDetails)
    }


    class LoanProductViewHolder(private val binding: LoanAccountListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loanTypes: ActivesLoan) {
            binding.loanProperties = loanTypes
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedSacco: ActivesLoan) -> Unit){
        fun click(selectedLoan: ActivesLoan)=clickListener(selectedLoan)
    }

}
