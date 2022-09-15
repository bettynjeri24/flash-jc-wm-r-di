package io.eclectics.cargilldigital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.AppCargillDigital
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.AdapterBuyerFundsrequestBinding
import io.eclectics.cargilldigital.data.model.CoopFundsRequestList
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import javax.inject.Inject

class BuyerDashboardFundsAdapter @Inject constructor(var context:Context,val clickListener: FundsListListener, var data:List<CoopFundsRequestList>): RecyclerView.Adapter<BuyerDashboardFundsAdapter.ViewHolder>(),
    Filterable {
    private var filteredData: ArrayList<CoopFundsRequestList> = ArrayList(data)
    private var mData: ArrayList<CoopFundsRequestList> = ArrayList(data)
    private var searchFound = false
    private var query = ""
    /*var data = TransactionRepository.transactionsList.value
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun getItemCount(): Int = filteredData!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context,clickListener, filteredData!![position])
        //holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*filteredData.clear()
        mData.clear()
        filteredData.addAll(data)
        mData.addAll(data)*/
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterBuyerFundsrequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /*fun bind(){
            binding.btnApprove.setOnClickListener {
                it.findNavController().navigate(R.id.nav_coopApproveFundsRequest)
            }*/
        fun bind(context: Context, clickListener: FundsListListener, item: CoopFundsRequestList) { // a refactor of onBindViewHolder method


            binding.apply {
                tvBuyerName.text =  "${ context.resources.getString(R.string.mobile_number)
                }: ${item.phonenumber} "
                tvDescription.text = "${item.reasons}"
                tvAmount.text = " ${NetworkUtility().cashFormatter(item.amountRequested!!.toString())}"////"CFA ${item.amountValue}"
                //${ context.resources.getString(R.string.due_amount)
                //                }
                tvRequestdate.text = item.dateOfRequest

                /*binding.btnApprove.setOnClickListener {
                    clickListener.onClick(item,"funds")
                }*/
                if(item.servicedStatus!!&&!item.isArchived){
                    binding.btnApprove.apply {
                        text = context.resources.getString(
                            R.string.approved
                        )
                        setTextColor(
                            AppCargillDigital.applicationContext().resources.getColor(
                                R.color.primary_green))
                        //setStrokeColorResource(AppCargillDigital.applicationContext().resources.getColorStateList(R.color.white_gray))
                        //strokeColor = resources.getColorStateList(R.color.white_gray)
                    }

                }

                else if (item.isArchived && !item.servicedStatus!!){
                    binding.btnApprove.apply {
                        text = context.resources.getString(
                            R.string.request_rejected
                        )
                        setTextColor(
                            AppCargillDigital.applicationContext().resources.getColor(
                                R.color.reddish))
                        //setStrokeColorResource(AppCargillDigital.applicationContext().resources.getColorStateList(R.color.white_gray))
                        //strokeColor = resources.getColorStateList(R.color.white_gray)
                    }

                }else {
                    binding.btnApprove.apply {
                        text = context.resources.getString(
                            R.string.awaits_approved
                        )
                        setTextColor(
                            AppCargillDigital.applicationContext().resources.getColor(
                                R.color.color_awaits_approval))
                        //setStrokeColorResource(AppCargillDigital.applicationContext().resources.getColorStateList(R.color.white_gray))
                        //strokeColor = resources.getColorStateList(R.color.white_gray)
                    }
                }
            }

//            if (item.transType == "credit"){
//                binding.tvAmount.setTextColor(R.color.accent2)
//                binding.tvAmount.setCompoundDrawables(R.drawable.ic_arrow_up,0,0,0)
//                binding.tvAmount.compoundDrawableTintMode = R.color.accent2
//            }
        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterBuyerFundsrequestBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }


    }
    /**
     * Handles clicks to the layout items
     */
    class FundsListListener(val clickListener: (acc: CoopFundsRequestList, action: String) -> Unit) {
        fun onClick(acc: CoopFundsRequestList, action: String) = clickListener(acc, action)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString().trim()
                if (charString.isBlank()) {
                    LoggerHelper.loggerError("searchstring","search empty string")
                    searchFound = false
                    val filterResults = FilterResults()
                    filterResults.values = mData
                    return filterResults
                } else {
                    searchFound = true
                    query = charString.lowercase()
                    LoggerHelper.loggerError("searchstring","search found here string")
                    val filteredList: ArrayList<CoopFundsRequestList> =
                        ArrayList()
                    for (row in mData) {
                        if (row.firstName.lowercase()
                                .contains(charString.lowercase())
                            ||row.amountRequested.toString().lowercase()
                                .contains(charString.lowercase())
                            ||row.lastName.toString().lowercase()
                                .contains(charString.lowercase())

                        )

                        {
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
                filteredData.addAll(filterResults.values as List<CoopFundsRequestList>)
                notifyDataSetChanged()
            }
        }
    }



}