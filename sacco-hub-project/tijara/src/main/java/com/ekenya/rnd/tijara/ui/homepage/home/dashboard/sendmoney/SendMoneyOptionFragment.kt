package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.layoutAdapter.SendMoneyItemsAdapter
import com.ekenya.rnd.tijara.databinding.FragmentSendMoneyOptionBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class SendMoneyOptionFragment : Fragment() {
    private lateinit var binding:FragmentSendMoneyOptionBinding
    private  var itemLists:ArrayList<BillPaymentMerchant> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSendMoneyOptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvSendMoney.layoutManager = GridLayoutManager(requireContext(), 1)
            val sItemAdapter= SendMoneyItemsAdapter(SendMoneyItemsAdapter.OnClickListener {
            },itemLists)
            rvSendMoney.adapter=sItemAdapter
            addSendMoneyItems()
            setupNavUp()
        }
    }
    private fun addSendMoneyItems() {
        itemLists.add(BillPaymentMerchant( R.drawable.s_mobile_money_transfer,getString(R.string.transfer_mobile_m),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant(  R.drawable.s_pesa_link,getString(R.string.transfer_pesalink),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant( R.drawable.s_internal_transfer,getString(R.string.transfer_internal),R.drawable.arrow_right))

    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.send_money)
    }
}