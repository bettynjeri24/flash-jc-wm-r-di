package com.ekenya.rnd.tijara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.PesaLinkRowBinding
import com.ekenya.rnd.tijara.network.model.ActivesLoan
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.utils.bindImage
import com.ekenya.rnd.tijara.utils.callbacks.SelectLoanCallBack
import com.ekenya.rnd.tijara.utils.loadImageSrc
import kotlinx.android.synthetic.main.pesa_link_row.view.*
import kotlinx.android.synthetic.main.statement_account_row.view.*
import kotlinx.android.synthetic.main.statement_account_row.view.Cl_SAccount

class SelectLoanAccountAdapter(val context: Context?, val loanList: List<ActivesLoan>,private val callBack:SelectLoanCallBack)
    :RecyclerView.Adapter<SelectLoanAccountAdapter.SelectAccViewHolder>(){

   inner class SelectAccViewHolder(val itemBinding:PesaLinkRowBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun bindItems(savingItems: ActivesLoan){
            itemBinding.ConstraintLoan.setOnClickListener {
                // loadImageSrc(itemView.imageView4,savingItems.ur)
                callBack.onItemSelected(savingItems)
            }
            itemBinding.tvLoanOption.text=savingItems.name
            itemBinding.viewLine.setImageResource(R.mipmap.app_logo)

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAccViewHolder {
        return SelectAccViewHolder(PesaLinkRowBinding.inflate(LayoutInflater.from(context)))
    }

    override fun getItemCount(): Int {
        return loanList.size
    }
    override fun onBindViewHolder(holder: SelectAccViewHolder, position: Int) {
        val savingItem=loanList[position]
        holder.bindItems(savingItem)

   }
  }