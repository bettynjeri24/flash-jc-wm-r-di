package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentLoanItemListBinding
import com.ekenya.rnd.tijara.databinding.SettingRowListBinding
import com.ekenya.rnd.tijara.databinding.UserProfileItemListBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.BillPaymentMerchantCallBack
import kotlinx.android.synthetic.main.fragment_loan_item_list.view.*
import kotlinx.android.synthetic.main.user_profile_item_list.view.*

class SettingsAdapter(val items:ArrayList<BillPaymentMerchant>,val settingMerchantCallBack: BillPaymentMerchantCallBack):RecyclerView.Adapter<SettingsAdapter.UserPViewHolder>() {
    private var binding: SettingRowListBinding?=null

    inner class UserPViewHolder(itemBinding: SettingRowListBinding)
        :RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPViewHolder {
        binding=SettingRowListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserPViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: UserPViewHolder, position: Int) {
        val currentItems=items[position]
        holder.itemView.apply {
            binding?.viewLine?.setImageResource(currentItems.image)
            binding?.tvTitleHolder?.text=currentItems.name
            binding?.ivImageHolder?.setImageResource(currentItems.logo)

            /**clickListener to the notebody and title to navigate to update note screen**/
        }
        holder.itemView.Cl_loan.setOnClickListener {
            settingMerchantCallBack.onItemSelected(currentItems,position)
        }

    }

}