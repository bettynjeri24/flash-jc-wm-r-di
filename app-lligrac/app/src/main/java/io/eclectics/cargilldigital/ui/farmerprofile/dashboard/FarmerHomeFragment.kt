package io.eclectics.cargilldigital.ui.farmerprofile.dashboard

import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.RecentTransactionsAdapter
import io.eclectics.cargilldigital.adapter.TransactionListener
import io.eclectics.cargilldigital.adapter.TransactionsAdapter
import io.eclectics.cargilldigital.databinding.FarmerHomeFragmentBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.FarmerViewModel
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargill.model.FarmerTransaction
import io.eclectics.cargill.model.RecentTransaction
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargill.viewmodel.AgentViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class FarmerHomeFragment : Fragment() {

    //private lateinit var viewModel: FarmerHomeViewModel

    private var _binding: FarmerHomeFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var containerGrp: ViewGroup
    private var show = false
    val farmerViewModel: FarmerViewModel by viewModels() // appropriate view model
    lateinit var agentViewModel: AgentViewModel
    lateinit var pDialog: SweetAlertDialog
    lateinit var menuPassed: String
    lateinit var onBackPressedCallback: OnBackPressedCallback
    lateinit var farmerListJson: String
    lateinit var farmersList: List<FarmerTransaction>
    lateinit var userData: UserDetailsObj

    @Inject
    lateinit var navOptions: NavOptions

    //delete ths
    val genViewModel: GeneralViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var userJson = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FarmerHomeFragmentBinding.inflate(inflater, container, false)
        // farmViewModel = ViewModelProvider(requireActivity()).get(FarmViewModel::class.java)
        pDialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        (activity as MainActivity?)!!.hideToolbar()
        agentViewModel = ViewModelProvider(requireActivity()).get(AgentViewModel::class.java)
        show = true
        containerGrp = container!!
        animate(container!!, binding)

        binding.toolbar.ivProfile.setOnClickListener {
            (activity as MainActivity?)!!.openDrawer()
        }
        binding.toolbar.ivNotification.setOnClickListener {
            //findNavController().popBackStack(R.id.nav_selectAccount,false)
            (activity as MainActivity?)!!.navigateToAccounts()
        }
        UtilPreference().saveActiveprofile(requireActivity(), R.id.nav_farmerDashboard)
        UtilPreference().setActiveDashbaord(requireActivity(), R.id.action_farmerdashboard)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // populatedashboard()  tv_available_bal
        binding.tvAvailableBal.setOnClickListener {
            lifecycleScope.launch {
                // getFarmerWalletBalance()
                getWalletAPiBalance()
            }
        }

        binding.clTransferMoney.setOnClickListener {
            findNavController().navigate(R.id.nav_SendMoneyAccountList)//sendMoneyMenu
        }
        binding.clMyAccount.setOnClickListener {
            findNavController().navigate(R.id.nav_farmerBeneficiaryAccount)
        }

        binding.clStatements.setOnClickListener {
            findNavController().navigate(R.id.nav_statementFragment, null, navOptions)
        }
        lifecycleScope.launch {
            // NetworkUtility().sendRequest(pDialog)
            getWalletAPiBalance()
            getPreferenceBalance()
            //setDummmyWalletbalance()
            farmerDummyData()
            //sendFarmregRequest()
            binding.tvViewAll.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("recenttrans", farmerListJson)
                findNavController().navigate(R.id.nav_morecentTransaction, bundle)
            }
        }
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // NetworkUtility().transactionWarning("You wantto exit?",requireActivity())
                findNavController().navigate(R.id.action_logout)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }


    private fun getPreferenceBalance() {
        val number = UtilPreference().getWalletBalance(requireActivity()).toInt()
        val COUNTRY = "CI"//CI
        val LANGUAGE = "fr"//fr
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
        binding.tvAvailableBal.text = " ${str.toString()}"
        try {
            var expenditure = number / 4
            val exps =
                NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(expenditure)
            binding.tvTotalMonthlyCashOut.text = " ${exps.toString()}"
        } catch (ex: Exception) {
        }

        //UtillPreference().saveWalletBalance(requireActivity(),number.toString())
        binding.tvGreet.text =
            "${resources.getString(R.string.dashboard_greetings)} ${userData.firstName}"
        //load wallet balance form api

    }


    private fun getWalletAPiBalance() {
        var endpoint = ApiEndpointObj.walletBalanceEnquiry
        var lookupJson = JSONObject()
        lookupJson.put("coopIndex", userData.cooperativeIndex)
        lookupJson.put("phonenumber", userData.phoneNumber)
        lifecycleScope.launch {
            // pdialog.show()
            genViewModel.walletBalanceRequest(lookupJson, endpoint, requireActivity())
                .observe(viewLifecycleOwner, Observer {
                    // pdialog.dismiss()
                    when (it) {
                        is ViewModelWrapper.error -> LoggerHelper.loggerError(
                            "nonet",
                            "no internet"
                        )//GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                        is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                        //processRequest(it.value)//
                    }
                })
        }

    }


    private fun processRequest(response: String) {
//nav_selectAccount
        LoggerHelper.loggerError("waalletbalance", "wallet balance")

        LoggerHelper.loggerSuccess("responseBal", "Balance response ${response}")
        //save to room database
        UtilPreference().saveWalletBalance(requireActivity(), response)
        val number = response.toInt()
        val COUNTRY = "CI"//CI
        val LANGUAGE = "fr"//fr
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
        binding.tvAvailableBal.text = " ${str.toString()}"
        try {
            var expenditure = number / 4
            val exps =
                NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(expenditure)
            binding.tvTotalMonthlyCashOut.text = " ${exps.toString()}"
        } catch (ex: Exception) {
        }
        //UtillPreference().saveWalletBalance(requireActivity(),number.toString())
        binding.tvGreet.text =
            "${resources.getString(R.string.dashboard_greetings)} ${userData.firstName}"

    }

    private fun farmerDummyData() {
        // var list = DemoDummyData.getSampleTransaction()
        var listArray = userData.getTransactionlist()
        var jsonarray = JSONArray(listArray.toString())
        var stringlabe = JSONArray(listArray.toString())
        val jArray = stringlabe//listArray as JSONArray?
        var transArrayList = ArrayList<FarmerTransaction>()
        val listdata = ArrayList<String>()
        if (jArray != null) {
            for (i in 0 until jArray.length()) {
                var trans: FarmerTransaction = NetworkUtility.jsonResponse(jArray.getString(i))
                LoggerHelper.loggerError("list", jArray.getString(i))
                // transArrayList.add(trans)
                listdata.add(jArray.getString(i))
            }
        }
        if (listdata.isNotEmpty()) {
            var transList: List<FarmerTransaction> =
                NetworkUtility.jsonResponse(listdata.toString())
            var productFilterSort = transList.toMutableList()
                .sortedByDescending { vanOrderList -> vanOrderList.datetime }
            val adapter = TransactionsAdapter(TransactionListener { collection ->

            }, productFilterSort)
            farmerListJson = listdata.toString()
            binding.rvTransactins.adapter = adapter
        } else {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvTransactins.visibility = View.GONE
        }

        //get online recent transaction
        lifecycleScope.launch {
            getRecentTransaction()
        }

    }

    suspend fun getRecentTransaction() {
        val lookupJson = JSONObject()
        val endpoint = ApiEndpointObj.recentTransactions
        lookupJson.put("userId", userData.providedUserId)
        lookupJson.put("phoneNumber", userData.phoneNumber)
//getFarmerWalletBalance
        genViewModel.getRecentTransactions(lookupJson, endpoint, requireActivity())
            .observe(requireActivity(), androidx.lifecycle.Observer {
                // pDialog.dismiss()
                when (it) {
                    is ViewModelWrapper.error -> {
                        LoggerHelper.loggerError("error", "error ${it.error}")
                    }//GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processFloatBalance(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                }
            })
    }


    private fun showPaybillMenu() {
        binding.rcvPaybills.visibility = View.VISIBLE
        binding.rvTransactins.visibility = View.GONE
        binding.rcvFTMenu.visibility = View.GONE
        animate(containerGrp, binding)
    }

    private fun displayPassedMenu(menuPassed: String) {
        when (menuPassed) {
            "sendmoney" -> sendMoneyTitle()
            "paybill" -> showPaybillMenu()
        }
    }

    fun sendMoneyTitle() {
        //showToast()
        binding.rcvPaybills.visibility = View.GONE
        binding.rvTransactins.visibility = View.GONE
        binding.rcvFTMenu.visibility = View.VISIBLE
    }


    private fun processFloatBalance(floatResp: String) {
        //FloatCallBack
        //var balance = UtillPreference().getWalletBalance(requireContext())
        LoggerHelper.loggerError("response", "resp $floatResp")
        farmerListJson = floatResp
        val listdata: List<RecentTransaction> = NetworkUtility.jsonResponse(floatResp)
        if (listdata.isNotEmpty()) {
            var productFilterSort = listdata.toMutableList()
                .sortedByDescending { vanOrderList -> vanOrderList.datecreated }
            val adapter = RecentTransactionsAdapter(TransactionListener { collection ->

            }, productFilterSort)
            binding.rvTransactins.adapter = adapter
        }


    }

    private fun animate(parent: ViewGroup, binding: FarmerHomeFragmentBinding) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 1500
        transition.addTarget(binding.clTransaction)

        TransitionManager.beginDelayedTransition(parent, transition)
        binding.clTransaction.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showToast() {
        Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
    }

    private suspend fun sendFarmregRequest() {
        /* farmViewModel.getFarmerTransaction().observe(requireActivity(),androidx.lifecycle.Observer{
             pDialog.dismiss()
             when(it){
                 is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                 is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
             }
         })*/

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedCallback.remove()
    }

    /* private fun processRequest(response: String) {
         LoggerHelper.loggerError("processTrans","trans $response")
 //FarmerTransaction
         farmersList = NetworkUtility.jsonResponse(response)
         var productFilterSort = farmersList.toMutableList()
             .sortedByDescending { vanOrderList -> vanOrderList.syncDate }
         val adapter = TransactionsAdapter(TransactionListener { collection ->

         },productFilterSort)
         binding.rvTransactins.adapter = adapter
         *//*var adapter = FarmersAdapter(FarmerListener { farmer, action ->
            handleAction(farmer, action)
        })*//*

    }*/
}