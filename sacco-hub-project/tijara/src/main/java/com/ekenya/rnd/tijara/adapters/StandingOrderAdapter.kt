package com.ekenya.rnd.tijara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.SavingAccountRowBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.StandingOrderData
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.StandingOrderCallBack
import com.ekenya.rnd.tijara.utils.bindImage
import kotlinx.android.synthetic.main.merchants_items_lists.view.*
import kotlinx.android.synthetic.main.saving_account_row.view.*
import kotlinx.android.synthetic.main.statement_account_row.view.*
import java.util.*

class StandingOrderAdapter(val context: Context?, val savingList: List<StandingOrderData>,val standingOrderCallBack: StandingOrderCallBack)
    :RecyclerView.Adapter<StandingOrderAdapter.SavingAccViewHolder>(){
    var name1=""
    var posone=""
    var postwo=""

   inner class SavingAccViewHolder(private val binding: SavingAccountRowBinding):RecyclerView.ViewHolder(binding.root){
        fun bindItems(savingItems: StandingOrderData){
           // var accNumber=savingItems.accountNo.replace("\\w(?=\\w{4})".toRegex(),"*")
            var accNumber=savingItems.toAccountNo.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
          binding.tvNo.text = "A/C#${accNumber}"
             val splited: List<String> = savingItems.toAccount?.split("\\s".toRegex())
            if( splited.count() == 2){
                val firstName=splited[0]
                val name2=(firstName).toUpperCase(Locale.ENGLISH)
                val lastName=splited[1]
                name1=(lastName).toUpperCase(Locale.ENGLISH)
                posone=name1[0].toString().toUpperCase(Locale.ENGLISH)
                postwo=name2[0].toString().toUpperCase(Locale.ENGLISH)
                binding.sInitials.text=" $postwo $posone"
            }else if (splited.count()===3){
                val firstName=splited[0]
                val name2=(firstName).toUpperCase(Locale.ENGLISH)
                val lastName=splited[1]
                name1=(lastName).toUpperCase(Locale.ENGLISH)
                posone=name1[0].toString().toUpperCase(Locale.ENGLISH)
                postwo=name2[0].toString().toUpperCase(Locale.ENGLISH)
                binding.sInitials.text=" $postwo $posone"
            }else{
                val names=savingItems.toAccount
                posone=names[0].toString().toUpperCase(Locale.ENGLISH)
                postwo=names[0].toString().toUpperCase(Locale.ENGLISH)
                binding.sInitials.text=" $postwo $posone"
            }
            binding.tvHolder.text =savingItems.toAccount
           binding.ConstraintLoan.setOnClickListener {
               standingOrderCallBack.onItemSelected(savingItems)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingAccViewHolder {
        return SavingAccViewHolder(SavingAccountRowBinding.inflate(LayoutInflater.from(context)))
    }

    override fun getItemCount(): Int {
        return savingList.size
    }
    override fun onBindViewHolder(holder: SavingAccViewHolder, position: Int) {
        val savingItem=savingList[position]
        holder.bindItems(savingItem)
        /*if(position == savingList.size-1){
           holder.itemView.vieShare.visibility=View.GONE
        }*/
   }
  }