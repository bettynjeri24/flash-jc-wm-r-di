package com.ekenya.rnd.cargillfarmer.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillfarmer.data.responses.FarmerLatestTransactionData
import com.ekenya.rnd.cargillfarmer.databinding.ItemMinistatementAccountLayoutBinding
import com.ekenya.rnd.common.utils.custom.cashFormatter


class MiniStatementAccountWalletAdapter(
    private val model: List<FarmerLatestTransactionData>
) : ListAdapter<FarmerLatestTransactionData, MiniStatementAccountWalletAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemMinistatementAccountLayoutBinding.inflate(
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

        /* bind card details */
        holder.binding.apply {
            tVTitleMiniStatement.text = model[position].recipientName
            tVMiniStatementDateTime.text = model[position].datecreated

            /*       if (model[position].isMiniStatementCashOut) {
                       val color = "36B51F".toInt(16) + -0x1000000
                       tVMiniStatementAmount.setTextColor(color)
                   }*/
            if (model[position].status == true) {
                iVMiniStatement.setImageResource(com.ekenya.rnd.common.R.drawable.ic_unsuccessful_deposit_wallet)
                tVMiniStatementAmount.apply {
                    val imgs = ContextCompat.getDrawable(this.context, com.ekenya.rnd.common.R.drawable.ic_arrow_down)!!
                    setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                    imgs.setTint(ContextCompat.getColor(this.context, com.ekenya.rnd.common.R.color.reddish))
                    setTextColor(ContextCompat.getColor(this.context, com.ekenya.rnd.common.R.color.reddish))
                    text = "-${cashFormatter(model[position].amount!!)}"
                }
            } else {
                iVMiniStatement.setImageResource(com.ekenya.rnd.common.R.drawable.ic_succesful_deposit_wallet)
                tVMiniStatementAmount.apply {
                    val imgs = ContextCompat.getDrawable(this.context, com.ekenya.rnd.common.R.drawable.ic_arrow_up)!!
                    setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                    imgs.setTint(ContextCompat.getColor(this.context, com.ekenya.rnd.common.R.color.primary_green))
                    setTextColor(ContextCompat.getColor(this.context, com.ekenya.rnd.common.R.color.primary_green))
                    text = cashFormatter(model[position].amount!!)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    inner class ViewHolder(val binding: ItemMinistatementAccountLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<FarmerLatestTransactionData>() {
    override fun areItemsTheSame(
        oldItem: FarmerLatestTransactionData,
        newItem: FarmerLatestTransactionData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: FarmerLatestTransactionData,
        newItem: FarmerLatestTransactionData
    ): Boolean {
        return oldItem == newItem
    }

}
