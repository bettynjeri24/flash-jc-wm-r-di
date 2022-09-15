package io.eclectics.cargilldigital.ui.cooperativeprofile.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.RecentTransactionsAdapter
import io.eclectics.cargilldigital.adapter.TransactionListener
import io.eclectics.cargilldigital.adapter.TransactionsAdapter
import io.eclectics.cargilldigital.databinding.FragmentCooperativeDashboardBinding
import io.eclectics.cargilldigital.data.model.CoopBank
import io.eclectics.cargilldigital.data.model.Section
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.data.model.UserLogginData
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargill.model.FarmerTransaction
import io.eclectics.cargill.model.RecentTransaction
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CooperativeDashboard : Fragment() {
    private var _binding: FragmentCooperativeDashboardBinding? = null
    private val binding get() = _binding!!
    lateinit var userData: UserDetailsObj
    lateinit var onBackPressedCallback: OnBackPressedCallback
    @Inject
    lateinit var navOptions: NavOptions
    val genViewModel: GeneralViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCooperativeDashboardBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        UtilPreference().saveActiveprofile(requireActivity(),R.id.nav_cooperativeProfile)
        UtilPreference().setActiveDashbaord(requireActivity(),R.id.action_coopProfile)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        setDummmyWalletbalance()
        simulateLogin()
        farmerDummyData()


        binding.toolbar.ivProfile.setOnClickListener {
            (activity as MainActivity?)!!.openDrawer()
        }

        binding.tvAvailableBal.setOnClickListener {
             lifecycleScope.launch {
                // getFarmerWalletBalance()
                getWalletAPiBalance()
            }
        }
     /*   var adapter = CoopDisbursmentAdapter()
        binding.rvTransactins.adapter =  adapter*/
        binding.toolbar.ivNotification.setOnClickListener {
            //findNavController().navigate(R.id.nav_selectAccount)
            (activity as MainActivity?)!!.navigateToAccounts()
        }
        //nav_coopBuyerList
        binding.clBookEvalue.setOnClickListener {
            findNavController().navigate(R.id.nav_evalueBookingList)
        }
        binding.clBuyersList.setOnClickListener {
            findNavController().navigate(R.id.nav_coopBuyerList)
        }
        binding.clRequest.setOnClickListener {
            findNavController().navigate(R.id.nav_coopBuyerFundRequest)
        }
    binding.tvViewAll.setOnClickListener {
        findNavController().navigate(R.id.nav_eValueAnalysis)
    }

        onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                // NetworkUtility().transactionWarning("You wantto exit?",requireActivity())
                findNavController().navigate(R.id.action_logout)
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,onBackPressedCallback)
    }

    private fun simulateLogin() {
        var endpoint  =  ApiEndpointObj.userLogin
        var lookupJson = JSONObject()

        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        lookupJson.put("pin","1234")
        lookupJson.put("phonenumber","2250594851583")//phoneNumber
        //TODO MUST DELETE AFTER 15/04
        //UtillPreference().setUserCreds(requireActivity(),binding.etPassword.text.toString())
         lifecycleScope.launch {
           // pdialog.show()
            genViewModel.sendLoginRequest(lookupJson,endpoint,requireActivity()).observe(requireActivity(), Observer {
              //  pdialog.dismiss()
                when(it){
                    is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processRequestLogin(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    //processRequest(it.value)//
                }
            })
        }
    }

    private fun processRequestLogin(response: String) {
       // var lookupresponse: UserDetailsObj
        try {
//nav_selectAccount
            LoggerHelper.loggerError("offlineproduct", "response test loop")

            LoggerHelper.loggerSuccess("response", "product response ${response}")
            lateinit var loginResponse: String
            loginResponse = response
            var profileStr = JSONObject(response).getString("role")
            var dummySection = Section(1, "dummy", "dummy", "dummy")
            var json = NetworkUtility.getJsonParser().toJson(dummySection)
            var dummyBank = CoopBank(1, "pkau", "oak", "oak", 1)
            var bankJson = NetworkUtility.getJsonParser().toJson(dummyBank)
            lateinit var userLoginDataObj: UserLogginData
            var lookupresponse: UserDetailsObj
            when (profileStr) {
                "Cooperative" -> {
                    var jsonResponse = JSONObject(response)
                    jsonResponse.put("section", JSONObject(json.toString()))
                    // jsonResponse.put("bankAccount",JSONObject(bankJson.toString()))
                    //jsonResponse.put("bankAccount",JSONObject())
                    //var lookupresponse:OtherUserObj = NetworkUtility.jsonResponse(response)
                    loginResponse = jsonResponse.toString()
                    lookupresponse = NetworkUtility.jsonResponse(jsonResponse.toString())
                    UtilPreference().saveWalletBalance(
                        requireActivity(),
                        lookupresponse.walletBalance
                    )
                    setDummmyWalletbalance()
                }
            }
        }catch (ex:Exception){}
        //loginResponse = response
       // var lookupresponse: UserDetailsObj  = NetworkUtility.jsonResponse(value)

    }

    fun setDummmyWalletbalance(){
        val number = UtilPreference().getWalletBalance(requireActivity()).toInt()
        val COUNTRY = "CI"//CI
        val LANGUAGE = "fr"//fr
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE,COUNTRY)).format(number)
        binding.tvAvailableBal.text =" ${str.toString()}"
       // binding.tvActualBal.text = " ${str.toString()}"
        //UtillPreference().saveWalletBalance(requireActivity(),number.toString())
        binding.tvGreet.text = "${resources.getString(R.string.dashboard_greetings)} ${userData.firstName}"
    }

    private fun getWalletAPiBalance() {
        try {
            var endpoint = ApiEndpointObj.walletBalanceEnquiry
            var lookupJson = JSONObject()
            lookupJson.put("coopIndex", userData.cooperativeIndex)
            lookupJson.put("phonenumber", userData.phoneNumber)
             lifecycleScope.launch {
                // pdialog.show()
                genViewModel.generalPinRequest(lookupJson, endpoint, requireActivity())
                    .observe(requireActivity(), androidx.lifecycle.Observer {
                        // pdialog.dismiss()
                        when (it) {
                            is ViewModelWrapper.error -> GlobalMethods().transactionWarning(
                                requireActivity(),
                                "${it.error}"
                            )//LoggerHelper.loggerError("error","error")
                            is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                            //processRequest(it.value)//
                        }
                    })
            }
        }catch (ex:Exception){}

    }
    private fun processRequest(response: String) {
//nav_selectAccount
        LoggerHelper.loggerError("offlineproduct", "response test loop")

        LoggerHelper.loggerSuccess("response", "product response ${response}")
        //save to room database
        UtilPreference().saveWalletBalance(requireActivity(),response)
        setDummmyWalletbalance()
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

            val adapter = TransactionsAdapter(TransactionListener { collection ->

            }, transList)
            binding.rvTransactins.adapter = adapter
        } else {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvTransactins.visibility = View.GONE
        }
        CoroutineScope(Dispatchers.Main).launch() {
            getRecentTransaction()
        }
    }

    suspend fun getRecentTransaction() {
        try {
            var lookupJson = JSONObject()
            var endpoint = ApiEndpointObj.recentTransactions
            lookupJson.put("userId", userData.providedUserId)
            lookupJson.put("phoneNumber","2250703035850" )//
//getFarmerWalletBalance
            genViewModel.getRecentTransactions(lookupJson, endpoint, requireActivity())
                .observe(requireActivity(), androidx.lifecycle.Observer {
                    // pDialog.dismiss()  http://102.37.14.127:5000/auth/mobileapplogin
                    when (it) {
                        is ViewModelWrapper.error -> {
                            LoggerHelper.loggerError("error", "error ${it.error}")
                        }//GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                        is ViewModelWrapper.response -> processFloatBalance(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    }
                })
        }catch (ex:Exception){}
    }

    private fun processFloatBalance(floatResp: String) {
        //FloatCallBack
        //var balance = UtillPreference().getWalletBalance(requireContext())
        LoggerHelper.loggerError("respobnse", "resp $floatResp")
        var listdata: List<RecentTransaction> = NetworkUtility.jsonResponse(floatResp)
        if (listdata.isNotEmpty()) {

            val adapter = RecentTransactionsAdapter(TransactionListener { collection ->

            }, listdata)
            binding.rvTransactins.adapter = adapter
            binding.tvErrorResponse.visibility = View.GONE
            binding.rvTransactins.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedCallback.remove()
    }

    }