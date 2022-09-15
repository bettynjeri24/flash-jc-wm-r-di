package com.ekenya.rnd.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.datadashboard.model.BillItem
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.BillItemBinding

class FrequentBillersRvAdapter(context: Context) :
    RecyclerView.Adapter<BillPaymentsRecyclerviewAdapter.BillersViewHolder>() {
    private val context = context

    private var dashboardItems = mutableListOf<BillItem>()

    fun setDashBoardItems(items: List<BillItem>) {
        dashboardItems.clear()
        this.dashboardItems = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BillPaymentsRecyclerviewAdapter.BillersViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = BillItemBinding.inflate(inflater, parent, false)
        return BillPaymentsRecyclerviewAdapter.BillersViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BillPaymentsRecyclerviewAdapter.BillersViewHolder,
        position: Int
    ) {

        val dashboardItem = dashboardItems[position]





        holder.binding.tvBillName.text = dashboardItem.bill_Name.toString()
        dashboardItem.bill_icon?.let { holder.binding.billOptionLogo.setBackgroundResource(it) }
        holder.binding.billLayout.setOnClickListener {
            it.findNavController().navigate(R.id.dstvPayments)
            //holder.binding.dashboardItemTitle.text =dashboardItem.title

        }

        /*if (dashboardItems.size == 1) {
                // setOneTimeListeners(dashboardItem.bill_Name, holder)
            } else if (dashboardItems.size > 1) {
                setFullListClickListeners(position, holder)

            }*/


    }


    private fun setFullListClickListeners(
        position: Int,
        holder: BillPaymentsRecyclerviewAdapter.BillersViewHolder
    ) {
        when (position) {
            0 -> {
                holder.binding.billLayout.setOnClickListener {
                    it.findNavController().navigate(R.id.dstvPayments)
                    //holder.binding.dashboardItemTitle.text =dashboardItem.title

                }
            }
            1 -> holder.binding.billLayout.setOnClickListener {
                it.findNavController().navigate(R.id.botswanaAccountLookupFragment)

            }
            2 -> holder.binding.billLayout.setOnClickListener {
                it.findNavController().navigate(R.id.busTicketingFragment)
            }

            else -> holder.binding.billLayout.setOnClickListener { // Note the block
                Toast.makeText(context, "Feature Coming Soon", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return dashboardItems.size
    }


/*
    class BillersViewHolder(val binding: BillItemBinding) : RecyclerView.ViewHolder(binding.root) {


    }
*/
}