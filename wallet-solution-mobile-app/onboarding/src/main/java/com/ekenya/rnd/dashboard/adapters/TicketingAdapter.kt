package com.ekenya.rnd.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.datadashboard.model.StatementItem
import com.ekenya.rnd.dashboard.view.AvailableTicketsFragment
import com.ekenya.rnd.onboarding.databinding.AvailableBusItemLayoutBinding

class TicketingAdapter(context: AvailableTicketsFragment, val destination1: String, val destination2: String) :
    RecyclerView.Adapter<TicketingViewHolder>() {

    private var transactionItems = mutableListOf<StatementItem>()
    private val context = context

    fun sendTransactions(dashboardItems: List<StatementItem>) {
        this.transactionItems = dashboardItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketingViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = AvailableBusItemLayoutBinding.inflate(inflater, parent, false)
        return TicketingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: TicketingViewHolder, position: Int) {
        holder.binding.fromDestination.text = destination1
        holder.binding.toDestination.text = destination2
        holder.binding.tvRemainingSeats.text ="Seats "+SharedPreferencesManager.getseats(context.requireContext())

        holder.binding.btnBook.setOnClickListener {

           // context.showCardPaymentOptionsDialog(Constants.TICKETING_FRAGMENT)

            //Toast.makeText(context.requireContext(), "Feature Coming Soon", Toast.LENGTH_SHORT).show()
            // it.findNavController().navigate(R.id.billPaymentsConfirmationFragment)
        }
    }
}

class TicketingViewHolder(val binding: AvailableBusItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

}