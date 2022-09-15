package io.eclectics.cargilldigital.ui.generalwalletprofile.gwalletaccounts

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
import io.eclectics.cargilldigital.adapter.BeneficiaryAccAdapter
import io.eclectics.cargilldigital.databinding.FragmentGwalletLinkedAccountsBinding
import io.eclectics.cargilldigital.data.model.*
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.FarmerViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class GwalletLinkedAccounts : Fragment() {
    lateinit var userData: OtherUserObj
    lateinit var selectedChannel: SendMoney.ChannelListObj
    @Inject
    lateinit var navOptions: NavOptions
    val farmViewModel: FarmerViewModel by viewModels()
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var beneficiaryAdapter: BeneficiaryAccAdapter
    private var _binding: FragmentGwalletLinkedAccountsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_gwallet_linked_accounts, container, false)
        _binding = FragmentGwalletLinkedAccountsBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.setToolbarTitle(resources.getString(R.string.my_accounts),resources.getString(R.string.mt_account_tsubbtle))

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddMore.setOnClickListener {
            findNavController().navigate(R.id.nav_addBeneficiaryAccount)
        }
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        //val adapter = BeneficiaryAccAdapter()
        var accountList = FarmerAccount.getFarmerbeneficiary()
        getLinkedBeneficiaryAcc()

    }
    private fun getLinkedBeneficiaryAcc() {
        var endpoint  =  ApiEndpointObj.myCashoutChannels
        var lookupJson = JSONObject()
        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        lookupJson.put("userId",userData.providedUserId)
        lookupJson.put("phoneNumber",userData.phoneNumber)
         lifecycleScope.launch {
            pdialog.show()
            farmViewModel.getAddedBenefiaryAccount(lookupJson,endpoint,requireActivity()).observe(requireActivity(), Observer {
                pdialog.dismiss()
                when(it){
                    is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processChannelRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    //processRequest(it.value)//
                }
            })
        }
    }

    private fun processChannelRequest(response: String) {
        var accList: List<FarmerAccount.BeneficiaryAccObj> = NetworkUtility.jsonResponse(response)
        if(accList.isNotEmpty()) {
            val adapter =
                BeneficiaryAccAdapter(BeneficiaryAccAdapter.AccountListener { farmer, action ->
                    handleAction(farmer, action)
                }, accList)
            binding.rcvBeneficialyAcc.adapter = adapter
            binding.tvErrorResponse.visibility= View.GONE
            binding.rcvBeneficialyAcc.visibility = View.VISIBLE
        }else{
            binding.tvErrorResponse.visibility= View.VISIBLE
            binding.rcvBeneficialyAcc.visibility = View.GONE
        }
    }

    private fun handleAction(accBeneficiary: FarmerAccount.BeneficiaryAccObj, action: String) {
        when(action){
            "removeAcc" ->{
                removeBeneficiaryAccount(accBeneficiary)
            }
        }
    }

    private fun removeBeneficiaryAccount(accBeneficiary: FarmerAccount.BeneficiaryAccObj) {

        var endpoint  =  ApiEndpointObj.removeBeneficiaryAcc
        var lookupJson = JSONObject()
        //lookupJson.put("phonenumber",requireArguments().getString("phone")) accountholderphonenumber
        lookupJson.put("userId",userData.providedUserId)
        lookupJson.put("accountholderphonenumber",accBeneficiary.accountholderphonenumber)
        lookupJson.put("phoneNumber",userData.phoneNumber)
        lookupJson.put("beneficiaryaccount",accBeneficiary.channelNumber)
        lookupJson.put("endPoint",endpoint)
        lookupJson.put("indexid",accBeneficiary.id)

        var confirmationObj = ConfirmationObj(resources.getString(R.string.remove_beneficiary_acc),accBeneficiary.channelNumber,accBeneficiary.channelName,accBeneficiary.beneficiaryName,R.id.nav_farmerDashboard,"removeAcc")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("accountNumber",accBeneficiary.channelNumber)
        bundle.putString("accountName",accBeneficiary.beneficiaryName)
        bundle.putString("channel",accBeneficiary.channelName)
        bundle.putString("confirmTitle",resources.getString(R.string.confirm_remove_beneficiary))
        bundle.putString("requestjson",lookupJson.toString())
        bundle.putString("endPoint", endpoint)

        findNavController().navigate(R.id.nav_confirmationAddChannel,bundle,navOptions)
    }
}