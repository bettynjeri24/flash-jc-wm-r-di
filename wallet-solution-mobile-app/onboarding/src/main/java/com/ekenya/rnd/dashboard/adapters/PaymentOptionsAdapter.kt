package com.ekenya.rnd.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.datadashboard.model.BillItem
import com.ekenya.rnd.onboarding.databinding.PaymentoptionsItemlayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class PaymentOptionsAdapter(
    context: Context,
    parentFragment: Fragment?,
    dialog: BottomSheetDialog,
    customClickLister : MyClickListener
):
    RecyclerView.Adapter<PaymentOptionsAdapter.PaymentOptionsViewHolder>() {
    private val context = context
    private val parentFragment = parentFragment
    private val dialog = dialog
    private val mMyClickListener = customClickLister


    private var dashboardItems = mutableListOf<BillItem>()

    fun setDashBoardItems(items: List<BillItem>) {
        dashboardItems.clear()
        this.dashboardItems = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentOptionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = PaymentoptionsItemlayoutBinding.inflate(inflater, parent, false)
        return PaymentOptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentOptionsViewHolder, position: Int) {

        val dashboardItem = dashboardItems[position]
        holder.binding.paymentOptionTitle.text = dashboardItem.bill_Name
        holder.binding.paymentOptionDescription.text = dashboardItem.description
        dashboardItem.bill_icon?.let { holder.binding.paymentOptionIcon.setBackgroundResource(it) }

        val bundle = bundleOf("selectedOption" to dashboardItem.bill_Name)

        holder.binding.paymentOption.setOnClickListener {
            dialog.dismiss()
            //parentFragment?.findNavController()?.navigate(R.id.transactionConfirmationFragment,bundle)
            //val position: Int = holder.adapterPosition
            if (mMyClickListener != null) mMyClickListener.onItemClick(position)

        }

    }

    override fun getItemCount(): Int {
        return dashboardItems.size
    }


    class PaymentOptionsViewHolder(val binding: PaymentoptionsItemlayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    interface MyClickListener {
        fun onItemClick(position: Int)
    }
}