package com.ekenya.rnd.tijara.ui.homepage.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.layoutAdapter.OtherServicesAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.ServiceLifestyleAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.ServicesPaymentAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.ServicesStatementAdapter
import com.ekenya.rnd.tijara.databinding.FragmentServiceBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.network.model.BillerData
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.utils.makeGone
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class ServiceFragment : Fragment() {
    lateinit var binding: FragmentServiceBinding
    lateinit var paymentAdapter:ServicesPaymentAdapter
    private  var items:ArrayList<BillPaymentMerchant> = ArrayList()
    var arrayList = ArrayList<BillPaymentMerchant>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentServiceBinding.inflate(layoutInflater)
        handleBackButton()
        binding.apply {
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
            items.clear()
            addPaymentItems()
            addLifeStyleItems()
            addStatementItems()
            addOtherServices()
            arrayList.clear()
            arrayList.addAll(items)
            rvPayment.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            paymentAdapter= ServicesPaymentAdapter()
            rvPayment.adapter=paymentAdapter
            paymentAdapter.swapData(items)
            paymentAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    val query = binding.search.query.toString()
                    if (paymentAdapter.itemCount <= 0 && query.isNotBlank()){
                        binding.Noservises.visibility=View.VISIBLE
                    }else{
                        binding.Noservises.visibility=View.GONE
                    }
                    super.onChanged()
                }
            })

        }
        searchAvailableServices()


        return binding.root
    }
    private fun searchAvailableServices(){
        val searchView = binding.search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                paymentAdapter.filter.filter(newText)
                return true
            }
        })

    }


    private fun addPaymentItems() {
        if(Constants.isSacco==true){
            items.add(BillPaymentMerchant(R.drawable.h_shares,"My Shares", R.drawable.arrow_right,"Payment"))
            items.add(BillPaymentMerchant(R.drawable.h_send,"Send Money", R.drawable.arrow_right,"Payment"))
            //   items.add(BillPaymentMerchant(R.drawable.qr_transactions,"QR Transactions", R.drawable.arrow_right,"Payment"))
            items.add(BillPaymentMerchant(R.drawable.h_deposit,"Make Deposit", R.drawable.arrow_right,"Payment"))
        }else{
            //  items.add(BillPaymentMerchant(R.drawable.my_shares,"My Shares", R.drawable.arrow_right,"Payment"))
            items.add(BillPaymentMerchant(R.drawable.h_send,"Send Money", R.drawable.arrow_right,"Payment"))
            //  items.add(BillPaymentMerchant(R.drawable.qr_transactions,"QR Transactions", R.drawable.arrow_right,"Payment"))
            items.add(BillPaymentMerchant(R.drawable.h_deposit,"Make Deposit", R.drawable.arrow_right,"Payment"))
        }
    }
    private fun addLifeStyleItems() {
        items.add(BillPaymentMerchant(R.drawable.h_airtime,"Buy Airtime ", R.drawable.arrow_right,"Life Style"))
        items.add(BillPaymentMerchant(R.drawable.h_bill,"Bill Payment", R.drawable.arrow_right,"Life Style"))
    }
    private fun addStatementItems() {
        items.add(BillPaymentMerchant(R.drawable.mini_state,"Get Full Statement", R.drawable.arrow_right,"Statement"))
        items.add(BillPaymentMerchant(R.drawable.full_state,"Get Mini Statement", R.drawable.arrow_right,"Statement"))
    }
    private fun addOtherServices() {
        items.add(BillPaymentMerchant(R.drawable.loans,"Loans", R.drawable.arrow_right,"Other Services"))
        //  items.add(BillPaymentMerchant(R.drawable.customer_requests,"Customer Request", R.drawable.arrow_right,"Other Services"))
        items.add(BillPaymentMerchant(R.drawable.accounts,"Accounts", R.drawable.arrow_right,"Other Services"))
    }
    private fun handleBackButton() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                    //findNavController().navigate(R.id.action_serviceFragment_to_dashboardFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }


}