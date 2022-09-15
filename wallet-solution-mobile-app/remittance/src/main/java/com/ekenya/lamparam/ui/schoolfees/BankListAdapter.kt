package com.ekenya.lamparam.ui.schoolfees

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.layout_banklist_row.view.*
import java.util.*

class BankListAdapter(val activity: FragmentActivity?, val bankList:List<SchoolListModel>):RecyclerView.Adapter<BankListAdapter.BankListViewHolder>() {

    inner class BankListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title = itemView.tv_bank_name
        val drawableTextView = itemView.drawableTextView
        val cv_bank_item = itemView.cv_bank_item
        val bankAccount =itemView.tv_bank_account

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListViewHolder {
        return BankListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_banklist_row,parent,false))
    }

    override fun getItemCount(): Int {
        return bankList.size
    }

    override fun onBindViewHolder(holder: BankListViewHolder, position: Int) {
       holder.title.setText(bankList.get(position).name)
        holder.bankAccount.setText(bankList.get(position).accNo)
//        val gradientDrawable:GradientDrawable =
//            holder.drawableTextView.getBackground() as GradientDrawable;
//        gradientDrawable.setColor(getRandomColor());
       /* val gradientDrawable =
            holder.drawableTextView.background as GradientDrawable
        holder.drawableTextView.setBackgroundColor(getRandomColor())*/
        val  serviceSubString:String= (bankList.get(position).name.substring(0,1))
        val selectedBundle = Bundle()
       // UtilityConstant.schoolName=""
        selectedBundle.putString("name",bankList.get(position).name)

        holder.drawableTextView.setImageResource(bankList[position].img)

        holder.cv_bank_item.setOnClickListener {
          //  UtilityConstant.schoolAcc = bankList.get(position).bank_code
            //UtilityConstant.schoolName = bankList.get(position).name
            it.findNavController().navigate(R.id.nav_feeMenu)
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(
            255,
            rnd.nextInt(256),
            rnd.nextInt(256),
            rnd.nextInt(256)
        )
    }
}