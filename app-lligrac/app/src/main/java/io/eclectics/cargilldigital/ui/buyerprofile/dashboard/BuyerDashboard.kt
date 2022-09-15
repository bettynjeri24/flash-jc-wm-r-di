package io.eclectics.cargilldigital.ui.buyerprofile.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.CollectionsAdapter
import io.eclectics.cargilldigital.databinding.FragmentBuyerDashboardBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargill.viewmodel.FarmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.Exception

@AndroidEntryPoint
class BuyerDashboard : Fragment() {
    private var _binding: FragmentBuyerDashboardBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navOptions: NavOptions
    val viewModel:FarmViewModel by viewModels()
    lateinit var collectionAdapter:CollectionsAdapter
    lateinit var userData: UserDetailsObj
    lateinit var onBackPressedCallback: OnBackPressedCallback
    //delete this
    val genViewModel: GeneralViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuyerDashboardBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        UtilPreference().saveActiveprofile(requireActivity(),R.id.nav_agentProfile)
        UtilPreference().setActiveDashbaord(requireActivity(),R.id.action_agentProfile)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_logout)
               // NetworkUtility().transactionWarning("You wantto exit?",requireActivity())
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,onBackPressedCallback)
        //binding.setLifecycleOwner(getViewLifecycleOwner());
        getWalletAPiBalance()
        populatedashboard()
       // buyerTransactionList()
        setUpTabs()
        binding.toolbar.ivNotification.setOnClickListener {
            //findNavController().navigate(R.id.nav_selectAccount)
            (activity as MainActivity?)!!.navigateToAccounts()
        }
        binding.toolbar.ivProfile.setOnClickListener {
            (activity as MainActivity?)!!.openDrawer()
        }
        binding
            .clRequestFund.setOnClickListener {
                findNavController().navigate(R.id.nav_fundsRequest,null,navOptions)
            }

        binding.clPayFarmers.setOnClickListener {
            findNavController().navigate(R.id.nav_farmerList,null,navOptions)
        }
        binding.clPaymentList.setOnClickListener {
            findNavController().navigate(R.id.nav_peding_paymentList,null,navOptions)
        }

        //setup tab layout
        //var rcvRecentDisbursment = binding.rvTransactins

        binding.tvAvailableBal.setOnClickListener {
             lifecycleScope.launch {
                // getFarmerWalletBalance()
                getWalletAPiBalance()
            }
        }

    }

    private fun setUpTabs() {
        val fragmentAdapter = BuyerHomeTabsAdapter(childFragmentManager, lifecycle, context)
        binding.viewpagerMain.adapter = fragmentAdapter
        TabLayoutMediator(binding.tabsMain, binding.viewpagerMain) { tab, position ->
            tab.text = fragmentAdapter.getPageTitle(position)
        }.attach()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedCallback.remove()
    }
    private fun getWalletAPiBalance() {
        try {
            var endpoint = ApiEndpointObj.walletBalanceEnquiry
            var lookupJson = JSONObject()
            lookupJson.put("coopIndex", userData.cooperativeIndex)
            lookupJson.put("phonenumber", userData.phoneNumber)
            CoroutineScope(Dispatchers.Main).launch {
                // pdialog.show()
                genViewModel.walletBalanceRequest(lookupJson, endpoint, requireActivity())
                    .observe(requireActivity(), androidx.lifecycle.Observer {
                        // pdialog.dismiss()
                        when (it) {
                            is ViewModelWrapper.error -> LoggerHelper.loggerError(
                                "nonet",
                                "no internet"
                            )//GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                            is ViewModelWrapper.response -> processBalRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                            //processRequest(it.value)//
                        }
                    })
            }
        }catch (ex:Exception){}
    }


    private fun processBalRequest(response: String) {
        try {
//nav_selectAccount
            LoggerHelper.loggerError("offlineproduct", "response test loop")

            LoggerHelper.loggerSuccess("response", "product response ${response}")
            UtilPreference().saveWalletBalance(requireActivity(), response)
            val number = response.toInt()
            val COUNTRY = "CI"//CI
            val LANGUAGE = "fr"//fr
            val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
            binding.tvAvailableBal.text = " ${str.toString()}"
            try {
                var expenditure = number / 4
                val exps = NumberFormat.getCurrencyInstance(Locale(LANGUAGE,COUNTRY)).format(expenditure)
                binding.tvActualBal.text = " ${exps.toString()}"
            }catch (ex:Exception){}
            //UtillPreference().saveWalletBalance(requireActivity(),number.toString())
            binding.tvGreet.text =
                "${resources.getString(R.string.dashboard_greetings)} ${userData.firstName}"
        }catch (ex:Exception){
            LoggerHelper.loggerSuccess("biresponse", "bi response error occurring ")
        }

    }



    private fun populatedashboard() {

        val number = UtilPreference().getWalletBalance(requireActivity()).toInt()
        val COUNTRY = "CI"//CI
        val LANGUAGE = "fr"//fr
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE,COUNTRY)).format(number)
        binding.tvAvailableBal.text =" ${str.toString()}"
        try {
            var expenditure = number / 4
            val exps = NumberFormat.getCurrencyInstance(Locale(LANGUAGE,COUNTRY)).format(expenditure)
            binding.tvActualBal.text = " ${exps.toString()}"
        }catch (ex:Exception){}
        //UtillPreference().saveWalletBalance(requireActivity(),number.toString())
        binding.tvGreet.text = "${resources.getString(R.string.dashboard_greetings)} ${userData.firstName}"
    }




    private suspend fun sendFarmregRequest() {
        viewModel.getFarmerTransaction().observe(requireActivity(),androidx.lifecycle.Observer{
            //pDialog.dismiss()
            when(it){
                is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
            }
        })

    }
    fun dummyTransactioList(){
    /*    var transList = DemoDummyData.getSampleTransaction()
        var productFilterSort = transList.toMutableList()
            .sortedByDescending { vanOrderList -> vanOrderList.syncDate }
        val adapter = CollectionsAdapter(CollectionsListener { collection ->
            // navigateToCollection(collection)
        },productFilterSort)

        binding.rvTransactins.adapter = adapter*/
    }

    private fun processRequest(response: String) {
        LoggerHelper.loggerError("processTrans", "trans $response")
/*//FarmerTransaction
       var farmersList:List<FarmerTransaction> = NetworkUtility.jsonResponse(response)
        var productFilterSort = farmersList.toMutableList()
            .sortedByDescending { vanOrderList -> vanOrderList.syncDate }
        val adapter = CollectionsAdapter(CollectionsListener { collection ->
            // navigateToCollection(collection)
        },productFilterSort)

        binding.rvTransactins.adapter = adapter*/
       // rcvRecentDisbursment.adapter  = collectionAdapter
    }
}