package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.PesaLinkRowBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import kotlinx.android.synthetic.main.loan_options_item_list.view.*

class PesaLinkAdapter (val items:ArrayList<BillPaymentMerchant>): RecyclerView.Adapter<PesaLinkAdapter.PesaLinkViewHolder>() {
    private var binding: PesaLinkRowBinding?=null

    inner class PesaLinkViewHolder(itemBinding: PesaLinkRowBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesaLinkViewHolder {
        binding= PesaLinkRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PesaLinkViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: PesaLinkViewHolder, position: Int) {
        val currentItems=items[position]
        holder.itemView.apply {
            binding?.viewLine?.setImageResource(currentItems.image)
            binding?.tvLoanOption?.text=currentItems.name
            binding?.imageView4?.setImageResource(currentItems.logo)


        }
        holder.itemView.cardLoan.setOnClickListener {
            when(position){
                0 ->{
                    Navigation.findNavController(it).navigate(R.id.pesalinkPhoneCheckFragment)
                }
                1 ->{
                    Navigation.findNavController(it).navigate(R.id.action_pesaLinkFragment_to_sendToAccNumberFragment)
                }
            }

        }

    }

}