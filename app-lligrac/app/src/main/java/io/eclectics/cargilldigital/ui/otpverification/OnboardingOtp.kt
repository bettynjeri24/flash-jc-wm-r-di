package io.eclectics.cargilldigital.ui.otpverification

import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentOnboardingOtpBinding
import io.eclectics.cargilldigital.data.model.LookupModel
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.eef.moneyphone.services.SMSReceiver
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingOtp : Fragment() , SMSReceiver.OTPReceiveListener {

    @Inject
    lateinit var navoption: NavOptions
    @Inject
    lateinit var smsReceiver: SMSReceiver
    private var _binding: FragmentOnboardingOtpBinding? = null
    private val binding get() = _binding!!
    lateinit var phonenumber:String
    @Inject
    lateinit var pdialog: SweetAlertDialog
    val genViewModel:GeneralViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingOtpBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.verification_code),resources.getString(R.string.activate_account),binding.mainLayoutToolbar,requireActivity())
        LoggerHelper.loggerError("startListenng", "strart listenung now")
        Timber.e("tryOncreate","listening from oncreate")
        startSMSListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        smsReceiver.initOTPListener(this)
        phonenumber = requireArguments().getString("phone").toString()
        binding.btnVerifyOtp.setOnClickListener {
            //findNavController().navigate(R.id.nav_changePin,null,navoption)
            verifyOtp()
        }

    }

    private fun verifyOtp() {
        var endpoint  =  ApiEndpointObj.verifyLookupOtp
        var lookupJson = JSONObject()

        lookupJson.put("phonenumber",phonenumber)
        lookupJson.put("otp",binding.edtOtp.text.toString())
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
        NetworkUtility().confirmTransactionEndNav(binding.root,lookupresponse.message,requireActivity(),R.id.nav_setAccountPin,bundle)
        //findNavController().navigate(R.id.nav_changePin,bundle,navoption)

    }

    private fun startSMSListener() {
        try {
            LoggerHelper.loggerError("startsmslistener","listener activated")
            smsReceiver.initOTPListener(this)
            val intentFilter = IntentFilter()
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
            activity?.registerReceiver(smsReceiver, intentFilter)
            context?.let { smsReceiver.startSMSRetriever(it) }
        } catch (e: Exception) {
            Timber.e("Exception Message -%s", e.message)
        }

    }
    /* override fun onDestroyView() {
         super.onDestroyView()
         _binding = null
         context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(smsReceiver) }
     }*/

    override fun onOTPReceived(message: String) {
        Timber.e("success Message -%s", message)
        val thread = Thread {
            Handler(Looper.getMainLooper()).post {
                binding.edtOtp.setText(message)

                /* smsOtp = message
                 *//*  binding.etVerificationCode.setText(smsOtp)
                  binding.mProgressBar.visibility = View.GONE
                  binding.btnOk.visibility = View.VISIBLE*//*
                fBinding!!.etVerificationCode.setText(smsOtp)
                fBinding!!.mProgressBar.visibility = View.GONE
                fBinding!!.btnOk.visibility = View.VISIBLE
                //    binding.btnContinue.visibility = View.VISIBLE
                validateData()*/
            }
        }
        thread.start()
    }

    override fun onOTPTimeOut(timeoutMessage: String) {
        Timber.e("timeoutMessage Message -%s", timeoutMessage)
    }
}