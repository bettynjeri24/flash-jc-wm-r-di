package io.eclectics.cargilldigital.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FarmerMyaccountLayoutBinding
import io.eclectics.cargilldigital.data.model.FarmerAccount
import javax.inject.Inject

class SendMoneyAccListAdapter @Inject constructor(val clickListener: AccountListener,var data:List<FarmerAccount.BeneficiaryAccObj>) : RecyclerView.Adapter<SendMoneyAccListAdapter.ViewHolder>() {

    /*var data = TransactionRepository.transactionsList.value
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      //  holder.itemView.setOnClickListener { it.findNavController().navigate(R.id.nav_) }
         holder.bind(clickListener, data!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: FarmerMyaccountLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AccountListener, item: FarmerAccount.BeneficiaryAccObj) { // a refactor of onBindViewHolder method

             binding.apply {
                // ivAccountIcon.setImageResource(item.iconId)
                 getImageResource(item.channelName)
                 tvName.text = item.beneficiaryName
                 tvPhoneNumber.text = item.channelNumber
                 tvCreationDate.text = "Date: ${item.dateCreated}"

                     /*tvTransaction.text =  "Disbusment: ${item.agentId}"
                 tvDescription.text = "Farm No: ${item.farmerNumber},${item.kgsAccepted} Kgs"
                 tvAmount.text = NetworkUtility().cashFormatter(item.amountValue!!)//"CFA ${item.amountValue}"
                 tvDate.text = item.syncDate*/
             }
            binding.imgnext.setOnClickListener {
                clickListener.onClick(item,"navigate")
            }
            binding.root.setOnClickListener { clickListener.onClick(item,"navigate") }

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
                val binding = FarmerMyaccountLayoutBinding.inflate(layoutInflater, parent, false)

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