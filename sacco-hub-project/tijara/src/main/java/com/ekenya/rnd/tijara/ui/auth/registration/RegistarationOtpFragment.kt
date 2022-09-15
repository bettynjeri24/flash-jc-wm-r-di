package com.ekenya.rnd.tijara.ui.auth.registration

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.DefaultPinDialogBinding
import com.ekenya.rnd.tijara.databinding.DeviceVerificationFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.PhoneActivateDTO
import com.ekenya.rnd.tijara.requestDTO.ResetPassDTO
import com.ekenya.rnd.tijara.requestDTO.VerifyOtpDTO
import com.ekenya.rnd.tijara.services.SmsBroadcastReceiver
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.TijaraRoomDatabase
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories.SaccoDetailsRepository
import com.ekenya.rnd.tijara.utils.*
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import java.util.regex.Pattern

class RegistarationOtpFragment : Fragment() {
    private lateinit var cardBinding: DefaultPinDialogBinding

    private lateinit var binding: DeviceVerificationFragmentBinding
    private lateinit var saccoRepository: SaccoDetailsRepository
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(RegistrationViewModel::class.java)
    }
    private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    private var timerStarted: Boolean = false
    var check = Constants.TIMEDIFF * 60

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
        binding = DeviceVerificationFragmentBinding.inflate(layoutInflater)
        val mobile = PrefUtils.getPreferences(requireContext(), "mobile")
        val number = mobile?.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
        binding.tvVerCode.text =
            String.format(getString(R.string.we_have_sent_verification_code), number)
        hideKeyboard()
        binding.tvVerCode.setOnClickListener {
            val directions =
                RegistarationOtpFragmentDirections.actionRegistarationOtpFragmentToDefaultPinFragment(
                    fragmentType = 0
                )
            findNavController().navigate(directions)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        startSmsUserConsent()
        viewModel.otpstatus.observe(viewLifecycleOwner) {
            if (null != it) {
                viewModel.stopObserving()
                binding.progressr.makeGone()
                requireActivity().window.statusBarColor = resources.getColor(R.color.white)

                when (it) {
                    1 -> {
                        binding.progressr.makeGone()
                        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                        val dialog = Dialog(requireContext())
                        cardBinding =
                            DefaultPinDialogBinding.inflate(LayoutInflater.from(context))
                        val mobile = PrefUtils.getPreferences(requireContext(), "mobile")
                        val numbr = mobile?.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                        cardBinding.tvReg.text =
                            String.format(getString(R.string.we_sent_yoi_your), numbr)
                        cardBinding.ivCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        cardBinding.btnREGISTER.setOnClickListener {
                            dialog.dismiss()
                            dialog.hide()
                            val directions =
                                RegistarationOtpFragmentDirections.actionRegistarationOtpFragmentToDefaultPinFragment(
                                    fragmentType = 0
                                )
                            findNavController().navigate(directions)
                        }
                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)
                        viewModel.stopObserving()
                    }
                    0 -> {
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(), viewModel.otpMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()
                    }
                }
            }
        }
        viewModel.resendStatus.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.progressr.makeGone()
                when (it) {
                    1 -> {
                        binding.progressr.makeGone()
                        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                        val dialog = Dialog(requireContext())
                        cardBinding =
                            DefaultPinDialogBinding.inflate(LayoutInflater.from(context))
                        val mobile = PrefUtils.getPreferences(requireContext(), "mobile")
                        val numbr = mobile?.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                        cardBinding.tvReg.text =
                            String.format(getString(R.string.we_sent_yoi_your), numbr)
                        cardBinding.ivCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        cardBinding.btnREGISTER.setOnClickListener {
                            dialog.dismiss()
                            dialog.hide()
                            findNavController().navigate(R.id.action_registarationOtpFragment_to_defaultPinFragment)
                        }
                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)
                        viewModel.stopObserving()
                    }
                    0 -> {
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(), viewModel.resendMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()
                    }
                }
            }
        }
        initCountdownTimer()
    }

    private fun initCountdownTimer() {
        timerStarted = true
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeLeftFormatted =
                    java.lang.String.format(
                        Locale.getDefault(),
                        "%02d min :%02d sec",
                        minutes,
                        seconds
                    )
                binding.codeTimer.setText(timeLeftFormatted)
                binding.btnResend.isEnabled = false
                binding.btnResend.setOnClickListener {
                    binding.btnResend.isClickable = false
                }
            }

            override fun onFinish() {
                binding.btnResend.isEnabled = true
                binding.btnResend.isClickable = true
                binding.btnResend.setOnClickListener {
                    initCountdownTimer()
                    val mobile = PrefUtils.getPreferences(requireContext(), "mobile")
                    val phoneActivateDTO = PhoneActivateDTO()
                    phoneActivateDTO.phone = mobile!!
                    requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                    if (isNetwork(requireContext())) {
                        requireActivity().window.statusBarColor =
                            resources.getColor(R.color.spinkit_color)
                        binding.progressr.makeVisible()
                        binding.progressr.tv_pbTitle.makeGone()
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        val resetPassDTO = ResetPassDTO()
                        resetPassDTO.username = Constants.RegUsername
                        viewModel.resendOTP(resetPassDTO)
                    } else {
                        onNoNetworkDialog(requireContext())
                    }
                }
                timerStarted = false
            }
        }.start()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val saccoDao = TijaraRoomDatabase.getDatabase(requireContext()).getAllSaccoDao()
        saccoRepository = SaccoDetailsRepository(saccoDao)
    }

    override fun onStart() {
        super.onStart()
        registerToSmsBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(smsBroadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_USER_CONSENT -> {
                if ((resultCode == Activity.RESULT_OK) && (data != null)) {
                    // That gives all message to us. We need to get the code from inside with regex
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)!!
                    val code = fetchVerificationCode(message)

                    Timber.d("CODE RECEIVED IS $code")
                    Timber.d("MESSAGE RECEIVED IS $message")
                    val digits = code.map(Character::getNumericValue)
                    Timber.d("SPLIT AT O IS:$digits")
                    Timber.d("SPLIT AT O IS:${digits[0]}")
                    binding.apply {
                        etInputOne.setText(digits[0].toString().trim())
                        etInputTwo.setText(digits[1].toString().trim())
                        etInputThree.setText(digits[2].toString().trim())
                        etInputFour.setText(digits[3].toString().trim())
                        etInputFive.setText(digits[4].toString().trim())
                        etInputSix.setText(digits[5].toString().trim())
                        requireActivity().window.statusBarColor =
                            resources.getColor(R.color.spinkit_color)
                        binding.progressr.makeVisible()
                        binding.progressr.tv_pbTitle.makeGone()
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        if (isNetwork(requireContext())) {
                            val verifyOtpDTO = VerifyOtpDTO()
                            verifyOtpDTO.token = code
                            viewModel.verifyRegOTP(verifyOtpDTO)
                        } else {
                            onNoNetworkDialog(requireContext())
                        }
                    }
                }
            }
        }
    }

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(this.requireActivity()).also {
            // We can add user phone number or leave it blank
            it.startSmsUserConsent(null)
                .addOnSuccessListener {
                    Timber.d("LISTENING_SUCCESS")
                }
                .addOnFailureListener {
                    Timber.d("LISTENING_FAILURE")
                }
        }
    }

    private fun registerToSmsBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver().also {
            it.smsBroadcastReceiverListener =
                object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                    override fun onSuccess(intent: Intent?) {
                        intent?.let { context -> startActivityForResult(context, REQ_USER_CONSENT) }
                    }

                    override fun onFailure() {
                    }
                }
        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        activity?.registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    /**
     * This method extracts the verification code from a message
     * @param in: The message where message ought to be extracted
     * @param codeLength: size of the verification code e.g 0100 is 4
     * @return returns the code
     */

    private fun fetchVerificationCode(message: String): String {
        Timber.d("fetchVerificationCode $message")
        return Regex("(\\d{6})").find(message)?.value ?: ""
        // comment
    }

    fun extractDigits(message: String): String? {
        val p =
            Pattern.compile("(\\d{4})")
        val m = p.matcher(message)
        val test = ""
        return if (m.find()) {
            m.group(0)
        } else ""
    }

    companion object {
        const val REQ_USER_CONSENT = 100
    }
}
