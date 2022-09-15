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
import kotlinx.android.synthetic.main.fragment_loan_item_list.view.*
import kotlinx.android.synthetic.main.user_profile_item_list.view.*

class UserProfileAdapter(val items:ArrayList<BillPaymentMerchant>):RecyclerView.Adapter<UserProfileAdapter.UserPViewHolder>() {
    private var binding: SettingRowListBinding?=null

    inner class UserPViewHolder(itemBinding:SettingRowListBinding)
        :RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPViewHolder {
        binding=SettingRowListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserPViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return 7
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
            when(position){
                0 -> {
                    findNavController(it).navigate(R.id.action_userProfileFragment_to_viewPersonalInfoFragment)
                }
                1 -> {
                     findNavController(it).navigate(R.id.action_userProfileFragment_to_identificationFragment)
                }
                2 -> {
                    findNavController(it).navigate(R.id.action_userProfileFragment_to_bankListFragment)
                }
                3 ->{
                    findNavController(it).navigate(R.id.action_userProfileFragment_to_kinListFragment)
                }
                4 ->{
                    findNavController(it).navigate(R.id.action_userProfileFragment_to_workListFragment)
                }
                5 ->{
                    findNavController(it).navigate(R.id.action_userProfileFragment_to_residensenfoFragment)

                }
                6 -> {
                    findNavController(it).navigate(R.id.action_userProfileFragment_to_settingFragment)

                }
            }

        }

    }

}