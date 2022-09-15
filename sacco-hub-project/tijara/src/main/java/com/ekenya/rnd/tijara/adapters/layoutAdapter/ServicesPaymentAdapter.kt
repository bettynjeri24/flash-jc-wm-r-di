package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentLoanItemListBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import kotlinx.android.synthetic.main.fragment_loan_item_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class ServicesPaymentAdapter():
    RecyclerView.Adapter<ServicesPaymentAdapter.PaymentItemViewHolder>(),Filterable {
    private val items:ArrayList<BillPaymentMerchant> = arrayListOf()
    private val filteredItems:ArrayList<BillPaymentMerchant> = arrayListOf()
    private var binding: FragmentLoanItemListBinding?=null
    private var searchFound=false
    private var query:String=""

    inner class PaymentItemViewHolder(itemBinding: FragmentLoanItemListBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentItemViewHolder {
        binding= FragmentLoanItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PaymentItemViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    override fun onBindViewHolder(holder: PaymentItemViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val currentItems=filteredItems[holder.adapterPosition]
        holder.itemView.apply {
            binding?.viewLine?.setImageResource(currentItems.image)
            binding?.tvTitleHolder?.text=currentItems.name
            binding?.tvPlaceHolder?.text=currentItems.categoryName
            binding?.ivImageHolder?.setImageResource(currentItems.logo)
            if(position==0){
                binding?.tvPlaceHolder?.makeVisible()
            }else {
                val prevItems=filteredItems[holder.adapterPosition.minus(1)]
                if (prevItems.categoryName!=currentItems.categoryName){
                    binding?.tvPlaceHolder?.makeVisible()
                }else{
                    binding?.tvPlaceHolder?.makeGone()
                }
            }

            /**clickListener to the notebody and title to navigate to update note screen**/
        }
        holder.itemView.Cl_loan.setOnClickListener {
            if (Constants.isSacco==true) {
                when (position) {
                    0 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_shareStatusFragment)
                    }
                    1 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_sendMoneyOptionFragment)
                    }
                    2 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_makeDepositFragment)
                    }
                    3 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_buyAirtimeFragment)
                    }
                    4 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_billersFragment)
                    }
                    5-> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_statementAccountFragment)
                    }
                    6 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_statementAccountFragment)
                    }
                    7 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_loanDashBoardFragment)
                    }
                    8 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_serviceFragment_to_myAccountsFragment)
                    }

                }
            }else{
                when(position){
                    /* 0 ->{
                         Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_shareStatusFragment)
                     }*/
                    0 ->{
                        Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_sendMoneyOptionFragment)
                    }
                    1 ->{
                        Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_makeDepositFragment)
                    }
                    2->{
                        Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_buyAirtimeFragment)
                    }
                    3 ->{
                        Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_billersFragment)
                    }
                    4 ->{
                        Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_statementAccountFragment)
                    }
                    5->{
                        Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_statementAccountFragment)
                    }
                    6 ->{
                        Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_loanDashBoardFragment)
                    }
                    7 ->{
                        //  Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_loanRepaymentFragment)
                    }
                    8 ->{
                        Navigation.findNavController(it).navigate(R.id.action_serviceFragment_to_myAccountsFragment)
                    }

                }
            }

        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString().trim()
                if (charString.isBlank()) {
                    searchFound = false
                    val filterResults = FilterResults()
                    filterResults.values = items
                    return filterResults
                } else {
                    searchFound = true
                    query = charString.toLowerCase(Locale.getDefault())
                    val filteredList: ArrayList<BillPaymentMerchant> =
                        ArrayList()
                    for (row in items) {
                        if (row.name.toLowerCase(Locale.getDefault())
                                .contains(charString.toLowerCase(Locale.getDefault()))
                        ) {
                            filteredList.add(row)
                        }
                    }
                    Log.d("TAG","$filteredList")
                    val filterResults = FilterResults()
                    filterResults.values = filteredList
                    return filterResults
                }
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                filteredItems.clear()
                filteredItems.addAll(filterResults.values as List<BillPaymentMerchant>)
                notifyDataSetChanged()
            }
        }
    }
    fun swapData(mItems:ArrayList<BillPaymentMerchant>){
        filteredItems.clear()
        items.clear()
        filteredItems.addAll(mItems)
        items.addAll(mItems)
        notifyDataSetChanged()
    }

}