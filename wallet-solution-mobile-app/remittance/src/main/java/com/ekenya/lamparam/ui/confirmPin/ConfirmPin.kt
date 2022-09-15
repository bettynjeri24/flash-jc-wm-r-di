package com.ekenya.lamparam.ui.confirmPin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.databinding.FragmentConfirmPinBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class ConfirmPin : Fragment() {

    private lateinit var viewModel: ConfirmPinViewModel

    private var _binding: FragmentConfirmPinBinding? = null
    private val binding get() = _binding!!

    //biometrics
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmPinBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //((activity as LampMainActivity).hideActionBar())

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ConfirmPinViewModel::class.java)

        viewModel.navigated.observe(viewLifecycleOwner, Observer {
            if (it)
                navigate()
        })

        viewModel.pin.observe(viewLifecycleOwner, Observer {
            if (it.length >= 0)
                changeImages(it)
        })

        viewModel.bioMetric.observe(viewLifecycleOwner, Observer {
            if (it)
                prompt()
        })

        initBioMetrics()

        binding.apply {
            rvFinger.setOnClickListener {
                viewModel.bioMetrics()
            }
            tv1.setOnClickListener {
                viewModel.mapToCode("1")
            }
            tv2.setOnClickListener {
                viewModel.mapToCode("2")
            }
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
            rvDelete.setOnClickListener {
                viewModel.deleteKeyStoke()
            }
        }


    }

    private fun changeImages(id: String) {
        binding.apply {
            when (id.length) {
                0 -> {
                    iv1.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_unfilled
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
                    iv4.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.pin_oval_filled
                        )
                    )
                    if (id != "4545"){
//                        globalMethods.transactionWarning(requireActivity(), getString(R.string.invalid_pin))
                        viewModel.clearFields()
                    }else{
                        viewModel.navigate()
                    }

                }
            }
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
//            globalMethods.loader(requireActivity())
            delay(2000)

            findNavController().navigate(R.id.nav_secretPin)
            viewModel.hasNavigated()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}