package io.eclectics.cargilldigital.ui.generalwalletprofile.statement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.adapter.MinistatementAdapter
import io.eclectics.cargilldigital.adapter.RecentTransactionsAdapter
import io.eclectics.cargilldigital.adapter.TransactionListener
import io.eclectics.cargilldigital.databinding.FragmentMiniBinding
import io.eclectics.cargilldigital.data.model.MiniStatement
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargill.model.RecentTransaction
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class MiniFragment : Fragment() {
    private var _binding: FragmentMiniBinding? = null
    private val binding get() = _binding!!
    lateinit var adapterMini:MinistatementAdapter
    lateinit var userData: UserDetailsObj
    val genViewModel: GeneralViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_mini, container, false)
        _binding = FragmentMiniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
         userData = NetworkUtility.jsonResponse(userJson)
        dummyMiniAdapter()
    }

   /* private fun dummyMiniAdapter() {
        var miniArrayList = ArrayList<MiniStatement>()
        var mini1 = MiniStatement("message","phonenumber")
        miniArrayList.add(mini1)
        val adapter = MinistatementAdapter(TransactionListener { collection ->

        },miniArrayList)
        binding.rvTransactins.adapter = adapter
    }*/

    private fun dummyMiniAdapter() {
        var userJson  = UtilPreference().getUserData(activity)
        var userData:UserDetailsObj = NetworkUtility.jsonResponse(userJson)
        // var list = DemoDummyData.getSampleTransaction()
        var listArray = userData.getTransactionlist()
        var jsonarray = JSONArray(listArray.toString())
        var stringlabe = JSONArray(listArray.toString())
        val jArray = stringlabe//listArray as JSONArray?
        val listdata = java.util.ArrayList<String>()
        if (jArray != null) {
            for (i in 0 until jArray.length()) {
                // transArrayList.add(trans)
                listdata.add(jArray.getString(i))
            }
        }
        if (listdata.isNotEmpty()) {
            var transList: List<MiniStatement> =
                NetworkUtility.jsonResponse(listdata.toString())

            val adapter = MinistatementAdapter(TransactionListener { collection ->

            },transList)
            binding.rvTransactins.adapter = adapter
        } else {
            binding.tvErrorResponse.visibility = View.VISIBLE
            binding.rvTransactins.visibility = View.GONE
        }
        //get online recent transaction
        CoroutineScope(Dispatchers.Main).launch() {
            getRecentTransaction()
        }
    }
    suspend fun getRecentTransaction() {
        var lookupJson = JSONObject()
        var endpoint  =  ApiEndpointObj.recentTransactions
        lookupJson.put("userId",userData.providedUserId)
        lookupJson.put("phoneNumber",userData.phoneNumber)
//getFarmerWalletBalance
        genViewModel.getRecentTransactions(lookupJson,endpoint,requireActivity()).observe(requireActivity(),androidx.lifecycle.Observer{
            // pDialog.dismiss()
            when(it){
                is ViewModelWrapper.error -> {
                    LoggerHelper.loggerError("error","error ${it.error}")}//GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                is ViewModelWrapper.response -> processFloatBalance(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
            }
        })
    }

    private fun processFloatBalance(floatResp: String) {
        //FloatCallBack
        //var balance = UtillPreference().getWalletBalance(requireContext())
        LoggerHelper.loggerError("respobnse","resp $floatResp")
       // farmerListJson = floatResp
        var listdata:List<RecentTransaction> = NetworkUtility.jsonResponse(floatResp)
        if(listdata.isNotEmpty()) {

            val adapter = RecentTransactionsAdapter(TransactionListener { collection ->

            }, listdata)
            binding.rvTransactins.adapter = adapter
        }



    }
}