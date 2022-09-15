package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.network.model.ContactModel
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.ContactCallBack
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.android.synthetic.main.list_contact_item_list.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class ListContactAdapter(val contactList: ArrayList<ContactModel>,private val contactCallBack: ContactCallBack): RecyclerView.Adapter<ListContactAdapter.ContactAdapterViewHolder>(){

    inner class ContactAdapterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById(R.id.memberName) as TextView
        val txtPhone = itemView.findViewById(R.id.memberPhone) as TextView
        val txtInitials = itemView.findViewById(R.id.initials) as TextView

        fun bindItems(contact: ContactModel, position: Int) {
            txtName.setText(contact.name)

            txtPhone.text = contact.phonenumber
            if(contact.name.isEmpty()) {
                txtInitials.text = "*"
            }else{
                txtInitials.text= contact.name[0].toString().toUpperCase(Locale.ENGLISH)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapterViewHolder {
        val view=
                 LayoutInflater.from(parent.context).inflate(R.layout.list_contact_item_list,parent,false)
        return ContactAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
    override fun onBindViewHolder(holder: ContactAdapterViewHolder, position: Int) {
        val con=contactList[position]
        holder.bindItems(con,position)
        holder.itemView.clContact.setOnClickListener {
         /*   Constants.CONTACTPOSITION=position
            if (Constants.CONTACTFROM==0){
                Constants.CONTACTPHONE=con.phonenumber
            }else if (Constants.CONTACTFROM==1){
                Constants.CONTACTPHONE=con.phonenumber
            }*/
            contactCallBack.onItemSelected(con)




        }
       // notifyDataSetChanged()

    }


}

