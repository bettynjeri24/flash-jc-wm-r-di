package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.layoutAdapter.BillMerchantsAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.BillerCategoryAdapter
import com.ekenya.rnd.tijara.databinding.FragmentBillersBinding
import com.ekenya.rnd.tijara.network.model.BillerData
import com.ekenya.rnd.tijara.requestDTO.BillerDTO
import com.ekenya.rnd.tijara.utils.callbacks.BillersCallBack
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class BillersFragment : BaseDaggerFragment(),BillersCallBack {
    private lateinit var binding: FragmentBillersBinding
    private lateinit var billerMerchantsAdapter: BillMerchantsAdapter
    var arrayList = ArrayList<BillerData>()
    val displayList = ArrayList<BillerData>()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewmodel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(BillersViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentBillersBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.billerViewmodel=viewmodel
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        billerMerchantsAdapter = BillMerchantsAdapter(context, displayList,this)
        binding.rvBiller.layoutManager = GridLayoutManager(activity, 1)
        binding.rvBiller.adapter = billerMerchantsAdapter
        searchBiller()
        viewmodel.billList.observe(viewLifecycleOwner, Observer {
            displayList.clear()
            arrayList.clear()
            displayList.addAll(it!!)
            billerMerchantsAdapter.notifyDataSetChanged()
            arrayList.addAll(it)
       })

        binding.apply {
             val billerCategories=BillerCategoryAdapter(BillerCategoryAdapter.OnClickListener{
            displayList.clear()
            val billerDTO=BillerDTO()
            billerDTO.categoryId=it.id.toString()
            viewmodel.getFilteredBillers(billerDTO)
         },requireContext())
            rvBillerCategories.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            rvBillerCategories.adapter=billerCategories
        }

    }
        private fun searchBiller(){
                val searchView = binding.search
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText!!.isNotEmpty()) {
                            Timber.d("SEARCHING")
                            displayList.clear()
                            binding.NoBillers.visibility=View.GONE
                            val search = newText.toLowerCase(Locale.US)
                            Timber.d("SEARCHING...$search")
                            arrayList.forEach {
                                if (it.name.toLowerCase(Locale.US).contains(search)) {
                                    Timber.d("DISPLAYING...$it")
                                    displayList.add(it)
                                }
                            }
                            if (displayList.isEmpty()){
                                binding.NoBillers.visibility=View.VISIBLE
                            }else{
                                binding.NoBillers.visibility=View.GONE
                            }
                            binding.rvBiller.adapter?.notifyDataSetChanged()
                        } else {
                            binding.NoBillers.visibility=View.GONE
                            displayList.clear()
                            displayList.addAll(arrayList)
                            binding.rvBiller.adapter?.notifyDataSetChanged()
                        }
                        return true
                    }
                })

        }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
      //  binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
       // binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.billers)
    }

    override fun onItemSelected(item: BillerData) {
        viewmodel.setBilerCode(item.code)
        viewmodel.setBillerName(item.name)
        viewmodel.setBillerUrl(item.logoUrl)
        viewmodel.setPresentment(item.hasPresentment)
        if (item.hasPresentment == 1) {
            if (item.code=="NRBC_SEASONAL_PARKING" || item.code=="NRBC_DAILY_PARKING"){
                findNavController().navigate(R.id.action_billersFragment_to_dailyParkingLookFragment)
            }else if (item.code=="COUNTY_PAYMENT") {
                findNavController().navigate(R.id.action_billersFragment_to_countyLookUpFragment)
            }else if (item.code=="KPLC_POST_PAID" || item.code=="DSTV_KENYA"
                || item.code=="GOTV_KENYA" ||item.code=="NRBC_WATER") {

                findNavController().navigate(R.id.action_billersFragment_to_kplcLookUpFragment)
            }else {
                /**kplc-prepaid and Nhif*/
                findNavController() .navigate(R.id.action_billersFragment_to_billerAccLookUpFragment)
            }
        }else{
           findNavController().navigate(R.id.action_billersFragment_to_payBillFragment)
        }
    }


}