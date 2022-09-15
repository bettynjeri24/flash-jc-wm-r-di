package com.ekenya.rnd.tmd.ui.fragments.confirm_pin

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.biometric.BiometricManager
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentLoginAuthBinding
import com.ekenya.rnd.tmd.ui.fragments.login.Pin
import com.ekenya.rnd.tmd.ui.fragments.login.PinAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfirmNewPinFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentLoginAuthBinding

    @Inject
    lateinit var sharedpreferences: SharedPreferences

    private var pinAdapter: PinAdapter = PinAdapter()

    // observe this list to get the pin entered
    private var pinList = MutableLiveData(
        mutableListOf<Pin>().apply {

            // initialize it with 4 dummy digits
            repeat(4) {
                add(Pin("*"))
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginAuthBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpObserver()
        setUpKeyboard()
    }

    private fun setUpUI() {
        binding.apply {
            // set up pin recyclerview
            recyclerView2.apply {
                pinAdapter.submitList(pinList.value)
                adapter = pinAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
            textViewSubTitle.text = "Confirm Pin"
            groupLogin.visibility = View.INVISIBLE
        }
    }

    private fun setUpKeyboard() {
        // set up keyboard
        binding.includeKeyBoard.apply {
            btnOne.getKeyboardDigit()

            btnTwo.getKeyboardDigit()

            btnThree.getKeyboardDigit()

            btnFour.getKeyboardDigit()

            btnFive.getKeyboardDigit()

            btnSix.getKeyboardDigit()

            btnSeven.getKeyboardDigit()

            btnEight.getKeyboardDigit()

            btnNine.getKeyboardDigit()

            btnZero.getKeyboardDigit()

//                textViewVoiceId.setBiometricsLauncher()

            // removing the last digit
            btnErase.setOnClickListener {
                pinList.value?.asReversed()?.forEach { pin ->
                    if (pin.digit != "*") {
                        pin.digit = "*"
                        pinList.value = pinList.value
                        return@setOnClickListener
                    }
                }
            }
        }
    }

    // set up observer for the pin list
    private fun setUpObserver() {
        pinList.observe(viewLifecycleOwner) { pin ->
            pinAdapter.submitList(pin)
            pinAdapter.notifyDataSetChanged()

            if (pin.filter { it.digit == "*" }.toList().isEmpty()) {
                simulateLoading()
            }
        }
    }

    private fun simulateLoading() {
        lifecycleScope.launch {
            simulateSearching()
            findNavController().navigate(R.id.action_confirmNewPinFragment_to_securityFragment)
        }
    }

    private suspend fun simulateSearching() {
        showHideProgress("Authenticating")
        delay(2000)
        showHideProgress(null)
    }

    private fun isBiometricReady(): Boolean {
        val biometricManager = BiometricManager.from(requireContext())
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    // get value from pressed button
    fun Button.getKeyboardDigit() {
        setOnClickListener {
            pinList.value?.forEach { digit ->
                if (digit.digit == "*") {
                    digit.digit = text.toString()
                    pinList.value = pinList.value
                    return@setOnClickListener
                }
            }
        }
    }
}
