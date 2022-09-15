package io.eclectics.cargilldigital.ui.auth.lookup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentUserPhoneLookupBinding
import io.eclectics.cargilldigital.data.model.LookupModel
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.InputValidator
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class UserPhoneLookup : Fragment() {
    private var _binding: FragmentUserPhoneLookupBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var pdialog:SweetAlertDialog
    @Inject
    lateinit var navOptions:NavOptions
    val genViewModel:GeneralViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserPhoneLookupBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        //ToolBarMgmt.setToolbarTitle("Change pin","Verify and change pin",binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        binding.ccp.setCcpClickable(false)
        //TODO UNCOMMENT THIS IN RELEASE
        //  app:ccp_defaultPhoneCode="237"
        binding.ccp.setCountryForPhoneCode(225)
        binding.phoneNumberEditText.setText("")
        binding.btnContinue.setOnClickListener {
            //findNavController().navigate(R.id.nav_onboardingOtp)
            if(!InputValidator.isValidPhone(binding.phoneNumberEditText.text.toString())) {
                binding.phoneNumberEditText.requestFocus()
                //binding.phoneNumberTextInputLayout.error = resources.getString(R.string.validation_phone_number)
                binding.tvPhoneNumberTitle.text = resources.getString(R.string.validation_phone_number)
            }else {
                sendLookupRequest()
            }
        }
        binding.phoneNumberEditText.addTextChangedListener {
            binding.phoneNumberTextInputLayout.setHint("")
            binding.tvPhoneNumberTitle.text = ""
        }
      
    }

    private fun sendLookupRequest() {
        var endpoint  =  ApiEndpointObj.customerLookup
        var lookupJson = JSONObject()
        lookupJson.put("phonenumber","225"+binding.phoneNumberEditText.text.toString())
         lifecycleScope.launch {
            pdialog.show()
            genViewModel.phoneLookupRequest(lookupJson, endpoint, requireActivity()).observe(requireActivity(), Observer {
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
        LoggerHelper.loggerSuccess("response","product response ${response}")
                var lookupresponse:LookupModel = NetworkUtility.jsonResponse(response)

        LoggerHelper.loggerError("phonenumber","response phone ${lookupresponse.phonenumber}")
        //var lookupResponse = NetworkUtility.jsonResponse(productJson)
        var bundle = Bundle()
        bundle.putString("phone",lookupresponse.phonenumber)
        findNavController().navigate(R.id.nav_providerIdLookup,bundle,navOptions)
    }


}