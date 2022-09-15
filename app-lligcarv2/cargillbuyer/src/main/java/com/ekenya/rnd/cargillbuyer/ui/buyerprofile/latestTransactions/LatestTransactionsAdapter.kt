package com.ekenya.rnd.cargillbuyer.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.data.responses.LatestTransactionsData
import com.ekenya.rnd.cargillbuyer.data.responses.FfPendingPaymentsResponse
import com.ekenya.rnd.cargillbuyer.databinding.AdapterBuyerTransactionsBinding
import com.ekenya.rnd.common.data.db.entity.CargillUserTransactionData
import com.ekenya.rnd.common.utils.custom.cashFormatter


class LatestTransactionsAdapter(
    private val model: List<LatestTransactionsData>
) : ListAdapter<LatestTransactionsData, LatestTransactionsAdapter.ViewHolder>(
    DIFF_UTIL
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = AdapterBuyerTransactionsBinding.inflate(
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
        val item = model[position]
        holder.binding.apply {
            tvTransaction.text = "${item.sendorName}"
            tvDescription.text = "Account: ${item.sendorPhoneNumber}"
            tvDate.text = item.datecreated
            if (item.sendorPhoneNumber!!.isNotEmpty()) {
                tvAmount.apply {
                    val imgs = ContextCompat.getDrawable(
                        this.context,
                        com.ekenya.rnd.common.R.drawable.ic_arrow_down
                    )!!
                    setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                    imgs.setTint(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.reddish
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.reddish
                        )
                    )
                    text = "-${cashFormatter(model[position].amount!!)}"
                }

            } else {
                tvAmount.apply {
                    val imgs = ContextCompat.getDrawable(
                        this.context,
                        com.ekenya.rnd.common.R.drawable.ic_arrow_up
                    )!!
                    setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                    imgs.setTint(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.primary_green
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this.context,
                            com.ekenya.rnd.common.R.color.primary_green
                        )
                    )
                    text = cashFormatter(model[position].amount!!)
                }
            }


        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    inner class ViewHolder(val binding: AdapterBuyerTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root)

}

private val DIFF_UTIL =
    object : DiffUtil.ItemCallback<LatestTransactionsData>() {
        override fun areItemsTheSame(
            oldItem: LatestTransactionsData,
            newItem: LatestTransactionsData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LatestTransactionsData,
            newItem: LatestTransactionsData
        ): Boolean {
            return oldItem == newItem
        }

    }


interface OnLatestTransactionsDataItemListener {
    fun onItemClicked(view: View, model: LatestTransactionsData)
}
