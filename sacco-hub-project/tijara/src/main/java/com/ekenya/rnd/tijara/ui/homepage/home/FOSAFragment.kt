package com.ekenya.rnd.tijara.ui.homepage.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.tijara.databinding.FragmentFosaBinding
import com.ekenya.rnd.tijara.network.model.Accounts
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.DashboardViewModel


class FOSAFragment (val accounts: Accounts): Fragment() {
    private lateinit var fosaBinding: FragmentFosaBinding
    private lateinit var viewModel: DashboardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fosaBinding=FragmentFosaBinding.inflate(layoutInflater)
        fosaBinding.lifecycleOwner=requireActivity()
        return fosaBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.accList.observe(viewLifecycleOwner, Observer {
            /**
             * get the size of the items in the model class
             */
            val list = it.size
            /**decrease the size of the item by 1*/
            val loop = list.minus(1)
            /**
             *elementAt-> Returns an element at the given index
             * or throws an IndexOutOfBoundsException if the index is out of bounds of this list
             */
            for (x in 0..loop){
                val accountType = it.elementAt(x).product
                if (accountType == accounts.toString()){
                    val availabeBal =it.elementAt(x).availableBalance
                    val currentBal =it.elementAt(x).currentBalance
                    fosaBinding.apply {
                        tvAvailableAmount.text=availabeBal
                        tvCurrency.text=it.elementAt(x).defaultCurrency
                        tvCurrentAmount.text=currentBal
                        tvDefCurrency.text=it.elementAt(x).defaultCurrency
                        tvDateSaved.text=it.elementAt(x).lastSavingDate
                        fosaBinding.tvProductName.setText(accountType)

                    }
                }


            }


        })
    }


}