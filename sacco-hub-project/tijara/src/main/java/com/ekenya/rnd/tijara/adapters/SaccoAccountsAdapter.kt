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
import com.ekenya.rnd.tijara.databinding.SelectSaccoItemListBinding
import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.ekenya.rnd.tijara.network.model.SaccoList
import kotlinx.android.synthetic.main.select_sacco_item_list.view.*
import timber.log.Timber


class SaccoAccountsAdapter(private val onClickListener: OnClickListener, var context: Context):ListAdapter<SaccoDetail, SaccoAccountsAdapter.SaccoViewHolder>(
    DiffCallBack
) {
    private var selectedPosition =0

    object DiffCallBack : DiffUtil.ItemCallback<SaccoDetail>() {
        override fun areItemsTheSame(oldItem: SaccoDetail, newItem: SaccoDetail): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SaccoDetail, newItem: SaccoDetail): Boolean {
            return oldItem.orgId == newItem.orgId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaccoViewHolder {
        return SaccoViewHolder(SelectSaccoItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SaccoViewHolder, position: Int) {
        val saccoList = getItem(position)

        if (selectedPosition==position){
            Constants.SIGNUPORGID=saccoList.orgId.toString().trim()
            Timber.d("Sign Organizat Id: ${Constants.SIGNUPORGID}")

            holder.itemView.SacccoName.isSelected = true; //using selector drawable;
            // holder.itemView.Stima.background=R.color.buttonColor
            holder.itemView.SacccoName.setTextColor(
                ContextCompat.getColor(holder.itemView.SacccoName.context,
                    R.color.textColor))
            holder.itemView.SacccoName.background = ContextCompat.getDrawable(context, R.drawable.btn_less_radius)
            holder.itemView.SacccoName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selected_background, 0, 0, 0)
        }else{
            holder.itemView.SacccoName.isSelected=false
            holder.itemView.SacccoName.setTextColor(
                ContextCompat.getColor(holder.itemView.SacccoName.context,
                    R.color.black))
            holder.itemView.SacccoName.background = ContextCompat.getDrawable(context, R.drawable.transparent_bg);
            holder.itemView.SacccoName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_button_unselected, 0, 0, 0)
        }
        holder.itemView.SacccoName.setOnClickListener { v ->

            if (selectedPosition >= 0)notifyItemChanged(selectedPosition)
            onClickListener.click(saccoList)
            Constants.SELECTED_TYPE=0
            Timber.d("SELECTED 0 is ${Constants.SELECTED_TYPE}")
            selectedPosition = holder.adapterPosition
            Timber.d("SELECTED POSITION $selectedPosition")
            Timber.d("Sign Organiion Id: ${Constants.SIGNUPORGID}")
            Constants.SIGNUPORGID=saccoList.orgId.toString().trim()
            notifyItemChanged(selectedPosition)
        }

        holder.bind(saccoList)
    }

    class SaccoViewHolder(private val binding: SelectSaccoItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(saccoList: SaccoDetail) {
            binding.saccodata = saccoList
           binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedSacco: SaccoDetail) -> Unit){
        fun click(selectedSacco: SaccoDetail)=clickListener(selectedSacco)
    }


}