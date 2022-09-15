package com.ekenya.rnd.tijara.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.SaccoListItemsBinding
import com.ekenya.rnd.tijara.network.model.SaccoList
import kotlinx.android.synthetic.main.sacco_list_items.view.Stima
import timber.log.Timber


class SaccoListAdapter(private val onClickListener: OnClickListener, var context: Context):ListAdapter<SaccoList, SaccoListAdapter.SaccoViewHolder>(
    DiffCallBack
) {
    private var selectedPosition =0

    object DiffCallBack : DiffUtil.ItemCallback<SaccoList>() {
        override fun areItemsTheSame(oldItem: SaccoList, newItem: SaccoList): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SaccoList, newItem: SaccoList): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaccoViewHolder {
        return SaccoViewHolder(SaccoListItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SaccoViewHolder, position: Int) {
        val saccoList = getItem(position)

        if (selectedPosition==position){
            Constants.SIGNUPORGID=saccoList.id.toString().trim()
            Timber.d("Sign Organizat Id: ${Constants.SIGNUPORGID}")

            holder.itemView.Stima.isSelected = true; //using selector drawable;
            // holder.itemView.Stima.background=R.color.buttonColor
            holder.itemView.Stima.setTextColor(
                ContextCompat.getColor(holder.itemView.Stima.context,
                    R.color.textColor))
            holder.itemView.Stima.background = ContextCompat.getDrawable(context, R.drawable.btn_less_radius)
            holder.itemView.Stima.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selected_background, 0, 0, 0)
        }else{
            holder.itemView.Stima.isSelected=false
            holder.itemView.Stima.setTextColor(
                ContextCompat.getColor(holder.itemView.Stima.context,
                    R.color.black))
            holder.itemView.Stima.background = ContextCompat.getDrawable(context, R.drawable.transparent_bg);
            holder.itemView.Stima.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_button_unselected, 0, 0, 0)
        }
        holder.itemView.Stima.setOnClickListener { v ->

            if (selectedPosition >= 0)notifyItemChanged(selectedPosition)
            onClickListener.click(saccoList)
            Constants.SELECTED_TYPE=0
            Timber.d("SELECTED 0 is ${Constants.SELECTED_TYPE}")
            selectedPosition = holder.adapterPosition
            Timber.d("SELECTED POSITION $selectedPosition")
            Timber.d("Sign Organiion Id: ${Constants.SIGNUPORGID}")
            Constants.SIGNUPORGID=saccoList.id.toString().trim()
            notifyItemChanged(selectedPosition)
        }

        holder.bind(saccoList)
    }

    class SaccoViewHolder(private val binding: SaccoListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(saccoList: SaccoList) {
            binding.saccoList = saccoList
           binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedSacco: SaccoList) -> Unit){
        fun click(selectedSacco: SaccoList)=clickListener(selectedSacco)
    }


}