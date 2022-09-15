package io.eclectics.cargilldigital.ui.buyerprofile.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.adapter.CollectionsAdapter
import io.eclectics.cargilldigital.adapter.RecentTransactionsAdapter
import io.eclectics.cargilldigital.adapter.TransactionListener
import io.eclectics.cargilldigital.adapter.TransactionsAdapter
import io.eclectics.cargilldigital.databinding.FragmentBuyerRecentTranactionBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargill.model.FarmerTransaction
import io.eclectics.cargill.model.RecentTransaction
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargill.viewmodel.FarmViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import javax.inject.Inject


@AndroidEntryPoint
class BuyerRecentTranaction : Fragment() {
   private var _binding:FragmentBuyerRecentTranactionBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navOptions: NavOptions
    val viewModel: FarmViewModel by viewModels()
    lateinit var collectionAdapter: CollectionsAdapter
    lateinit var userData: UserDetailsObj
    //delete ths
    val genViewModel: GeneralViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_buyer_recent_tranaction, container, false)
        _binding = FragmentBuyerRecentTranactionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        var rcvRecentDisbursment = binding.rvTransactins
        buyerTransactionList()
    }
    private fun buyerTransactionList() {
        // var list = DemoDummyData.getSampleTransaction()
        var listArray = userData.getTransactionlist()
        var jsonarray = JSONArray(listArray.toString())
        var stringlabe = JSONArray(listArray.toString())
        val listdata = ArrayList<String>()
        val jArray = stringlabe//listArray as JSONArray?
        var transArrayList = ArrayList<FarmerTransaction>()

        if (jArray != null) {
            for (i in 0 until jArray.length()) {
                var trans: FarmerTransaction = NetworkUtility.jsonResponse(jArray.getString(i))
                LoggerHelper.loggerError("list",jArray.getString(i))
                // transArrayList.add(trans)
                listdata.add(jArray.getString(i))
            }
        }
        if(listdata.isNotEmpty()) {
            var transList: List<FarmerTransaction> =
                NetworkUtility.jsonResponse(listdata.toString())

       /*     var productBrandList =
                transList.distinctBy { ProductListWorkwithback -> ProductListWorkwithback.amount }*/
            var productFilterSort = transList.toMutableList()
                .sortedByDescending { vanOrderList -> vanOrderList.datetime }
            val adapter = TransactionsAdapter(TransactionListener { collection ->

            }, productFilterSort)
            binding.rvTransactins.adapter = adapter
            binding.tvErrorResponse.visibility = View.GONE
            binding.rvTransactins.visibility = View.VISIBLE
        }
        else{
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvTransactins.visibility = View.GONE
        }
        //LoggerHelper.loggerError("lstdata","lst data ${listdata.get(0)}")
        //get online recent transaction
         lifecycleScope.launch {
            getRecentTransaction()
        }


    }
    suspend fun getRecentTransaction() {
        try {
            var lookupJson = JSONObject()
            var endpoint = ApiEndpointObj.recentTransactions
            lookupJson.put("userId", userData.providedUserId)
            lookupJson.put("phoneNumber", userData.phoneNumber)
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
        LoggerHelper.loggerError("respobnse","resp $floatResp")
        var listdata:List<RecentTransaction> = NetworkUtility.jsonResponse(floatResp)
        if(listdata.isNotEmpty()) {
            var productFilterSort = listdata.toMutableList()
                .sortedByDescending { vanOrderList -> vanOrderList.datecreated }
            val adapter = RecentTransactionsAdapter(TransactionListener { collection ->

            }, listdata)
            binding.rvTransactins.adapter = adapter
        }



    }
}