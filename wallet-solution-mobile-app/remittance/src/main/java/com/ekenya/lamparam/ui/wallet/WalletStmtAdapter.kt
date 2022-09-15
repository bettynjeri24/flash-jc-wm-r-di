package com.ekenya.lamparam.ui.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.layout_ministatement.view.*

class WalletStmtAdapter : RecyclerView.Adapter<WalletStmtAdapter.MinistatementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinistatementViewHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return MinistatementViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_ministatement,parent,false))
    }

    override fun getItemCount(): Int {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 20
    }

    override fun onBindViewHolder(holder: MinistatementViewHolder, position: Int) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class MinistatementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtTransactionName = itemView.txtTranssName
        val imgstatus = itemView.imgStatus

    }
}