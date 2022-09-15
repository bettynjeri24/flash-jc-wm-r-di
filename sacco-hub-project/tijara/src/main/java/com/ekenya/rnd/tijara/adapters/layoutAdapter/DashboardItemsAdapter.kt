package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentDashboardItemsListBinding
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.utils.toastyInfo
import kotlinx.android.synthetic.main.fragment_dashboard_items_list.view.*

class DashboardItemsAdapter (val items:ArrayList<UserProfileList>,val context: Context): RecyclerView.Adapter<DashboardItemsAdapter.DashBoardItemViewHolder>() {
    private var binding: FragmentDashboardItemsListBinding?=null

    inner class DashBoardItemViewHolder(itemBinding: FragmentDashboardItemsListBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardItemViewHolder {
        binding= FragmentDashboardItemsListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DashBoardItemViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DashBoardItemViewHolder, position: Int) {
        val currentItems=items[position]
        holder.itemView.apply {
            binding?.tvIconText?.text=currentItems.name
            binding?.ivIconHolder?.setImageResource(currentItems.logo)

            /**clickListener to the notebody and title to navigate to update note screen**/
        }
        holder.itemView.cl_dashData.setOnClickListener {
            if (Constants.isSacco==true){
                when(position) {
                    0 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_buyAirtimeFragment)
                    }
                    1 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_sendMoneyOptionFragment)
                    }
                    2 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_billersFragment)
                    }
                    3 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_makeDepositFragment)
                    }
                    4 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_loanDashBoardFragment)
                    }
                    5 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_savingsFragment)
                    }
                    6 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_shareStatusFragment)
                    }
                    7 -> {
                        Navigation.findNavController(it).navigate(R.id.action_homeMainFragment_to_customerRequestFragment)
                    }
                }
            }else{
                when(position) {
                    0 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_buyAirtimeFragment)
                    }
                    1 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_sendMoneyOptionFragment)
                    }
                    2 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_billersFragment)
                    }
                    3 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_makeDepositFragment)
                    }
                    4 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_loanDashBoardFragment)
                    }
                    5 -> {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_homeMainFragment_to_savingsFragment)
                    }
                    6 -> {
                        Navigation.findNavController(it).navigate(R.id.action_homeMainFragment_to_customerRequestFragment)
                    }

                }
            }

        }

    }

}