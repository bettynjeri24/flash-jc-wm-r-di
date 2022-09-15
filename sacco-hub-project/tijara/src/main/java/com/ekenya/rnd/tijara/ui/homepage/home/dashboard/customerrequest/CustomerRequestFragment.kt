package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.layoutAdapter.CustomerRequestsAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.LoanDashboardItemsAdapter
import com.ekenya.rnd.tijara.databinding.FragmentCustomerRequestBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class CustomerRequestFragment : Fragment() {
private lateinit var binding:FragmentCustomerRequestBinding
    private lateinit var customerRequestsAdapter: CustomerRequestsAdapter

private  var itemLists:ArrayList<BillPaymentMerchant> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding= FragmentCustomerRequestBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvcustomerRequest.layoutManager = GridLayoutManager(requireContext(), 1)
            customerRequestsAdapter= CustomerRequestsAdapter(CustomerRequestsAdapter.OnClickListener {


            },itemLists)
            rvcustomerRequest.adapter=customerRequestsAdapter

        }
        addCustomerRequestItems()
        setupNavUp()
    }
    private fun addCustomerRequestItems() {
        itemLists.add(BillPaymentMerchant(R.drawable.ic_standing_orders,getString(R.string.standing_order), R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant( R.drawable.ic_cheque,getString(R.string.cheque_request),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant( R.drawable.ic_atm_card,getString(R.string.atm_card),R.drawable.arrow_right))
       // itemLists.add(BillPaymentMerchant( R.drawable.ic_loanportal,getString(R.string.loan_portal),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant( R.drawable.ic_receipt,getString(R.string.update_email),R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant(R.drawable.ic_membership_card,getString(R.string.membership_card), R.drawable.arrow_right))
        itemLists.add(BillPaymentMerchant(R.drawable.ic_complaints,getString(R.string.complaint), R.drawable.arrow_right))

    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.other_services)
    }
}