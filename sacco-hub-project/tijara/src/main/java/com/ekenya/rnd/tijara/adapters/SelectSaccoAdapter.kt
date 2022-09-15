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
import com.ekenya.rnd.tijara.databinding.SelectSaccoItemListBinding
import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.utils.callbacks.SaccoDetailsCallBack
import kotlinx.android.synthetic.main.select_sacco_item_list.view.*
import timber.log.Timber


@Suppress("DEPRECATION")
class SelectSaccoAdapter(val context:Context,private val saccoCallBack: SaccoDetailsCallBack, private val saccoList:List<SaccoDetailEntity>): RecyclerView.Adapter<SelectSaccoAdapter.SaccoListViewHolder>(

) {
    private var selectedPosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaccoListViewHolder {
        return SaccoListViewHolder(SelectSaccoItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SaccoListViewHolder, position: Int) {
        val saccoListDetails = saccoList[position]
        holder.itemView.SacccoName.text=saccoListDetails.name
        Constants.SELECTED_TYPE=1
        Timber.d("SELECTED 1 is ${Constants.SELECTED_TYPE}")

        if (selectedPosition==position){
            saccoCallBack.onItemSelected(saccoListDetails)
            Constants.ORGID=saccoListDetails.orgId.toString().trim()
            Constants.USERNAME=saccoListDetails.username.trim()
            Constants.userFname=saccoListDetails.firstName.trim()
            Constants.SaccoName=saccoListDetails.name.trim()
            Constants.isSacco=saccoListDetails.isSacco
            holder.itemView.SacccoName.isSelected = true;
            holder.itemView.SacccoName.setTextColor(ContextCompat.getColor(holder.itemView.SacccoName.context,R.color.textColor))
            holder.itemView.SacccoName.background = ContextCompat.getDrawable(context, R.drawable.btn_less_radius)
            holder.itemView.SacccoName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selected_background, 0, 0, 0)
        }else{
            holder.itemView.SacccoName.isSelected=false
            holder.itemView.SacccoName.setTextColor(ContextCompat.getColor(holder.itemView.SacccoName.context,R.color.black))
            holder.itemView.SacccoName.background = ContextCompat.getDrawable(context, R.drawable.transparent_bg);
            holder.itemView.SacccoName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_button_unselected, 0, 0, 0)
        }
        holder.itemView.SacccoName.setOnClickListener { v ->
            saccoCallBack.onItemSelected(saccoListDetails)
            Constants.ORGID=saccoListDetails.orgId.toString().trim()
            Constants.USERNAME=saccoListDetails.username.trim()
            Constants.userFname=saccoListDetails.firstName.trim()
            Constants.SaccoName=saccoListDetails.name.trim()
            Constants.isSacco=saccoListDetails.isSacco
            Timber.d("Sign In Organization Id ${Constants.ORGID}")
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            saccoCallBack.onItemSelected(saccoListDetails)
            selectedPosition = holder.adapterPosition
            Timber.d("SELECTED POSITION $selectedPosition")
            notifyItemChanged(selectedPosition)
        }
        holder.bind(saccoListDetails)
    }


    class SaccoListViewHolder(private val binding: SelectSaccoItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(saccoTypes: SaccoDetailEntity) {
            binding.SacccoName.text = saccoTypes.name
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedSacco: SaccoDetailEntity) -> Unit){
        fun click(selectedSacco: SaccoDetailEntity)=clickListener(selectedSacco)
    }

    override fun getItemCount(): Int {
        return saccoList.size
    }
}
