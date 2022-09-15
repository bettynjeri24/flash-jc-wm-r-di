package io.eclectics.cargilldigital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.AppCargillDigital
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.AdapterEvalueBookingsBinding
import io.eclectics.cargilldigital.data.model.CoopEvalue
import io.eclectics.cargill.utils.NetworkUtility
import javax.inject.Inject

class BookingEvalueAdapter  @Inject constructor(var context: Context, val clickListener: BookedEvalueListListener, var data:List<CoopEvalue.EvalueList>): RecyclerView.Adapter<BookingEvalueAdapter.ViewHolder>() {

    /*var data = TransactionRepository.transactionsList.value
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context,clickListener, data!![position])

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterEvalueBookingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            clickListener: BookedEvalueListListener,
            item: CoopEvalue.EvalueList
        ) { // a refactor of onBindViewHolder method
             binding.apply {
                 tvBookedBy.text =  "${context.resources.getString(R.string.booked_by)}: ${item.bookedBy}"
                 tvDescription.text = item.reasonForBooking
                 tvAmount.text = NetworkUtility().cashFormatter(item.amountBooked!!.toString())//"CFA ${item.amountValue}"
                 tvDateBooked.text = item.dateOfBooking
                 if(item.status!!){
                     binding.btnApproveEvalue.apply {
                         text = context.resources.getString(
                             R.string.approved
                         )
                         setBackgroundColor(AppCargillDigital.applicationContext().resources.getColor(R.color.txtGray))
                         //setStrokeColorResource(AppCargillDigital.applicationContext().resources.getColorStateList(R.color.white_gray))
                         strokeColor = resources.getColorStateList(R.color.white_gray)
                     }

                 }else {
                     binding.btnApproveEvalue.setOnClickListener {
                         clickListener.onClick(item, "approve")
                     }
                 }

             }

//            if (item.transType == "credit"){
//                binding.tvAmount.setTextColor(R.color.accent2)
//                binding.tvAmount.setCompoundDrawables(R.drawable.ic_arrow_up,0,0,0)
//                binding.tvAmount.compoundDrawableTintMode = R.color.accent2
//            }
        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterEvalueBookingsBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

    class BookedEvalueListListener(val clickListener: (acc: CoopEvalue.EvalueList, action: String) -> Unit) {
        fun onClick(acc: CoopEvalue.EvalueList, action: String) = clickListener(acc, action)
    }
}