package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.LoanDashboardItemsListBinding
import com.ekenya.rnd.tijara.databinding.PesaLinkRowBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.network.model.UserProfileList
import kotlinx.android.synthetic.main.loan_options_item_list.view.*
import kotlinx.android.synthetic.main.send_money_items_list.view.*

class LoanDashboardItemsAdapter (val onClickListener:OnClickListener,
                                 val items:ArrayList<BillPaymentMerchant>)
    : RecyclerView.Adapter<LoanDashboardItemsAdapter.LoanDashboardViewHolder>() {
    private var binding: PesaLinkRowBinding?=null


    inner class LoanDashboardViewHolder(itemBinding: PesaLinkRowBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanDashboardViewHolder {
        binding= PesaLinkRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoanDashboardViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: LoanDashboardViewHolder, position: Int) {
        val currentItems=items[position]
        holder.itemView.apply {
            binding?.viewLine?.setImageResource(currentItems.image)
            binding?.tvLoanOption?.text=currentItems.name
            binding?.imageView4?.setImageResource(currentItems.logo)

            /**clickListener to the notebody and title to navigate to update note screen**/
        }
        holder.itemView.cardLoan.setOnClickListener {
            when(position){
                0 ->{
                    onClickListener.click(currentItems)
                    it.findNavController().navigate(R.id.action_loanDashBoardFragment_to_loanProductFragment)
                }
                1 ->{
                    onClickListener.click(currentItems)
                    it.findNavController().navigate(R.id.action_loanDashBoardFragment_to_activeLoanFragment)

                }
                2 ->{
                    onClickListener.click(currentItems)
                    it.findNavController().navigate(R.id.action_loanDashBoardFragment_to_pendingLoanFragment)
                }

                3->{
                    onClickListener.click(currentItems)
                    it.findNavController().navigate(R.id.action_loanDashBoardFragment_to_myGuarantorsFragment)

                }
                4->{
                    onClickListener.click(currentItems)
                    it.findNavController().navigate(R.id.action_loanDashBoardFragment_to_loanGuarantedFragment)
                }
                5->{
                    onClickListener.click(currentItems)
                    it.findNavController().navigate(R.id.action_loanDashBoardFragment_to_loanStatementAccountFragment)
                }
                6->{
                    onClickListener.click(currentItems)
                    it.findNavController().navigate(R.id.action_loanDashBoardFragment_to_loanCalculatorFragment)
                }

            }

        }

    }

    class OnClickListener(val clickListener:(types:BillPaymentMerchant)->Unit){

        fun click(types: BillPaymentMerchant)=clickListener(types)



    }

}