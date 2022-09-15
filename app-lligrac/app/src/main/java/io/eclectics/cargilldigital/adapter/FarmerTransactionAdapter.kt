package io.eclectics.cargilldigital.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.databinding.AdapterTransactionsBinding
import io.eclectics.cargill.model.FarmerTransaction

class FarmerTransactionAdapter : RecyclerView.Adapter<FarmerTransactionAdapter.ViewHolder>() {

    /*var data = TransactionRepository.transactionsList.value
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(clickListener, data!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: TransactionListener, item: FarmerTransaction) { // a refactor of onBindViewHolder method

           /* binding.apply {
                tvTransaction.text =  "Disbusment: ${item.agentId}"
                tvDescription.text = "Farm No: ${item.farmerNumber},${item.kgsAccepted} Kgs"
                tvAmount.text = NetworkUtility().cashFormatter(item.amountValue!!)//"CFA ${item.amountValue}"
                tvDate.text = item.syncDate
            }*/

//            if (item.transType == "credit"){
//                binding.tvAmount.setTextColor(R.color.accent2)
//                binding.tvAmount.setCompoundDrawables(R.drawable.ic_arrow_up,0,0,0)
//                binding.tvAmount.compoundDrawableTintMode = R.color.accent2
//            }
        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterTransactionsBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

}
