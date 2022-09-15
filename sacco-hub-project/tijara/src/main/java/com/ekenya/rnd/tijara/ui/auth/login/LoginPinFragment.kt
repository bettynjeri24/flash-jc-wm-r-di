package com.ekenya.rnd.tijara.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.MainActivity
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentLoginPinBinding
import com.ekenya.rnd.tijara.requestDTO.LoginDTO
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import javax.inject.Inject

class LoginPinFragment : BaseDaggerFragment() {
    companion object {
        fun loginInstance() = LoginPinFragment()
    }

    private lateinit var pinBinding: FragmentLoginPinBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    /**the pin inputs*/
    private var one1: String? = null
    private var two2: String? = null
    private var three3: String? = null
    private var four4: String? = null
    private var mConfirmPin: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pinBinding = FragmentLoginPinBinding.inflate(layoutInflater)
        hideKeyboard()
        return pinBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.e(
            "=============\nLoginPinFragment\n==USERNAME ${
            PrefUtils.getPreferences(
                requireContext(),
                getString(R.string.stored_username)
            )
            }==========="
        )

        if (Constants.userFname.isEmpty()) {
            pinBinding.tvGreetings.text = String.format(
                getGreetings()!!,
                ""
            )
        } else {
            pinBinding.tvGreetings.text = String.format(
                getGreetings()!!,
                Constants.userFname
            )
        }

        pinBinding.apply {
            ivBack.setOnClickListener { findNavController().navigateUp() }
            tvForgotPin.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_loginPinFragment_to_forgetPinDashboardFragment)
            }
            btnOne.setOnClickListener { controlPinPad2("1") }
            btnTwo.setOnClickListener { controlPinPad2("2") }
            btnThree.setOnClickListener { controlPinPad2("3") }
            btnFour.setOnClickListener { controlPinPad2("4") }
            btnFive.setOnClickListener { controlPinPad2("5") }
            btnSix.setOnClickListener { controlPinPad2("6") }
            btnSeven.setOnClickListener { controlPinPad2("7") }
            btnEight.setOnClickListener { controlPinPad2("8") }
            btnNine.setOnClickListener { controlPinPad2("9") }
            btnZero.setOnClickListener { controlPinPad2("0") }
            btnDelete.setOnClickListener { deletePinEntry() }
            viewModel.status.observe(viewLifecycleOwner) {
                if (null != it) {
                    requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                    pinBinding.avi.makeGone()
                    clearPin()
                    when (it) {
                        1 -> {
                            clearPin()
                            pinBinding.avi.makeGone()
                            pinBinding.clPin.visibility = View.GONE
                            PrefUtils.setPreference(requireContext(), "isFirstLogin", "false")
                            //    startActivity(Intent(activity, MainActivity::class.java))
                            viewModel.changePassword.observe(viewLifecycleOwner) { status ->
                                Log.d("TAG", "hhh$status")
                                if (status == false) {
                                    PrefUtils.setPreference(
                                        requireContext(),
                                        "changePassword",
                                        "false"
                                    )
                                    startActivity(Intent(activity, MainActivity::class.java))
                                    // findNavController().navigate(R.id.action_loginPinFragment_to_changeFirstPinFragment)
                                } else {
                                    startActivity(Intent(activity, MainActivity::class.java))
                                }
                            }
                            viewModel.stopObserving()
                        }
                        0 -> {
                            onInfoDialog(
                                requireContext(),
                                viewModel.statusMessage.value
                            )
                            clearPin()
                            pinBinding.avi.makeGone()
                            pinBinding.clPin.visibility = View.VISIBLE
                            viewModel.stopObserving()
                        }
                        else -> {
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))
                            clearPin()
                            pinBinding.avi.makeGone()
                            pinBinding.clPin.visibility = View.VISIBLE
                            viewModel.stopObserving()
                        }
                    }
                }
            }
        }
    }

    private fun controlPinPad2(entry: String) {
        pinBinding.apply {
            if (one1 == null) {
                one1 = entry
                pin1.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            } else if (two2 == null) {
                two2 = entry
                pin2.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            } else if (three3 == null) {
                three3 = entry
                pin3.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            } else if (four4 == null) {
                four4 = entry
                pin4.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }
            if (mConfirmPin == null) {
                mConfirmPin = entry
            } else {
                mConfirmPin += entry
            }
            if (mConfirmPin!!.length == 4) {
                if (isNetwork(requireContext())) {
                    val loginDTO = LoginDTO()
                    loginDTO.password = mConfirmPin as String
                    val register_id = Constants.SIGNUPORGID
                    val login_id = Constants.ORGID
                    if (Constants.SELECTED_TYPE == 0) {
                        loginDTO.org_id = register_id
                        loginDTO.username = Constants.RegUsername
                        Timber.d("SIGN_UP ID $register_id")
                        Timber.d("USERNAME ${loginDTO.username}")
                    }
                    if (Constants.SELECTED_TYPE == 1) {
                        loginDTO.username = Constants.USERNAME
                        loginDTO.org_id = login_id
                        Timber.d("LOGINID: $login_id")
                    }
                    pinBinding.clPin.visibility = View.GONE
                    pinBinding.avi.show()
                    viewModel.loginUser(loginDTO)
                } else {
                    onNoNetworkDialog(requireContext())
                }
            }
        }
    }

    private fun deletePinEntry() {
        pinBinding.apply {
            if (mConfirmPin != null && mConfirmPin!!.length > 0) {
                mConfirmPin = mConfirmPin!!.substring(0, mConfirmPin!!.length - 1)
            }
            if (four4 != null) {
                pin4.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                four4 = null
            } else if (three3 != null) {
                pin3.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                three3 = null
            } else if (two2 != null) {
                pin2.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                two2 = null
            } else if (one1 != null) {
                pin1.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                one1 = null
            }
        }
    }

    private fun clearPin() {
        pinBinding.apply {
            pin1.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            one1 = null
            pin2.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            two2 = null
            pin3.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            three3 = null
            pin4.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            four4 = null
            mConfirmPin = null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
    }
}
