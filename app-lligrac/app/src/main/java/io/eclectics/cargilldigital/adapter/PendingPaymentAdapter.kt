package io.eclectics.cargilldigital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.databinding.AdapterPendingPaymentsBinding
import io.eclectics.cargilldigital.data.model.BuyerPendingPayment
import javax.inject.Inject

class PendingPaymentAdapter @Inject constructor(var context: Context, val clickListener: BuyerListListener, var data:List<BuyerPendingPayment>): RecyclerView.Adapter<PendingPaymentAdapter.ViewHolder>(),
    Filterable {
    private var filteredData: ArrayList<BuyerPendingPayment> = ArrayList(data)
    private var mData: ArrayList<BuyerPendingPayment> = ArrayList(data)
    private var searchFound = false
    private var query = ""
    /*var data = TransactionRepository.transactionsList.value
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun getItemCount(): Int = filteredData.size//data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context,clickListener, filteredData!![position])
        //holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /* filteredData.clear()
         mData.clear()
         filteredData.addAll(data)
         mData.addAll(data)*/
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterPendingPaymentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /*fun bind(){
            binding.btnTopUp.setOnClickListener {
                it.findNavController().navigate(R.id.nav_buyerTopUp)
            }*/
        fun bind(context: Context, clickListener: BuyerListListener, item: BuyerPendingPayment) { // a refactor of onBindViewHolder method


            binding.apply {
                tvFarmerName.text =  "${item.firstName} ${item.lastName}"
                tvReferenceNo.text = "Reference No: ${item.payementrefcode}"
                tvTotalAmount.text = item.fullAmount
                tvPaidAmount.text = item.amountPaid
                var balance = item.fullAmount.toInt() - item.amountPaid.toInt()
                tvbalance.text=  balance.toString()

              /*  tvBuyerName.text =  "${context.resources.getString(R.string.name)}: ${item.firstName} ${item.lastName}"
                tvStationName.text = "Section: ${item.sectionid}"
                tvBuyerId.text = "${context.resources.getString(R.string.mobile_number)}: ${item.phoneNumber}"//NetworkUtility().cashFormatter(item.amountValue!!)//"CFA ${item.amountValue}"
                tvAmount.text = "${context.resources.getString(R.string.float_balnce)}: ${NetworkUtility().cashFormatter(item.balanceOnRequest!!)}"

                btnTopUp.setOnClickListener{
                    clickListener.onClick(item,"topup")
                }*/
                itemView.setOnClickListener{clickListener.onClick(item,"topup")}
            }

        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterPendingPaymentsBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }
    /**
     * Handles clicks to the layout items
     */
    class BuyerListListener(val clickListener: (acc: BuyerPendingPayment, action: String) -> Unit) {
        fun onClick(acc: BuyerPendingPayment, action: String) = clickListener(acc, action)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString().trim()
                if (charString.isBlank()) {
                    searchFound = false
                    val filterResults = FilterResults()
                    filterResults.values = mData
                    return filterResults
                } else {
                    searchFound = true
                    query = charString.lowercase()
                    val filteredList: ArrayList<BuyerPendingPayment> =
                        ArrayList()
                    for (row in mData) {
                        if (row.farmerphonenumber.lowercase()
                                .contains(charString.lowercase())
                            ||row.payementrefcode.lowercase()
                                .contains(charString.lowercase())
                            ||row.paymentDescription!!.lowercase()
                                .contains(charString.lowercase())
                            ||row.fullAmount!!.lowercase()
                                .contains(charString.lowercase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    val filterResults = FilterResults()
                    filterResults.values = filteredList
                    return filterResults
                }
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                filteredData.clear()
                filteredData.addAll(filterResults.values as List<BuyerPendingPayment>)
                notifyDataSetChanged()
            }
        }
    }


}