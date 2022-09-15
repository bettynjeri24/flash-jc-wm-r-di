package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.BillerCategoriesListBinding
import com.ekenya.rnd.tijara.network.model.BillerCatgData
import kotlinx.android.synthetic.main.biller_categories_list.view.*
import timber.log.Timber

class BillerCategoryAdapter(private val onClickListener: OnClickListener,val context: Context): ListAdapter<BillerCatgData, BillerCategoryAdapter.BillerViewHolder>(
    DiffCallBack
) {
    private var selectedPosition = 0
    object DiffCallBack : DiffUtil.ItemCallback<BillerCatgData>() {
        override fun areItemsTheSame(oldItem: BillerCatgData, newItem: BillerCatgData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BillerCatgData, newItem: BillerCatgData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillerViewHolder {
        return BillerViewHolder(BillerCategoriesListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BillerViewHolder, position: Int) {

        val billerItems = getItem(position)

        Timber.d("SELECTED POSITION $selectedPosition")
        if (selectedPosition==position){
            Timber.d("POSIIIIIIII $position")
            Timber.d("POSIIIII $selectedPosition")
           // holder.itemView.CL_BILLER.isSelected = true
            holder.itemView.biller_text.setTextColor(ContextCompat.getColor(holder.itemView.biller_text.context,R.color.black))
            holder.itemView.CL_biller_text_bg.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_hint_border)
        }else{
           // holder.itemView.CL_BILLER.isSelected = false
            holder.itemView.biller_text.setTextColor(ContextCompat.getColor(holder.itemView.biller_text.context,R.color.black))
            holder.itemView.CL_biller_text_bg.background = ContextCompat.getDrawable(context, R.drawable.bg_transparent_hint_border)
        }

        holder.itemView.CL_biller_text_bg.setOnClickListener {
            if (selectedPosition >= 0)
            notifyItemChanged(selectedPosition)
            onClickListener.click(billerItems)
            selectedPosition = holder.adapterPosition
            Timber.d("SELECTED POSITION $selectedPosition")
            notifyItemChanged(selectedPosition)
        }
        holder.bind(billerItems)

    }


    class BillerViewHolder(private val binding: BillerCategoriesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(biller: BillerCatgData) {
            binding.billerCat = biller
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedBill: BillerCatgData) -> Unit){
        fun click(selectedBill: BillerCatgData)=clickListener(selectedBill)
    }

}
