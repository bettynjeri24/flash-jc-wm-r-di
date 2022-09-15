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
import com.ekenya.rnd.tijara.databinding.NewSaccoListItemsBinding
import com.ekenya.rnd.tijara.databinding.SaccoListItemsBinding
import com.ekenya.rnd.tijara.network.model.NewSaccoData
import com.ekenya.rnd.tijara.network.model.SaccoList
import com.ekenya.rnd.tijara.network.model.local.NewSaccoDataEntity
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import kotlinx.android.synthetic.main.sacco_list_items.view.Stima
import timber.log.Timber


class NewSaccoListAdapter(private val onClickListener: OnClickListener, var context: Context,private val saccoList:List<NewSaccoDataEntity>): RecyclerView.Adapter<NewSaccoListAdapter.SaccoViewHolder>(

) {
    private var selectedPosition =0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaccoViewHolder {
        return SaccoViewHolder(NewSaccoListItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SaccoViewHolder, position: Int) {
        val saccoList =saccoList[position]
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

    class SaccoViewHolder(private val binding: NewSaccoListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(saccoList: NewSaccoDataEntity) {
            binding.Stima.text=saccoList.name
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedSacco: NewSaccoDataEntity) -> Unit){
        fun click(selectedSacco: NewSaccoDataEntity)=clickListener(selectedSacco)
    }

    override fun getItemCount(): Int {
        return saccoList.size
    }


}