package com.ekenya.rnd.onboarding.ui

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.DashBoardActivity
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.AccountLookUpPayload
import com.ekenya.rnd.dashboard.datadashboard.model.VerifyDevicePayload
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.onboarding.BuildConfig
import com.ekenya.rnd.onboarding.base.SmsBroadcastReceiver
import com.ekenya.rnd.onboarding.databinding.FragmentAccountLookUpOtpBinding
import com.google.android.gms.auth.api.phone.SmsRetriever


open class DeviceVerificationFragment : Fragment() {
    private var binding: FragmentAccountLookUpOtpBinding? = null
    private lateinit var viewModel: LoginViewModel

    private lateinit var tvtimer: TextView
    private lateinit var otp: String
    private lateinit var otpdigit1: String
    private lateinit var otpdigit2: String
    private lateinit var otpdigit3: String
    private lateinit var otpdigit4: String
    private lateinit var otpdigit5: String
    private lateinit var otpdigit6: String
    private val REQ_USER_CONSENT = 200
    private lateinit var intentFilter: IntentFilter
    private var smsReceiver: SmsBroadcastReceiver? = null
    private lateinit var version: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountLookUpOtpBinding.inflate(layoutInflater)
        initializeViewModel()
        //startSmsUserConsent()
        initUI()
        //setClickListeners()
        setTimer()
        initBroadCast()
        initSmsListener()
        hardCodeCredentials()




        return binding!!.root
    }


    private fun initUI() {
        //
        binding!!.bypassBtn.setOnClickListener {
            doDeviceOtpVerification(SharedPreferencesManager.getOtpToken(requireContext())!!)
        }


        tvtimer = binding!!.tvTimer
        binding!!.tvCodeSentto.text =
            "We’ve sent you a code to +${blurPhoneNumber(requireContext())} "
        binding!!.btnResendOtp.setOnClickListener {
            binding!!.tvTimer.makeVisible()
            setTimer()
            resendDeviceToken()
        }
    }

    private fun initBroadCast() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = SmsBroadcastReceiver()
        smsReceiver?.setOTPListener(object : SmsBroadcastReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                if (!otp.isNullOrBlank()) {
                    val rawotp = otp.toCharArray()
                    binding?.tilDigit1?.setText(rawotp[0].toString())
                    binding?.tilDigit2?.setText(rawotp[1].toString())
                    binding?.tilDigit3?.setText(rawotp[2].toString())
                    binding?.tilDigit4?.setText(rawotp[3].toString())
                    binding?.tilDigit5?.setText(rawotp[4].toString())
                    binding?.tilDigit6?.setText(rawotp[5].toString())

                    //otpToken = "${matcher.group(0)}"
                    doDeviceOtpVerification(otp!!)
                }
            }
        })
    }

    private fun initSmsListener() {
        val client = SmsRetriever.getClient(requireActivity())
        client.startSmsRetriever()
    }


    /*   private fun validDetails(): Boolean {
           otpdigit1 = binding?.tilDigit1?.text.toString().trim()
           otpdigit2 = binding?.tilDigit2?.text.toString().trim()
           otpdigit3 = binding?.tilDigit3?.text.toString().trim()
           otpdigit4 = binding?.tilDigit4?.text.toString().trim()
           otpdigit5 = binding?.tilDigit5?.text.toString().trim()
           otpdigit6 = binding?.tilDigit6?.text.toString().trim()

           if (otpdigit1.isNullOrBlank()) {
               binding?.tilDigit1?.error = "Please retry otp"
               binding?.tilDigit1?.error
               return false
           }


           return true
       }*/


    private fun hardCodeCredentials() {
        if (BuildConfig.DEBUG) {
            version = "debug"

        } else {
            version = "release"
        }
    }

    private fun resendDeviceToken() {
        val data = AccountLookUpPayload(
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            version
        )
        viewModel.resendDeviceToken(data).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        // startSmsUserConsent()


                        if (it.data!!.status == 0) {

                        } else {
                            showErrorSnackBar("Some Error Occcured")
                        }

                    }
                    Status.ERROR -> {


                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {


                    }
                }
            }
        })
    }


    fun setTimer() {

        object : CountDownTimer(50000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvtimer.setText("Resending otp in: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                binding!!.btnResendOtp.makeVisible()
                binding!!.tvTimer.makeInvisible()

                //resendDeviceToken()

            }
        }.start()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)
    }


    override fun onResume() {
        super.onResume()
        changeActionbarColor(Color.parseColor("#ffffff"))

        /*removeActionbarTitle()

        changeActionbarColor(R.color.white)
        removeActionBarElevation()*/
        requireActivity().registerReceiver(smsReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        smsReceiver = null
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(smsReceiver)
    }


    private fun doDeviceOtpVerification(otp: String) {
        val data = VerifyDevicePayload(
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            otp,
            "Authenticate"
        )
        viewModel.verifyDeviceOtpLookUp(data).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        when (it.data!!.status) {
                            0 -> {
                                showSuccessSnackBar("Device verified Successfully")
                                Handler().postDelayed({
                                    //doSomethingHere()
                                    val intent = Intent(activity, DashBoardActivity::class.java)
                                    startActivity(intent)
                                }, 1000)

                                /*if (SharedPreferencesManager.isRegisteredUser(requireContext())==false){
                                    findNavController().navigate(R.id.deviceVerificationFragmenttocreatepinaction)
                                }else{*/

                                //}
                            }
                            1 -> {
                                //findNavController().navigate(R.id.action_accountLookUpFragment_to_accountLookupotpfragment)
                                /*SharedPreferencesManager.setOtpToken(
                                    requireContext(),
                                    it.data!!.data.toString()
                                )*/
                            }
                            else -> {
                                toastMessage(it.data!!.message)

                            }
                        }

                    }
                    Status.ERROR -> {
                        //  binding.progressBar.visibility = View.INVISIBLE

                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        //  binding.progressBar.visibility = View.VISIBLE


                    }
                }
            }
        })
    }


}



