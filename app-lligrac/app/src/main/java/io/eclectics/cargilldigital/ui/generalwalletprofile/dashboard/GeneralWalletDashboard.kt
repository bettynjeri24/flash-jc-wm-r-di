package io.eclectics.cargilldigital.ui.generalwalletprofile.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.TransactionListener
import io.eclectics.cargilldigital.adapter.TransactionsAdapter
import io.eclectics.cargilldigital.databinding.FragmentOtherUsersDashboardBinding
import io.eclectics.cargilldigital.data.model.Section
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargill.model.FarmerTransaction
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class GeneralWalletDashboard : Fragment() {
    private var _binding: FragmentOtherUsersDashboardBinding? = null
    private val binding get() = _binding!!
    lateinit var userData: UserDetailsObj
    @Inject
    lateinit var navOptions:NavOptions
    val genViewModel: GeneralViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOtherUsersDashboardBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        UtilPreference().saveActiveprofile(requireActivity(),R.id.nav_generalWalletProfile)
        UtilPreference().setActiveDashbaord(requireActivity(),R.id.action_genUserProfile)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        setDummmyWalletbalance()
        farmerDummyData()
        binding.toolbar.ivProfile.setOnClickListener {
            (activity as MainActivity?)!!.openDrawer()
        }
        binding.tvAvailableBal.setOnClickListener {
             lifecycleScope.launch {
                // getFarmerWalletBalance()
                makeSampleLogin()
            }
        }
        /*var adapter = GeneralWalletRecentTransactionAdapter()
        binding.rvTransactins.adapter =  adapter*/
        binding.toolbar.ivNotification.setOnClickListener {
            //findNavController().navigate(R.id.nav_selectAccount)
            (activity as MainActivity?)!!.navigateToAccounts()
        }
        //nav_coopBuyerList
        binding.clTransferMoney.setOnClickListener {
            findNavController().navigate(R.id.nav_gwalletAccList,null,navOptions)
        }

        binding.clRequest.setOnClickListener {
            findNavController().navigate(R.id.nav_statementFragment,null,navOptions)
        }
        binding.tvViewAll.setOnClickListener {
            findNavController().navigate(R.id.nav_eValueAnalysis,null,navOptions)
        }
        binding.clMyaccount.setOnClickListener {
            findNavController().navigate(R.id.nav_generalWalletLinkedAcc,null,navOptions)
        }
    }

    private fun setDummmyWalletbalance() {

        val number = UtilPreference().getWalletBalance(requireActivity()).toInt()
        val COUNTRY = "CI"//CI
        val LANGUAGE = "fr"//fr
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE,COUNTRY)).format(number)
        binding.tvAvailableBal.text =" ${str.toString()}"
        //binding.tvActualBal.text = " ${str.toString()}"
        try {
            var expenditure = number / 4
            val exps = NumberFormat.getCurrencyInstance(Locale(LANGUAGE,COUNTRY)).format(expenditure)
            binding.tvActualBal.text = " ${exps.toString()}"
        }catch (ex:Exception){}
        //UtillPreference().saveWalletBalance(requireActivity(),number.toString())
        binding.tvGreet.text = "${resources.getString(R.string.dashboard_greetings)} ${userData.firstName}"
    }

    private fun makeSampleLogin() {
        var endpoint  =  ApiEndpointObj.userLogin
        var lookupJson = JSONObject()
//TODO SAMPLE CREDS 15/04
        var creds =   UtilPreference().getUserCreds(requireActivity())
        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        lookupJson.put("pin",creds)
        lookupJson.put("phonenumber",userData.phoneNumber)
         lifecycleScope.launch {
            // pdialog.show()
            genViewModel.phoneLookupRequest(lookupJson,endpoint,requireActivity()).observe(requireActivity(), androidx.lifecycle.Observer {
                // pdialog.dismiss()
                when(it){
                    is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    //processRequest(it.value)//
                }
            })
        }

    }


    private fun processRequest(response: String) {
//nav_selectAccount
        LoggerHelper.loggerError("offlineproduct", "response test loop")

        LoggerHelper.loggerSuccess("response", "product response ${response}")
        //save to room database
        var dummySection = Section(1,"dummy","dummy","dummy")
        var json = NetworkUtility.getJsonParser().toJson(dummySection)
        var jsonResponse = JSONObject(response)
        jsonResponse.put("section",JSONObject(json.toString()))
        //var lookupresponse:OtherUserObj = NetworkUtility.jsonResponse(response)
       var loginResponse2 = jsonResponse.toString()
        var lookupresponse: UserDetailsObj = NetworkUtility.jsonResponse(loginResponse2)
        UtilPreference().setUserData(requireActivity(),loginResponse2)
        var data = UtilPreference().getUserData(requireActivity())
        userData = NetworkUtility.jsonResponse(data)
        setDummmyWalletbalance()
        farmerDummyData()
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
    }
}