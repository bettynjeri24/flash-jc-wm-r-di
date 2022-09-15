package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.databinding.KinsItemListBinding
import com.ekenya.rnd.tijara.network.model.NextOfKin
import com.ekenya.rnd.tijara.network.model.ViewNextKinList
import kotlinx.android.synthetic.main.kins_item_list.view.*
import kotlinx.android.synthetic.main.view_bank_info_item_lists.view.*
import kotlinx.android.synthetic.main.view_bank_info_item_lists.view.Cl_Bank
import java.util.*

class KinsListAdapter (private val onClickListener: OnClickListener): ListAdapter<NextOfKin, KinsListAdapter.KinsViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<NextOfKin>() {
        override fun areItemsTheSame(oldItem: NextOfKin, newItem: NextOfKin): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NextOfKin, newItem: NextOfKin): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KinsViewHolder {
        return KinsViewHolder(KinsItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: KinsViewHolder, position: Int) {
        val kinsItems = getItem(position)
        holder.itemView.ClKIn.setOnClickListener {
            onClickListener.click(kinsItems)
        }
        holder.bind(kinsItems)
    }

    class KinsViewHolder(private val binding: KinsItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bankItems: NextOfKin) {
            binding.kinsProprerties = bankItems
            val posone=bankItems.fullName[0].toString().toUpperCase(Locale.ENGLISH)
            val postwo=bankItems.fullName[1].toString().toUpperCase(Locale.ENGLISH)
            binding.initials.text=" $posone $postwo"
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedKins: NextOfKin) -> Unit){
        fun click(selectedKins: NextOfKin)=clickListener(selectedKins)
    }
}