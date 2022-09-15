package com.ekenya.rnd.cargillbuyer.ui.printer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillbuyer.databinding.AdapterPrinterLayoutBinding

class PrinterDataAdapter(var context: Context, var data: List<PrinterData>) :
    RecyclerView.Adapter<PrinterDataAdapter.ViewHolder>() {


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

                tvPrinterRow.text = item.textToPrint


            }

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
        var textToPrint: String,
        var color: Int,
        var size: Int,
        var separator: Boolean
    )
}