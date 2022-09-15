package com.ekenya.lamparam.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.databinding.FragmentPinBinding
import com.ekenya.lamparam.network.DefaultResponse
import com.ekenya.lamparam.network.Status
import com.ekenya.lamparam.user.UserDataRepository
import com.ekenya.lamparam.utilities.GlobalMethods
import kotlinx.coroutines.*

import java.util.concurrent.Executor
import javax.inject.Inject

class PinFragment : Fragment() {
    private lateinit var viewModel: PinViewModel

    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!

    //biometrics
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @Inject
    lateinit var globalMethods: GlobalMethods

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userDataRepository: UserDataRepository

    private lateinit var phone: String

    //initialized for all fragments
    private val onboardingViewModel: OnBoardingViewModel by activityViewModels() { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PinViewModel::class.java)

        phone = userDataRepository.phoneNumber

        viewModel.navigated.observe(viewLifecycleOwner, {
            if (it)
                navigate()
        })

        viewModel.pin.observe(viewLifecycleOwner, {
            if (it.length >= 0)
                changeImages(it)
        })

        viewModel.bioMetric.observe(viewLifecycleOwner, {
            if (it)
                prompt()
        })

        initBioMetrics()

        binding.apply {
            rvFinger.setOnClickListener { viewModel.bioMetrics() }
            tv1.setOnClickListener { viewModel.mapToCode("1") }
            tv2.setOnClickListener { viewModel.mapToCode("2") }
            tv3.setOnClickListener {
                viewModel.mapToCode("3")
            }
            tv4.setOnClickListener {
                viewModel.mapToCode("4")
            }
            tv5.setOnClickListener {
                viewModel.mapToCode("5")
            }
            tv6.setOnClickListener {
                viewModel.mapToCode("6")
            }
            tv7.setOnClickListener {
                viewModel.mapToCode("7")
            }
            tv8.setOnClickListener {
                viewModel.mapToCode("8")
            }
            tv9.setOnClickListener {
                viewModel.mapToCode("9")
            }
            tv0.setOnClickListener {
                viewModel.mapToCode("0")
            }
            rvDelete.setOnClickListener { viewModel.deleteKeyStoke() }
        }
    }

    private fun changeImages(id: String) {
        binding.apply {
            when (id.length) {
                0 -> {
                    iv1.setImageDrawable( ContextCompat.getDrawable( requireActivity(),
                            R.drawable.pin_oval_unfilled) )
                    iv2.setImageDrawable( ContextCompat.getDrawable( requireActivity(),
                            R.drawable.pin_oval_unfilled ) )
                    iv3.setImageDrawable( ContextCompat.getDrawable(  requireActivity(),
                        R.drawable.pin_oval_unfilled ) )
                    iv4.setImageDrawable( ContextCompat.getDrawable( requireActivity(),
                            R.drawable.pin_oval_unfilled ) )
                }
                1 -> {
                    iv1.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv2.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_unfilled
                        )
                    )
                    iv3.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_unfilled
                        )
                    )
                    iv4.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_unfilled
                        )
                    )
                }
                2 -> {
                    iv1.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv2.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv3.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_unfilled
                        )
                    )
                    iv4.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_unfilled
                        )
                    )
                }
                3 -> {
                    iv1.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv2.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv3.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv4.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_unfilled
                        )
                    )
                }
                4 -> {
                    iv1.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv2.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv3.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    iv4.setImageDrawable( ContextCompat.getDrawable( requireActivity(),
                            R.drawable.pin_oval_filled ) )
                    sendRequest(id)
                }
            }
        }
    }

    /**
     * Sends request to perform account lookup
     */
    private fun sendRequest(id: String) {
        onboardingViewModel.login(phone, id).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        viewModel.clearFields()
                        globalMethods.loader(requireActivity(), 1)
                        resource.data?.let { data -> parseData(data.body()!!) }
                    }
                    Status.ERROR -> {
                        globalMethods.loader(requireActivity(), 1)
                        viewModel.clearFields()
                        globalMethods.transactionError(requireActivity(), it.message!!)
                    }
                    Status.LOADING -> {
                        globalMethods.loader(requireActivity(), 0)
                    }
                }
            }
        })
    }

    private fun parseData(body: DefaultResponse) {
        val resCode = body.responseCode
        val resMsg = body.responseMessage
        if (resCode == "00"){
            navigate()
        }else{
            globalMethods.transactionError(requireActivity(), resMsg)
        }
    }

    private fun initBioMetrics() {
        //biometrics
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        requireContext(),
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.navigate()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.fingerprint_login))
            .setSubtitle(" ")
            .setNegativeButtonText(getString(R.string.use_password))
            .setDescription(getString(R.string.fingerprint_desc))
            .build()
    }

    private fun prompt() {
        biometricPrompt.authenticate(promptInfo)
        viewModel.checkedBiometrics()
    }

    private fun navigate() {
        GlobalScope.launch(Dispatchers.Main) {
            startActivity(Intent(activity, LampMainActivity::class.java))
            viewModel.hasNavigated()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}