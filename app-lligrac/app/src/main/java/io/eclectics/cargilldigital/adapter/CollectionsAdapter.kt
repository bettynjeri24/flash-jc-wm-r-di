package io.eclectics.cargilldigital.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.databinding.AdapterCollectionsBinding

import io.eclectics.cargill.model.FarmerTransaction
import io.eclectics.cargill.utils.NetworkUtility
import javax.inject.Inject

class CollectionsAdapter @Inject constructor(val clickListener: CollectionsListener,var data:List<FarmerTransaction>) : RecyclerView.Adapter<CollectionsAdapter.ViewHolder>() {

   /* var data = CollectionsRepository.collectionsList.value
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun getItemCount(): Int = 6//data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener, data!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: CollectionsListener, item: FarmerTransaction) { // a refactor of onBindViewHolder method
            binding.collectionHolder.setOnClickListener { clickListener.onClick(item) }
            binding.apply {
                tvInitial.text = item.transactionId//.substring(0,1
                tvName.text =  "${item.transactionDirection}"
                tvAmount.text = "${NetworkUtility().cashFormatter(item.amount!!)}"
                tvQuantity.text = ""
            }

        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterCollectionsBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

}

/**
 * Handles clicks to the layout items
 */
class CollectionsListener(val clickListener: (collection: FarmerTransaction) -> Unit) {
    fun onClick(collection: FarmerTransaction) = clickListener(collection)
}