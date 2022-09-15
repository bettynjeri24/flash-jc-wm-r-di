package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.GuarantorsItemListBinding
import com.ekenya.rnd.tijara.network.model.GuarantorData
import com.ekenya.rnd.tijara.utils.FormatDigit
import kotlinx.android.synthetic.main.guarantors_item_list.view.*
import kotlinx.android.synthetic.main.loan_product_detail_history_item_list.view.*
import kotlinx.android.synthetic.main.loan_product_item_list.view.*
import kotlinx.android.synthetic.main.loan_product_item_list.view.Cl_loan_product
import java.math.RoundingMode
import java.text.DecimalFormat

class MyGuarontorsAdapter(private val onClickListener: OnClickListener): ListAdapter<GuarantorData
        , MyGuarontorsAdapter.LoanProductViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<GuarantorData>() {
        override fun areItemsTheSame(oldItem: GuarantorData, newItem: GuarantorData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GuarantorData, newItem: GuarantorData): Boolean {
            return oldItem.guarantorId == newItem.guarantorId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanProductViewHolder {
        return LoanProductViewHolder(GuarantorsItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: LoanProductViewHolder, position: Int) {

        val items = getItem(position)
        holder.bind(items)

        holder.itemView.ClGuarantors.setOnClickListener {
            if (holder.itemView.cl_DetailsLoan.visibility== View.GONE) {
                holder.itemView.cl_DetailsLoan.visibility = View.VISIBLE
                holder.itemView.ImageBack.setImageDrawable(ContextCompat.getDrawable(it.context, R.drawable.ic_back_loans))
                var angle = 0f
                angle += 90f
                holder.itemView.ImageBack.animate().setDuration(300).rotation(angle).start()
            }else{
                holder.itemView.cl_DetailsLoan.visibility= View.GONE
                holder.itemView.ImageBack.setImageResource(R.drawable.ic_back_loans)
                var angle = 90f
                angle += -90f
                holder.itemView.ImageBack.animate().setDuration(300).rotation(angle).start()
            }
            onClickListener.click(items)
        }
    }


    class LoanProductViewHolder(private val binding: GuarantorsItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GuarantorData) {
            binding.guarantors = item
            val texte = item.percentageRequested
            binding.tvPercentage.text="%.2f".format(texte)+"%"
            binding.tvAmount.text= FormatDigit.formatDigits(item.amountRequested)
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedItem: GuarantorData) -> Unit){
        fun click(selectedItem: GuarantorData)=clickListener(selectedItem)
    }

}
