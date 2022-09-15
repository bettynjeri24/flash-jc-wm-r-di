package com.ekenya.rnd.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.VerifyDefaultPinPayload
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentInitialPinBinding

class InitialPinFragment : Fragment() {
    private lateinit var binding: FragmentInitialPinBinding
    private var pin = ""
    private lateinit var confirmViewModel: ConfirmViewModel
    private lateinit var viewModel: MobileWalletViewModel
    private lateinit var loginViewModel: LoginViewModel


    private var one1: String? = null
    private var isComplete = false
    private var isDone = false
    private var two2: String? = null
    private var three3: String? = null
    private var four4: String? = null
    private var mConfirmPin: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInitialPinBinding.inflate(layoutInflater, container, false)

        initUI()
        initializeViewModel()


        return binding.root
    }

    private fun initializeViewModel() {

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(MobileWalletViewModel::class.java)

        confirmViewModel = ViewModelProvider(this).get(ConfirmViewModel::class.java)
    }

    private fun initUI() {
        binding.create4digitpinTxt.text =
            "The Default pin was sent to ${blurPhoneNumber(requireContext())}"
        binding.apply {
            binding.btnOne.setOnClickListener { controlPinPad2("1") }
            binding.btnTwo.setOnClickListener { controlPinPad2("2") }
            binding.btnThree.setOnClickListener { controlPinPad2("3") }
            binding.btnFour.setOnClickListener { controlPinPad2("4") }
            binding.btnFive.setOnClickListener { controlPinPad2("5") }
            binding.btnSix.setOnClickListener { controlPinPad2("6") }
            binding.btnSeven.setOnClickListener { controlPinPad2("7") }
            binding.btnEight.setOnClickListener { controlPinPad2("8") }
            binding.btnNine.setOnClickListener { controlPinPad2("9") }
            binding.btnZero.setOnClickListener { controlPinPad2("0") }
            binding.btnDelete.setOnClickListener { deletePinEntry() }
        }
    }

    private fun controlPinPad2(entry: String) {
        binding.apply {
            if (one1 == null) {
                binding.pin1.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.activestepsbackground
                    )
                };
                one1 = entry
            } else if (two2 == null) {
                binding.pin2.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.activestepsbackground
                    )
                };
                two2 = entry
            } else if (three3 == null) {
                binding.pin3.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.activestepsbackground
                    )
                };
                three3 = entry
            } else if (four4 == null) {
                binding.pin4.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.activestepsbackground
                    )
                };
                four4 = entry
                isDone = true
            }

            if (isDone) {
                /*if (validDetails()){*/
                pin = one1 + two2 + three3 + four4
                // mainViewModel.resetPin(user_id,pin)

                //confirm pin is correct

                //verifyPin
                verifyPin(pin)

                saveInitialPinEntered(pin)
                // findNavController().navigate(R.id.actionInitialpin_tocreatenewpin2Fragment)
                // requestInitialPinConfirmation()


                //}
            }

        }
    }

    private fun verifyPin(otp: String) {

        val data = VerifyDefaultPinPayload(
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            otp
        )
        loginViewModel.verifyDefaultPin(data).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.avi.makeInvisible()

                        if (it.data!!.status == 0) {


                            findNavController().navigate(R.id.createNewPinFragment2)

                        } else {
                            showErrorSnackBar(it.data!!.message)
                        }

                    }
                    Status.ERROR -> {

                        binding.avi.makeInvisible()
                        try {
                            showErrorSnackBar(it.data!!.message)

                        } catch (e: Exception) {
                        }
                    }
                    Status.LOADING -> {
                        binding.avi.makeVisible()

                        binding.pin1.makeInvisible()
                        binding.pin2.makeInvisible()
                        binding.pin3.makeInvisible()
                        binding.pin4.makeInvisible()


                    }
                }
            }
        })

    }

    private fun saveInitialPinEntered(initialPin: String) {
        SharedPreferencesManager.setInitialPin(requireContext(), initialPin)
    }

    private fun deletePinEntry() {
        binding.apply {
            if (mConfirmPin != null && mConfirmPin!!.length > 0) {
                mConfirmPin = mConfirmPin!!.substring(0, mConfirmPin!!.length - 1)
            }
            if (four4 != null) {
                binding.pin4.background = resources.getDrawable(R.drawable.inactive_pin_bg)
                four4 = null
                isDone = false

            } else if (three3 != null) {
                binding.pin3.background = resources.getDrawable(R.drawable.inactive_pin_bg)
                three3 = null
            } else if (two2 != null) {
                binding.pin2.background = resources.getDrawable(R.drawable.inactive_pin_bg)
                two2 = null
            } else if (one1 != null) {
                binding.pin1.background = resources.getDrawable(R.drawable.inactive_pin_bg)
                one1 = null
            }
        }
    }


}


