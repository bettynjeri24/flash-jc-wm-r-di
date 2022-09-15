package io.eclectics.cargilldigital.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.databinding.AdapterFarmersBinding
import io.eclectics.cargill.model.FarmerModelObj
import javax.inject.Inject

class FarmersAdapter @Inject constructor(
    val clickListener: FarmerListener,
    var data: List<FarmerModelObj>
) : RecyclerView.Adapter<FarmersAdapter.ViewHolder>(),
    Filterable {
    private var filteredData: ArrayList<FarmerModelObj> = ArrayList(data)
    private var mData: ArrayList<FarmerModelObj> = ArrayList(data)
    private var searchFound = false
    private var query = ""

    /*var data = FarmersRepository.getFarmerList().value
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun getItemCount(): Int = filteredData.size//data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener, filteredData!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterFarmersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            clickListener: FarmerListener,
            item: FarmerModelObj
        ) { // a refactor of onBindViewHolder method

            binding.apply {
                tvFarmer.text = "${item.firstName} ${item.lastName}"
                tvLocation.text = item.location
                tvCall.text = item.phoneNumber
            }

//            binding.tvCall.setOnClickListener {
//                val callIntent = Intent(Intent.ACTION_CALL)
//                callIntent.data = Uri.parse("tel:" + item.phoneNumber) //change the number.
//                binding.root.context.startActivity(callIntent)
//            }

            binding.tvMore.setOnClickListener {
                clickListener.onClick(item, "farmer_details")
            }

            binding.ivSms.setOnClickListener {
                val sendIntent = Intent(Intent.ACTION_VIEW)
                sendIntent.data = Uri.parse("sms:" + item.phoneNumber)
                sendIntent.putExtra("sms_body", "Do you have cocoa for sale?")
                binding.root.context.startActivity(sendIntent)
            }

            binding.btnFarmDetails.setOnClickListener {
                clickListener.onClick(
                    item,
                    "farm_details"
                )
            }
            binding.btnCollect.setOnClickListener { clickListener.onClick(item, "Pay Farmer") }

        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterFarmersBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
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
                    filterResults.values = mData
                    return filterResults
                } else {
                    searchFound = true
                    query = charString.lowercase()
                    val filteredList: ArrayList<FarmerModelObj> =
                        ArrayList()
                    for (row in mData) {
                        if (row.firstName!!.lowercase()
                                .contains(charString.lowercase())
                            || row.lastName!!.lowercase()
                                .contains(charString.lowercase())
                            || row.phoneNumber!!.lowercase()
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
                filteredData.addAll(filterResults.values as List<FarmerModelObj>)
                notifyDataSetChanged()
            }
        }
    }
}

/**
 * Handles clicks to the layout items
 */
class FarmerListener(val clickListener: (farmer: FarmerModelObj, action: String) -> Unit) {
    fun onClick(farmer: FarmerModelObj, action: String) = clickListener(farmer, action)
}
