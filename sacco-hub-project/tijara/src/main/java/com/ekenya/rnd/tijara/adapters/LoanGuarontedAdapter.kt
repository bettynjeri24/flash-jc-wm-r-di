package com.ekenya.rnd.tijara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.GuarantorsItemListBinding
import com.ekenya.rnd.tijara.databinding.LoanGuarantedRequestRowBinding
import com.ekenya.rnd.tijara.network.model.GuarantorData
import com.ekenya.rnd.tijara.network.model.NewLoanGuaranteData
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.camelCase
import kotlinx.android.synthetic.main.guarantors_item_list.view.*
import kotlinx.android.synthetic.main.loan_product_item_list.view.*
import kotlinx.android.synthetic.main.loan_product_item_list.view.Cl_loan_product
import java.math.RoundingMode
import java.text.DecimalFormat

class LoanGuarontedAdapter(private val guarantor:List<NewLoanGuaranteData>,private val onClickListener: OnClickListener,val context:Context):
    RecyclerView.Adapter<LoanGuarontedAdapter.LoanProductViewHolder>(

    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanProductViewHolder {
        return LoanProductViewHolder(LoanGuarantedRequestRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: LoanProductViewHolder, position: Int) {

        val items = guarantor[position]
        holder.bind(items)
        holder.itemView.ClGuarantors.setOnClickListener {
            onClickListener.click(items)
        }
    }


    inner class LoanProductViewHolder(private val binding: LoanGuarantedRequestRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewLoanGuaranteData) {
            binding.tvLoantitle.text= camelCase(item.loanName)
            binding.textLimit.text=item.name
            val texte = item.percentageRequested
            binding.tvPercentage.text="%.2f".format(texte)+"%"
            val amouyt= FormatDigit.formatDigits(item.amountRequested)
            binding.tvAmount.text= String.format(context.getString(R.string.kesh),amouyt)
            //  loadImageSrc( binding.ivLoan,item.im)


        }

    }
    class OnClickListener(val clickListener: (selectedItem: NewLoanGuaranteData) -> Unit){
        fun click(selectedItem: NewLoanGuaranteData)=clickListener(selectedItem)
    }

    override fun getItemCount(): Int {
        return  guarantor.size
    }

}
