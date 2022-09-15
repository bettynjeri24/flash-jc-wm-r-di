package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.databinding.WorkItemListsBinding
import com.ekenya.rnd.tijara.network.model.WorkInfo
import kotlinx.android.synthetic.main.work_item_lists.view.*
import java.util.*

/**
 * class to list all the user employment info
 */
class WorkListAdapter (private val onClickListener: OnClickListener): ListAdapter<WorkInfo, WorkListAdapter.WorkViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<WorkInfo>() {
        override fun areItemsTheSame(oldItem: WorkInfo, newItem: WorkInfo): Boolean {
            return oldItem == newItem
        }

        /**
         *
         */
        override fun areContentsTheSame(oldItem: WorkInfo, newItem: WorkInfo): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        return WorkViewHolder(WorkItemListsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val workItems = getItem(position)
        holder.itemView.ClWork.setOnClickListener {
            onClickListener.click(workItems)
        }
        holder.bind(workItems)
    }

    class WorkViewHolder(private val binding: WorkItemListsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(workItems: WorkInfo) {
            binding.workProprerties = workItems
            if (workItems.employer!=null){
                val posone=workItems?.employer[0].toString().toUpperCase(Locale.ENGLISH)
                val postwo=workItems?.employer[1].toString().toUpperCase(Locale.ENGLISH)
                binding.initials.text=" $posone $postwo"
            }else{
                binding.initials.text=" * "
            }

            binding.executePendingBindings()

        }

    }
    class OnClickListener(val clickListener: (selectedWork: WorkInfo) -> Unit){
        fun click(selectedWork: WorkInfo)=clickListener(selectedWork)
    }


}