package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentLoanItemListBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.network.model.UserProfileList
import kotlinx.android.synthetic.main.fragment_loan_item_list.view.*

class ServiceLifestyleAdapter (val items:ArrayList<BillPaymentMerchant>): RecyclerView.Adapter<ServiceLifestyleAdapter.LifestyleItemViewHolder>() {
    private var binding: FragmentLoanItemListBinding?=null

    inner class LifestyleItemViewHolder(itemBinding: FragmentLoanItemListBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifestyleItemViewHolder {
        binding= FragmentLoanItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LifestyleItemViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: LifestyleItemViewHolder, position: Int) {
        val currentItems=items[position]
        holder.itemView.apply {
            binding?.viewLine?.setImageResource(currentItems.image)
            binding?.tvTitleHolder?.text=currentItems.name
            binding?.ivImageHolder?.setImageResource(currentItems.logo)

            /**clickListener to the notebody and title to navigate to update note screen**/
        }
        holder.itemView.Cl_loan.setOnClickListener {
            when(position){
                0 ->{
                    Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_buyAirtimeFragment)
                }
                1 ->{
                    Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_billersFragment)
                }
            }

        }

    }

}