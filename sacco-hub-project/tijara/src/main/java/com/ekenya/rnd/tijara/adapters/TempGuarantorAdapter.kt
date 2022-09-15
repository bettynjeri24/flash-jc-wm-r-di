package com.ekenya.rnd.tijara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.databinding.TempGuarantorsRowBinding
import com.ekenya.rnd.tijara.network.model.TempGuarantor
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.TempGuarantorsCallBack
import kotlinx.android.synthetic.main.temp_guarantors_row.view.*
import java.util.*


class TempGuarantorAdapter(private val onClickListener: OnClickListener,private val tempCallback:TempGuarantorsCallBack): ListAdapter<TempGuarantor, TempGuarantorAdapter.TempGuarantorViewHolder>(
    DiffCallBack
) {
    object DiffCallBack : DiffUtil.ItemCallback<TempGuarantor>() {
        override fun areItemsTheSame(oldItem: TempGuarantor, newItem: TempGuarantor): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TempGuarantor, newItem: TempGuarantor): Boolean {
            return oldItem.tempGuarantorId == newItem.tempGuarantorId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempGuarantorViewHolder {
        return TempGuarantorViewHolder(TempGuarantorsRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TempGuarantorViewHolder, position: Int) {

        val tempGuarantor = getItem(position)
        holder.bind(tempGuarantor)
        holder.itemView.iv_remove.setOnClickListener {
            Constants.isMore=false
            Constants.isRemove=true
            onClickListener.click(tempGuarantor)
            tempCallback.onItemSelected(tempGuarantor)
        }
        holder.itemView.setOnClickListener {
            Constants.isMore=true
            Constants.isRemove=false
            onClickListener.click(tempGuarantor)
            tempCallback.onItemSelected(tempGuarantor)
        }
    }


    class TempGuarantorViewHolder(private val binding: TempGuarantorsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tempGuarantor: TempGuarantor) {
            binding.tempGuarantor = tempGuarantor
            val posone=tempGuarantor.memberName[0].toString().toUpperCase(Locale.ENGLISH)
            val postwo=tempGuarantor.memberName[1].toString().toUpperCase(Locale.ENGLISH)
            binding.initials.text=" $posone $postwo"
            binding.amount.text= FormatDigit.formatDigits(tempGuarantor.amount)
           // binding.amount.text= String.format("%,d","%.2f".format(tempGuarantor.amount))
            binding.executePendingBindings()


        }/* binding.tvPercentage.text="%.2f".format(texte)+"%"
        val amountRequested = item.amountRequested
        binding.tvAmount.text="%.2f".format(amountRequested)*/

    }
    class OnClickListener(val clickListener: (tempGuarantor: TempGuarantor) -> Unit){
        fun click(tempGuarantor: TempGuarantor)=clickListener(tempGuarantor)
    }

}


