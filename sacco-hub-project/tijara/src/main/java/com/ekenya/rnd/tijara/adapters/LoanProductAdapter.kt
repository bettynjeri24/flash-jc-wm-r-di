package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.LoanProductItemListBinding
import com.ekenya.rnd.tijara.network.model.LoanProduct
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.LoanProductCallBack
import com.ekenya.rnd.tijara.utils.bindImage
import com.ekenya.rnd.tijara.utils.makeVisible
import kotlinx.android.synthetic.main.loan_product_item_list.view.*
import java.util.*

class LoanProductAdapter(private val onClickListener: OnClickListener): ListAdapter<LoanProduct, LoanProductAdapter.LoanProductViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<LoanProduct>() {
        override fun areItemsTheSame(oldItem: LoanProduct, newItem: LoanProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LoanProduct, newItem: LoanProduct): Boolean {
            return oldItem.productId == newItem.productId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanProductViewHolder {
        return LoanProductViewHolder(LoanProductItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: LoanProductViewHolder, position: Int) {

        val loanItems = getItem(position)
        holder.bind(loanItems)
        holder.itemView.Cl_loan_product.setOnClickListener {
         // it.findNavController().navigate(R.id.action_loanProductFragment_to_loanOptionFragment)
            onClickListener.click(loanItems)
        }
    }


    class LoanProductViewHolder(private val binding: LoanProductItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var name1=""
        var posone=""
        var postwo=""
        fun bind(loanProduct: LoanProduct) {
            binding.loanProduct = loanProduct
            binding.executePendingBindings()
                if (loanProduct.imageUrl.isNullOrEmpty()) {
                    binding.initials.makeVisible()
                    val splited: List<String> = loanProduct.name?.split("\\s".toRegex())
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
                        val names = loanProduct.name
                        posone = names[0].toString().toUpperCase(Locale.ENGLISH)
                        postwo = names[0].toString().toUpperCase(Locale.ENGLISH)
                        binding.initials.text = " $postwo $posone"
                    }

                } else {
                    bindImage(binding.ivLoan, loanProduct.imageUrl)
                }



        }

    }
    class OnClickListener(val clickListener: (selectedLoan: LoanProduct) -> Unit){
        fun click(selectedLoan: LoanProduct)=clickListener(selectedLoan)
    }

}
