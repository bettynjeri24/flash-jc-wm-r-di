package io.eclectics.cargilldigital.ui.auth.pinmgmt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import cn.pedant.SweetAlert.SweetAlertDialog
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentSetAccountPinBinding
import io.eclectics.cargilldigital.data.model.LookupModel
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.InputValidator
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


class SetAccountPin : Fragment() {
    private var _binding: FragmentSetAccountPinBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navoption: NavOptions
    @Inject
    lateinit var pdialog: SweetAlertDialog
    val genViewModel: GeneralViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_set_account_pin, container, false)
        _binding = FragmentSetAccountPinBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        binding.btnContinue.setOnClickListener {
            //findNavController().navigate(R.id.nav_selectAccount)
            if(isValidFields()) {
                sendSetPinReq()
            }
        }

    }

    private fun isValidFields(): Boolean {

        var pin = binding.etPassword.text.toString()
        var confirmPin = binding.etConfirmPassword.text.toString()

        if(!InputValidator.isValidPIN(pin)) {
            binding.etPassword.requestFocus()
            binding.tlPassword.error = resources.getString(R.string.enter_secure_pin)
            return false
        }
        if(!InputValidator.isValidPIN(pin)) {
            binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(R.string.enter_secure_pin)
            return false
        }
        if(!pin.trim().contentEquals(confirmPin.trim())){
            binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(R.string.new_confirm_notmatch)
            return false
        }

        return true

    }

    private fun sendSetPinReq() {
        var endpoint  =  ApiEndpointObj.createAccPin
        var lookupJson = JSONObject()

        lookupJson.put("phonenumber",requireArguments().getString("phone"))//
        lookupJson.put("pin",binding.etPassword.text.toString())
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
        LoggerHelper.loggerError("offlineproduct","response test loop")

        LoggerHelper.loggerSuccess("response","product response ${response}")
        var lookupresponse: LookupModel = NetworkUtility.jsonResponse(response)
        LoggerHelper.loggerError("phonenumber","response phone ${lookupresponse.phonenumber}")
        //var lookupResponse = NetworkUtility.jsonResponse(productJson)
        var bundle = Bundle()
        bundle.putString("phone",requireArguments().getString("phone"))
        //SET DUMMEY FIRST TIME LOGIN
        UtilPreference().setTirstTimeLogin(requireActivity(),false)
        //save logged phone number
        UtilPreference().setLoggedPhoneNumber(requireActivity(),requireArguments().getString("phone")!!)
        NetworkUtility().confirmTransactionEndNav(binding.root,lookupresponse.message,requireActivity(),R.id.nav_loginAccountFragment,bundle)
        //findNavController().navigate(R.id.nav_selectAccount,bundle,navoption)

    }

}