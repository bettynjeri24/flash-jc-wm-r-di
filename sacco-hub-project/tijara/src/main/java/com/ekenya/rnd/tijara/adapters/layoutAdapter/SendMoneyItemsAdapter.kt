package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.*
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_dashboard_items_list.view.*
import kotlinx.android.synthetic.main.fragment_dashboard_items_list.view.cl_dashData
import kotlinx.android.synthetic.main.loan_options_item_list.view.*
import kotlinx.android.synthetic.main.send_money_items_list.view.*

class SendMoneyItemsAdapter (val onClickListener:OnClickListener,val items:ArrayList<BillPaymentMerchant>): RecyclerView.Adapter<SendMoneyItemsAdapter.SendMItemViewHolder>() {
    private var binding: PesaLinkRowBinding?=null


    inner class SendMItemViewHolder(itemBinding: PesaLinkRowBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMItemViewHolder {
        binding= PesaLinkRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SendMItemViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: SendMItemViewHolder, position: Int) {
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
                    it.findNavController().navigate(R.id.action_sendMoneyOptionFragment_to_mobileMoneyTFragment)
                }
                1 ->{
                    onClickListener.click(currentItems)
                    Navigation.findNavController(it).navigate(R.id.action_sendMoneyOptionFragment_to_pesaLinkFragment)
                }
                2 ->{
                    onClickListener.click(currentItems)
                    Navigation.findNavController(it).navigate(R.id.action_sendMoneyOptionFragment_to_internalTransferFragment)

                }

            }

        }

    }

    class OnClickListener(val clickListener:(types:BillPaymentMerchant)->Unit){

        fun click(types: BillPaymentMerchant)=clickListener(types)



    }

}