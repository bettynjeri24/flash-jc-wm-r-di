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
import com.ekenya.rnd.tijara.utils.loadImageSrc
import kotlinx.android.synthetic.main.guarantors_item_list.view.*
import kotlinx.android.synthetic.main.loan_product_item_list.view.*
import kotlinx.android.synthetic.main.loan_product_item_list.view.Cl_loan_product
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class LoanGuarontedRequestAdapter(val context: Context,private val guarantor:List<NewLoanGuaranteData>,private val onClickListener: OnClickListener): RecyclerView.Adapter<LoanGuarontedRequestAdapter.LoanProductViewHolder>(

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


    inner  class LoanProductViewHolder(private val binding: LoanGuarantedRequestRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var name1=""
        var name2=""
        fun bind(item: NewLoanGuaranteData) {
            binding.tvLoantitle.text= camelCase(item.loanName)
            val splited: List<String> = item.name?.split("\\s".toRegex())
            if( splited.count() == 2){

                var posone=""
                var postwo=""
                val firstName=splited[0]
                name2=(firstName).toUpperCase(Locale.ENGLISH)
                val lastName=splited[1]
                name1=(lastName).toUpperCase(Locale.ENGLISH)
                //  posone=name1[0].toString().toUpperCase(Locale.ENGLISH)
                //  postwo=name2[0].toString().toUpperCase(Locale.ENGLISH)
                binding.textLimit.text=" $name2 $name1"
            }else if (splited.count()===3){
                val firstName=splited[0]
                val name2=(firstName).toUpperCase(Locale.ENGLISH)
                val lastName=splited[1]
                name1=(lastName).toUpperCase(Locale.ENGLISH)
                binding.textLimit.text=" $name2 $lastName"
            }else{
                val firstName=splited[0]
                val name2=(firstName).toUpperCase(Locale.ENGLISH)
                binding.textLimit.text=" $name2"
            }
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
