package com.ekenya.rnd.tijara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.LoanSummaryListItemsBinding
import com.ekenya.rnd.tijara.network.model.LoanStatement
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.formatDateName

class LoanSummaryAdapter(private val stateList:List<LoanStatement>,val context: Context): RecyclerView.Adapter<LoanSummaryAdapter.LoanSummaryViewHolder>(

) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanSummaryViewHolder {
        return LoanSummaryViewHolder(LoanSummaryListItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: LoanSummaryViewHolder, position: Int) {

        val accountDetails = stateList[position]
        holder.bind(accountDetails)
    }


   inner class LoanSummaryViewHolder(private val binding: LoanSummaryListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(accTypes: LoanStatement) {
            binding.tType.text = accTypes.notes
            binding.tAmount.text= String.format(context.getString(R.string.kesh),FormatDigit.formatDigits(accTypes.credit))
            binding.tDate.formatDateName(accTypes.transactionDate)
            binding.executePendingBindings()

        }

    }

    override fun getItemCount(): Int {
       return stateList.size
    }

}
