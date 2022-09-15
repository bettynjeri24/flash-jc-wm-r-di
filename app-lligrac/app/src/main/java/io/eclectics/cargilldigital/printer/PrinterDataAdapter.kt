package io.eclectics.cargilldigital.printer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.databinding.AdapterPrinterLayoutBinding

class PrinterDataAdapter(var context: Context, var data: List<PrinterData>) :
    RecyclerView.Adapter<PrinterDataAdapter.ViewHolder>() {

    /*var data = TransactionRepository.transactionsList.value
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, data!![position])

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterPrinterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            item: PrinterData
        ) { // a refactor of onBindViewHolder method
            binding.apply {
                //tvPrinterRow.text =  "${context.resources.getString(R.string.booked_by)}: ${item.bookedBy}"
                tvPrinterRow.text = item.printRow
                /* tvAmount.text = NetworkUtility().cashFormatter(item.amountBooked!!.toString())//"CFA ${item.amountValue}"
                 tvDateBooked.text = item.dateOfBooking
                 if(item.status!!){
                     binding.btnApproveEvalue.apply {
                         text = context.resources.getString(
                             R.string.approved
                         )
                         setBackgroundColor(
                             AppCargillDigital.applicationContext().resources.getColor(
                                 R.color.txtGray))
                         //setStrokeColorResource(AppCargillDigital.applicationContext().resources.getColorStateList(R.color.white_gray))
                         strokeColor = resources.getColorStateList(R.color.white_gray)
                     }

                 }else {
                     binding.btnApproveEvalue.setOnClickListener {
                         clickListener.onClick(item, "approve")
                     }
                 }*/

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
                val binding = AdapterPrinterLayoutBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

    class PrinterData(
        var printRow: String,
        var color: Int,
        var size: Int,
        var separator: Boolean
    )
}