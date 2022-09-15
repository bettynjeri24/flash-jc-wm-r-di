package com.ekenya.rnd.tijara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.ActiveLoanItemListBinding
import com.ekenya.rnd.tijara.network.model.ActivesLoan
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.getInitials
import kotlinx.android.synthetic.main.loan_product_item_list.view.*

class PendingLoanAdapter(private val onClickListener: OnClickListener,val context:Context): ListAdapter<ActivesLoan,
        PendingLoanAdapter.LoanProductViewHolder>(
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
        return LoanProductViewHolder(ActiveLoanItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: LoanProductViewHolder, position: Int) {

        val loanItems = getItem(position)
        holder.bind(loanItems)
      /*  holder.itemView.Cl_loan_product.setOnClickListener {
            onClickListener.click(loanItems)
            Constants.LOANID=loanItems.loanId
            Constants.PRODUCTNAME=loanItems.name
            it.findNavController().navigate(R.id.action_activeLoanFragment_to_loanRepaymentFragment)
        }*/
    }


  inner  class LoanProductViewHolder(private val binding: ActiveLoanItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(activesLoan: ActivesLoan) {
            binding.activeLoans = activesLoan
            binding.initials.text = getInitials(activesLoan.name).uppercase()
            if (activesLoan.loanBalance.contains("KES")){
                val amount=activesLoan.loanBalance.replace("KES","")
                val finalamount= FormatDigit.formatDigits(amount)
                binding.tvLimit.text= String.format(context.getString(R.string.kesh),finalamount)
            }
            if (activesLoan.interestAmount.contains("KES")){
                val amount=activesLoan.interestAmount.replace("KES","")
                val finalamount= FormatDigit.formatDigits(amount)
                binding.tvPayPeriod.text= String.format(context.getString(R.string.kesh),finalamount)
            }
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (activesLoan: ActivesLoan) -> Unit){
        fun click(activesLoan: ActivesLoan)=clickListener(activesLoan)
    }

}
