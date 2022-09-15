package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.LoanOptionsItemListBinding
import com.ekenya.rnd.tijara.network.model.UserProfileList
import kotlinx.android.synthetic.main.loan_options_item_list.view.*

class NoLoanAdapter (val items:ArrayList<UserProfileList>): RecyclerView.Adapter<NoLoanAdapter.LoanOptionViewHolder>() {
    private var binding: LoanOptionsItemListBinding?=null

    inner class LoanOptionViewHolder(itemBinding: LoanOptionsItemListBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanOptionViewHolder {
        binding= LoanOptionsItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoanOptionViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: LoanOptionViewHolder, position: Int) {
        val currentItems=items[position]
        holder.itemView.apply {
            binding?.tvLoanOption?.text=currentItems.name
            binding?.imageView4?.setImageResource(currentItems.logo)

            /**clickListener to the notebody and title to navigate to update note screen**/
        }
        holder.itemView.cardLoan.setOnClickListener {
            when(position){
                0 ->{
                   it.findNavController().navigate(R.id.action_loanOptionFragment_to_loanRequestFragment)

                }
                1 ->{
                    it.findNavController().navigate(R.id.action_loanOptionFragment_to_loanHistoryBottomSheetFragment)
                }
            }

        }

    }

}