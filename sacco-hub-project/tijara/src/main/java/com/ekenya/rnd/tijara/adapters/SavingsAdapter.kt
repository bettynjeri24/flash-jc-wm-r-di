package com.ekenya.rnd.tijara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.utils.bindImage
import kotlinx.android.synthetic.main.merchants_items_lists.view.*
import kotlinx.android.synthetic.main.saving_account_row.view.*
import kotlinx.android.synthetic.main.statement_account_row.view.*
import java.util.*

class SavingsAdapter(val context: Context?, val savingList: List<SavingAccountData>)
    :RecyclerView.Adapter<SavingsAdapter.SavingAccViewHolder>(){
    var name1=""

   inner class SavingAccViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(savingItems: SavingAccountData){
           // var accNumber=savingItems.accountNo.replace("\\w(?=\\w{4})".toRegex(),"*")
            var accNumber=savingItems.accountNo.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
          itemView.tvNo.text = "A/C#${accNumber}"
             val splited: List<String> = savingItems.accountName?.split("\\s".toRegex())
           if( splited.count() == 2){
               val lastName=splited[1]
               name1=(lastName).toUpperCase(Locale.ENGLISH)
           }
           val firstName=splited[0]
           val name2=(firstName).toUpperCase(Locale.ENGLISH)
           val finalName= "$name2 $name1"
            val posone=name1[0].toString().toUpperCase(Locale.ENGLISH)
            val postwo=name2[0].toString().toUpperCase(Locale.ENGLISH)
            itemView.sInitials.text=" $postwo $posone"
            itemView.tvHolder.text =savingItems.accountName
           itemView.ConstraintLoan.setOnClickListener {
               Constants.SAVINGAID= savingItems.accountId
               Constants.SAVINGPID= savingItems.productId.toString()
               Constants.SAVINGACCOUNTNAME= savingItems.accountName
               Constants.SAVINGACCOUNTNO= savingItems.accountNo
       it.findNavController().navigate(R.id.action_savingsFragment_to_selectStatementFragment)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingAccViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.saving_account_row,parent,false)
        return SavingAccViewHolder(view)
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