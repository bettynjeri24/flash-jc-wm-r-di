package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.ffpendingpayments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillbuyer.data.responses.FfPendingPaymentsData
import com.ekenya.rnd.cargillbuyer.databinding.ItemAdapterPendingPaymentsBinding


class FfPendingPaymentsListAdapter(
    private val model: List<FfPendingPaymentsData>,
    private val listener: OnFfPendingPaymentsItemListener
) : ListAdapter<FfPendingPaymentsData, FfPendingPaymentsListAdapter.ViewHolder>(
    DIFF_UTIL
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAdapterPendingPaymentsBinding.inflate(
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
            tvFarmerName.text = "${item.firstName} ${item.lastName}"
            tvReferenceNo.text = "Reference No: ${item.farmForceRef}"
            tvTotalAmount.text = item.fullAmount
            tvPaidAmount.text = item.amountPaid
            val balance = item.fullAmount!!.toInt() - item.amountPaid!!.toInt()
            tvbalance.text = balance.toString()

            clRootPendingPayment.setOnClickListener {
                listener.onItemClicked(it, item)
            }
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    inner class ViewHolder(val binding: ItemAdapterPendingPaymentsBinding) :
        RecyclerView.ViewHolder(binding.root)

}

private val DIFF_UTIL =
    object : DiffUtil.ItemCallback<FfPendingPaymentsData>() {
        override fun areItemsTheSame(
            oldItem: FfPendingPaymentsData,
            newItem: FfPendingPaymentsData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FfPendingPaymentsData,
            newItem: FfPendingPaymentsData
        ): Boolean {
            return oldItem == newItem
        }

    }


interface OnFfPendingPaymentsItemListener {
    fun onItemClicked(view: View, model: FfPendingPaymentsData)
}
