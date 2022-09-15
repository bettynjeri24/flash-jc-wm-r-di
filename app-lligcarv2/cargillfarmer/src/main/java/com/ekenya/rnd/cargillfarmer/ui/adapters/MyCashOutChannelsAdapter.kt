package com.ekenya.rnd.cargillfarmer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillfarmer.data.responses.MyCashOutChannelsData
import com.ekenya.rnd.cargillfarmer.databinding.FarmerMyaccountLayoutBinding


class MyCashOutChannelsAdapter(
    private val modelList: List<MyCashOutChannelsData>,
    private val listener: OnMyCashOutChannelsItemClickedListener
) : ListAdapter<MyCashOutChannelsData, MyCashOutChannelsAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = FarmerMyaccountLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = modelList[position]
        holder.binding.apply {
            root.setOnClickListener {
                listener.onItemClicked(it, item)
            }
            holder.getImageResource(item.channelName.toString())
            tvName.text = item.beneficiaryName
            tvPhoneNumber.text = item.channelNumber
            tvCreationDate.text = "Date: ${item.dateCreated}"
            imgnext.setOnClickListener {
                listener.onItemClicked(it, item)
            }
            rootMyCashOutCard.setOnClickListener { listener.onItemClicked(it, item) }
        }


    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    inner class ViewHolder(val binding: FarmerMyaccountLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun getImageResource(channelName: String) {
            when (channelName) {
                "Orange" -> {
                    binding.ivAccountIcon.setImageResource(com.ekenya.rnd.common.R.mipmap.orangemoney)
                }
                "MTN" -> {
                    binding.ivAccountIcon.setImageResource(com.ekenya.rnd.common.R.mipmap.mtnmoney)
                }
                "Orabank" -> binding.ivAccountIcon.setImageResource(com.ekenya.rnd.common.R.mipmap.orabank)
                "Wave" -> binding.ivAccountIcon.setImageResource(com.ekenya.rnd.common.R.mipmap.wave)
                "Moov" -> binding.ivAccountIcon.setImageResource(com.ekenya.rnd.common.R.mipmap.moovafrica)
                else -> {
                    binding.ivAccountIcon.setImageResource(com.ekenya.rnd.common.R.mipmap.otp_icon)
                }
            }
        }
    }


}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<MyCashOutChannelsData>() {
    override fun areItemsTheSame(
        oldItem: MyCashOutChannelsData,
        newItem: MyCashOutChannelsData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: MyCashOutChannelsData,
        newItem: MyCashOutChannelsData
    ): Boolean {
        return oldItem == newItem
    }

}


interface OnMyCashOutChannelsItemClickedListener {
    fun onItemClicked(view: View, model: MyCashOutChannelsData)
}