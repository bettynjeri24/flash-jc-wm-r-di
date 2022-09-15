package io.eclectics.cargilldigital.ui.farmerprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.SendMoneyAccListAdapter
import io.eclectics.cargilldigital.databinding.FragmentSendMoneyAccountListBinding
import io.eclectics.cargilldigital.data.model.FarmerAccount
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.FarmerRoomViewModel
import io.eclectics.cargilldigital.viewmodel.FarmerViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class SendMoneyAccountList : Fragment() {
    private var _binding: FragmentSendMoneyAccountListBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter:SendMoneyAccListAdapter
    lateinit var lookupJson:JSONObject
    @Inject
    lateinit var pdialog: SweetAlertDialog
    @Inject
    lateinit var navOptions:NavOptions
    val farmViewModel: FarmerViewModel by viewModels()
    val farmerRoomViewModel:FarmerRoomViewModel by viewModels()
    lateinit var userData:UserDetailsObj
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSendMoneyAccountListBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.transfer_money_toolbar),resources.getString(R.string.transfer_money_tsubttle),binding.mainLayoutToolbar,requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userdatajson = UtilPreference().getUserData(requireActivity())
         userData = NetworkUtility.jsonResponse(userdatajson)
            populateWalletDetails()
            getAccountList()
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        //handle on wallte to wallet transfer
        binding.cardWallet2Wallet.setOnClickListener {
            findNavController().navigate(R.id.nav_transferToCargillWallet,null,navOptions)
        }

    }

    private fun populateWalletDetails() {
        var maskedPhoneNumber = GlobalMethods().simpleMasking(userData.phoneNumber!!,2,3)
        binding.tvPhoneNumber.text = "Acc - $maskedPhoneNumber"
        binding.tvCreationDate.text = "${resources.getString(R.string.from)}: ${userData.firstName} ${userData.lastName}"
    }

    private fun getAccountList() {
        try {
            var endpoint = ApiEndpointObj.myCashoutChannels
            lookupJson = JSONObject()
            lookupJson.put("userId", userData.providedUserId)
            lookupJson.put("phoneNumber", userData.phoneNumber)
            lookupJson.put("channelType", "telco")
             lifecycleScope.launch {
                pdialog.show()
                farmViewModel.getAddedBenefiaryAccount(lookupJson, endpoint, requireActivity())
                    .observe(requireActivity(), Observer {
                        pdialog.dismiss()
                        when (it) {
                            is ViewModelWrapper.error -> GlobalMethods().transactionWarning(
                                requireActivity(),
                                "${it.error}"
                            )
                            is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                        }
                    })
            }
        }catch (ex:Exception){}
        }

    private fun processRequest(response: String) {
        LoggerHelper.loggerSuccess("response","send money  ${response}")
        var accList: List<FarmerAccount.BeneficiaryAccObj> = NetworkUtility.jsonResponse(response)
        if(accList.isNotEmpty()) {
         adapter = SendMoneyAccListAdapter(SendMoneyAccListAdapter.AccountListener { farmer, action ->
            handleAction(farmer, action)
        },accList)

        binding.rcvBeneficialyAcc.adapter = adapter
            binding.rcvBeneficialyAcc.adapter = adapter
            binding.tvErrorResponse.visibility= View.GONE
            binding.rcvBeneficialyAcc.visibility = View.VISIBLE
             lifecycleScope.launch {
                //insertBeneficiaryAccList
                farmerRoomViewModel.insertBeneficiaryAccList(requireActivity(),response)
            }


        }else{
            binding.tvErrorResponse.visibility= View.VISIBLE
            binding.rcvBeneficialyAcc.visibility = View.GONE
        }
    }


    private fun handleAction(account: FarmerAccount.BeneficiaryAccObj, action: String) {
        var accountJson = NetworkUtility.getJsonParser().toJson(account)
        var bundle = Bundle()
        bundle.putString("account",accountJson)
        when(account.channelType){
            "Telco" -> findNavController().navigate(R.id.nav_TransferToTelco,bundle)
            "Aggregator" -> findNavController().navigate(R.id.nav_transferToWallet,bundle)
            "Bank" -> findNavController().navigate(R.id.nav_TransferToBank,bundle)
           // 4 -> findNavController().navigate(R.id.nav_TransferToBank,bundle)
           // 5-> findNavController().navigate(R.id.nav_transferToWallet,bundle)
            "Card"-> findNavController().navigate(R.id.nav_transferToCard,bundle)
        }

    }
}