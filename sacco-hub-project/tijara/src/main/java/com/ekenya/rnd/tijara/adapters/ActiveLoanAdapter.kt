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
import com.ekenya.rnd.tijara.utils.bindImage
import com.ekenya.rnd.tijara.utils.makeVisible
import kotlinx.android.synthetic.main.loan_product_item_list.view.*
import java.util.*

class ActiveLoanAdapter(private val onClickListener: OnClickListener,val context:Context): ListAdapter<ActivesLoan,
        ActiveLoanAdapter.LoanProductViewHolder>(
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
        holder.itemView.Cl_loan_product.setOnClickListener {
            onClickListener.click(loanItems)
            Constants.LOANID=loanItems.loanId
            Constants.PRODUCTNAME=loanItems.name
            it.findNavController().navigate(R.id.action_activeLoanFragment_to_loanRepaymentFragment)
        }
    }


    inner class LoanProductViewHolder(private val binding: ActiveLoanItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var name1=""
        var posone=""
        var postwo=""
        fun bind(activesLoan: ActivesLoan) {
            if (activesLoan.imageUrl.isNullOrEmpty()) {
                binding.initials.makeVisible()
                val splited: List<String> = activesLoan.name?.split("\\s".toRegex())
                if (splited.count() == 2) {
                    val firstName = splited[0]
                    val name2 = (firstName).toUpperCase(Locale.ENGLISH)
                    val lastName = splited[1]
                    name1 = (lastName).toUpperCase(Locale.ENGLISH)
                    posone = name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = name2[0].toString().toUpperCase(Locale.ENGLISH)
                    binding.initials.text = " $postwo $posone"
                } else if (splited.count() === 3) {
                    val firstName = splited[0]
                    val name2 = (firstName).toUpperCase(Locale.ENGLISH)
                    val lastName = splited[1]
                    name1 = (lastName).toUpperCase(Locale.ENGLISH)
                    posone = name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = name2[0].toString().toUpperCase(Locale.ENGLISH)
                    binding.initials.text = " $postwo $posone"
                } else {
                    val names = activesLoan.name
                    posone = names[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = names[0].toString().toUpperCase(Locale.ENGLISH)
                    binding.initials.text = " $postwo $posone"
                }

            } else {
                bindImage(binding.ivLoan, activesLoan.imageUrl)
            }
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
            binding.activeLoans = activesLoan
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (activesLoan: ActivesLoan) -> Unit){
        fun click(activesLoan: ActivesLoan)=clickListener(activesLoan)
    }

}
