package io.eclectics.cargilldigital.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.AdapterFarmerAccountlistBinding
import io.eclectics.cargilldigital.data.model.FarmerAccount
import javax.inject.Inject

class BeneficiaryAccAdapter @Inject constructor(val clickListener: AccountListener,var data:List<FarmerAccount.BeneficiaryAccObj>) : RecyclerView.Adapter<BeneficiaryAccAdapter.ViewHolder>() {


    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  holder.itemView.setOnClickListener { it.findNavController().removeAcc(R.id.nav_) }
        holder.bind(clickListener, data!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterFarmerAccountlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AccountListener, item: FarmerAccount.BeneficiaryAccObj) { // a refactor of onBindViewHolder method

            binding.apply {

                getImageResource(item.channelName)
                //ivAccountIcon.setImageResource(x)
                tvName.text = item.beneficiaryName
                tvPhoneNumber.text = item.channelNumber
                tvCreationDate.text = "Date: ${item.dateCreated}"
            }
            binding.btnRemove.setOnClickListener {
                clickListener.onClick(item,"removeAcc")
            }
            binding.root.setOnClickListener { clickListener.onClick(item,"showaccdata") }

        }

        private fun getImageResource(channelName: String){
            when(channelName){
                "Orange"->{
                    binding.ivAccountIcon.setImageResource(R.mipmap.orangemoney)
                }
                "MTN"->{binding.ivAccountIcon.setImageResource(R.mipmap.mtnmoney)}
                "Orabank"->binding.ivAccountIcon.setImageResource(R.mipmap.orabank)
                "Wave"->binding.ivAccountIcon.setImageResource(R.mipmap.wave)
                "Moov"->binding.ivAccountIcon.setImageResource(R.mipmap.moovafrica)
                else ->{binding.ivAccountIcon.setImageResource(R.mipmap.otp_icon)}

            }
        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterFarmerAccountlistBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }
    /**
     * Handles clicks to the layout items
     */
    class AccountListener(val clickListener: (acc: FarmerAccount.BeneficiaryAccObj, action: String) -> Unit) {
        fun onClick(acc: FarmerAccount.BeneficiaryAccObj, action: String) = clickListener(acc, action)
    }

}