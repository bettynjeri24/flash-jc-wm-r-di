package io.eclectics.cargilldigital.ui.auth.lookup

import android.content.IntentFilter
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
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentCompanyIdLookupBinding
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.eef.moneyphone.services.SMSReceiver
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CompanyIDLookup : Fragment() {

    private var _binding: FragmentCompanyIdLookupBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var pdialog: SweetAlertDialog
    @Inject
    lateinit var navOptions: NavOptions
    @Inject
    lateinit var smsReceiver: SMSReceiver
    val genViewModel: GeneralViewModel by viewModels()
    lateinit var phonenumber:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment  tvGreeting
       // return inflater.inflate(R.layout.fragment_company_id_lookup, container, false)
        _binding = FragmentCompanyIdLookupBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        //ToolBarMgmt.setToolbarTitle("Change pin","Verify and change pin",binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        binding.tvGreeting.text = resources.getString(R.string.greetings_morning)//GlobalMethods().getGreetings()
        phonenumber = requireArguments().getString("phone").toString()
        binding.btnContinue.setOnClickListener {
            sendLoginRequest()
        }
    }

    private fun sendLoginRequest() {
        var endpoint  =  ApiEndpointObj.providerIdLookup
        var lookupJson = JSONObject()

        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        lookupJson.put("accountid",binding.phoneNumberEditText.text.toString())
        lookupJson.put("phonenumber",phonenumber)
         lifecycleScope.launch {
            pdialog.show()
            genViewModel.phoneLookupRequest(lookupJson,endpoint,requireActivity()).observe(requireActivity(), Observer {
                pdialog.dismiss()
                when(it){
                    is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    //processRequest(it.value)//
                }
            })
        }
    }

    private fun processRequest(response: String) {
        var jsonObject = JSONObject(response)
       var phoneNumber = jsonObject.getString("phonenumber")
        var bundle = Bundle()
        bundle.putString("phone",phoneNumber)
        startSMSListener()
        findNavController().navigate(R.id.nav_onboardingOtp,bundle,navOptions)
    }

    private fun startSMSListener() {
        try {
            LoggerHelper.loggerError("startsmslistener","listener activated")
            //smsReceiver.initOTPListener(this)
            val intentFilter = IntentFilter()
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
            activity?.registerReceiver(smsReceiver, intentFilter)
            context?.let { smsReceiver.startSMSRetriever(it) }
        } catch (e: Exception) {
            Timber.e("Exception Message -%s", e.message)
        }

    }
}