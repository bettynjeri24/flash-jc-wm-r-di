package com.ekenya.rnd.dashboard.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.datadashboard.model.StatementItem
import com.ekenya.rnd.onboarding.databinding.TransactionItemBinding

class TransactionItemsAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var transactionItems = mutableListOf<StatementItem>()

    fun sendTransactions(dashboardItems: List<StatementItem>) {
        this.transactionItems = dashboardItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = TransactionItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentTransactionItem = transactionItems.get(position)

        val narration = currentTransactionItem.narration
        if (!narration.isNullOrBlank()) {
            val initials = currentTransactionItem.narration
                .split(' ')
                .mapNotNull { it.firstOrNull()?.toString() }
                .reduce { acc, s -> acc + s }
            holder.binding.avatar.text = initials


        }



        holder.binding.username.text = currentTransactionItem.narration
        if (currentTransactionItem.dr_cr == "C") {
            holder.binding.tvTransactionAmount.setTextColor(Color.parseColor("#57cd13"));
            holder.binding.tvTransactionAmount.text =
                "+ GHS " + currentTransactionItem.amount.toString() + "0"
        } else {
            holder.binding.tvTransactionAmount.setTextColor(Color.parseColor("#cd1313"));

            holder.binding.tvTransactionAmount.text =
                "- GHS " + currentTransactionItem.amount.toString() + "0"
        }

        holder.binding.tvTransactionTime.text = currentTransactionItem.transaction_date
        holder.binding.tvTransactionRef.text =
            "REF: " + currentTransactionItem.receipt_number.toString()


    }

    override fun getItemCount(): Int {
        return transactionItems.size
    }
}

class ViewHolder(val binding: TransactionItemBinding) : RecyclerView.ViewHolder(binding.root) {

}