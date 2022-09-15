package com.ekenya.rnd.tijara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.MobileMoneyBottomSheetRowBinding
import com.ekenya.rnd.tijara.databinding.MobileMoneyListItemBinding
import com.ekenya.rnd.tijara.databinding.ViewBankInfoItemListsBinding
import com.ekenya.rnd.tijara.network.model.ServiceProviderItem
import com.ekenya.rnd.tijara.network.model.ViewBankInfoList
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.MobileMoneyViewmodel
import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.android.synthetic.main.view_bank_info_item_lists.view.*
import timber.log.Timber
class MobileMoneyAdapter(val context: Context,private val onClickListener: OnClickListener): ListAdapter<ServiceProviderItem, MobileMoneyAdapter.MobileMoneyViewHolder>(
    DiffCallBack
)

{
    object DiffCallBack : DiffUtil.ItemCallback<ServiceProviderItem>() {
        override fun areItemsTheSame(oldItem: ServiceProviderItem, newItem: ServiceProviderItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ServiceProviderItem, newItem: ServiceProviderItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MobileMoneyViewHolder {
        return MobileMoneyViewHolder(MobileMoneyListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MobileMoneyViewHolder, position: Int) {
        val mMoneyItems = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.click(mMoneyItems)
            Timber.d(" rrrrrrrrr${mMoneyItems.name}")
        }
        holder.bind(mMoneyItems)
    }

    class MobileMoneyViewHolder(private val binding: MobileMoneyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mMoneyItems: ServiceProviderItem) {
            binding.mobileProperties = mMoneyItems
            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedBank: ServiceProviderItem) -> Unit){
        fun click(selecteditem: ServiceProviderItem)=clickListener(selecteditem)
    }



}