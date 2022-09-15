package com.ekenya.rnd.onboarding.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.ConfirmFragmentBinding
import com.ekenya.rnd.walletbaseapp.tollo.WalletSolutionApp
import com.ekenya.rnd.walletbaseapp.tollo.di.helpers.activities.ActivityHelperKt
import com.ekenya.rnd.walletbaseapp.tollo.di.helpers.activities.AddressableActivity
import com.ekenya.rnd.walletbaseapp.tollo.di.helpers.features.FeatureModule
import com.ekenya.rnd.walletbaseapp.tollo.di.helpers.features.Modules
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus


class ConfirmFragment : Fragment() {
    private lateinit var binding: ConfirmFragmentBinding
    private var pin = ""
    private lateinit var confirmViewModel: ConfirmViewModel
    private lateinit var viewModel: MobileWalletViewModel

    private var one1: String? = null
    private var isComplete = false
    private var isDone = false
    private var two2: String? = null
    private var three3: String? = null
    private var four4: String? = null
    private var mConfirmPin: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmFragmentBinding.inflate(layoutInflater)


        mApp = activity?.application as WalletSolutionApp


        initUI()
        initializeViewModel()
        //setObservers()

        return binding.root
    }

    private fun setObservers() {
        confirmViewModel.isComplete.observe(viewLifecycleOwner, Observer {
            requestConfirmation()
        })
    }

    private fun requestConfirmation() {
        val data = MainDataObject(
            AppUtils.getChangePinData(
                requireContext()
            )
        )

        viewModel.confirmUserRegistration(data).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {

                        binding.avi.visibility = View.GONE

                        if (it.data?.data?.response?.response_code.equals("00")) {

                            SharedPreferencesManager.setHasSetPin(requireContext(), 3)
                            succesfulRegistrationDialog("Continue to Login")
                        } else {

                            showTransactionErrorDialog(it.data?.data?.response?.response_message!!,"Would like to Try again")

                        }

                    }
                    Status.ERROR -> {
                        binding.avi.visibility = View.GONE

                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                    }
                    Status.LOADING -> {

                        binding.avi.visibility = View.VISIBLE

                        binding.pin1.visibility = View.INVISIBLE
                        binding.pin2.visibility = View.INVISIBLE
                        binding.pin3.visibility = View.INVISIBLE
                        binding.pin4.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }


    private fun initializeViewModel() {
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

                pin = one1 + two2 + three3 + four4



                if (context?.let { SharedPreferencesManager.getPin(it) }.equals(pin)) {
                    requestConfirmation()

                } else {

                    showTransactionErrorDialog("Pins Do not Match", "Kindly Retry")
                }

                // findNavController().navigate(R.id.action_confirmFragment_to_loginFragment)

                //}
            }
            /*if (mConfirmPin == null) {
                mConfirmPin = entry
            } else {
                mConfirmPin = mConfirmPin + entry
            }*/
            /* if (mConfirmPin!!.length == 4) {
                 val directions =ChangePinFragmentDirections.actionChangePinFragmentToConfirmPinFragment()
                 findNavController().navigate(directions)
             }*/
        }
    }

    fun showTransactionErrorDialog(errorTitle: String, errorMessage: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.transaction_failed_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)
        val tvErrorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)
        val tvErrorTitle = dialog.findViewById<TextView>(R.id.tv_sorry)
        var title = errorTitle

        tvErrorMessage.text = errorMessage
        tvErrorTitle.text = errorTitle
        btnAlldone.text = "Retry"


        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()

            navigateTo(errorTitle)


        }


        btnAlldone.setOnClickListener {

            dialog.dismiss()
            navigateTo(errorTitle)

        }
        dialog.show()
        setDialogLayoutParams(dialog)
    }

    private fun navigateTo(errorTitle: String) {
        when (errorTitle) {
            "Pins Do not Match" -> {
                findNavController().navigate(R.id.createNewPinFragment2)

            }
            else -> {
                findNavController().navigate(R.id.getStartedFragment)
            }
        }
    }

    fun succesfulRegistrationDialog(error: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.user_succesfully_registered)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAlldone = dialog.findViewById<Button>(R.id.btn_Proceed)
        val errorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)
        val title = dialog.findViewById<TextView>(R.id.tv_sorry)
        title.text = "Pin Change Succesful"

        errorMessage.text = error

        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.loginFragment2)
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.loginFragment2)

        }
        dialog.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if (splitInstallManager.installedModules.contains(module.toString())) {
            // showFeatureModule(module)
//            setStatus("${module.name} already installed\nPress start to continue ..")
//            //
            // setObservers()
            /*binding?.btnSkiptoDashboard?.setOnClickListener{
                showFeatureModule(module)
            }*/
            return
        }
        val request = SplitInstallRequest
            .newBuilder()
            .addModule(module.name)
            .build()

        splitInstallManager.startInstall(request)
    }


    override fun onPause() {
        splitInstallManager.unregisterListener(listener)
        super.onPause()
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

    private var mApp: WalletSolutionApp? = null


    private val module by lazy {
        Modules.FeatureDashboard.INSTANCE
    }

    private val splitInstallManager: SplitInstallManager by lazy {
        SplitInstallManagerFactory.create(requireActivity())
    }

    private val listener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                // setStatus("DOWNLOADING")
            }
            SplitInstallSessionStatus.INSTALLING -> {
                //setStatus("INSTALLING")
            }
            SplitInstallSessionStatus.INSTALLED -> {

                // Enable module immediately
                activity?.let { SplitCompat.install(it) }

                //setStatus("${module.name} already installed\nPress start to continue ..")
                //
                /* binding.startButton.visibility = View.VISIBLE
                 binding.startButton.setOnClickListener*///{
                //binding?.btnSkiptoDashboard?.setOnClickListener{
                //showFeatureModule(module)

                //}
                //Handler(Looper.getMainLooper()).postDelayed({

                //showFeatureModule(module)

                // }, 1000)
                //}
            }
            SplitInstallSessionStatus.FAILED -> {
                setStatus("FAILED")
            }
        }
    }

    private fun setStatus(label: String) {
        //binding.status.text = label
        Toast.makeText(context, label, Toast.LENGTH_SHORT).show()
    }

    private fun showFeatureModule(module: FeatureModule) {
        try {
            //Inject
            mApp!!.addModuleInjector(module)
            //

            this.startActivity(
                ActivityHelperKt.intentTo(
                    requireActivity(),
                    module as AddressableActivity
                )
            )
            //finish();
        } catch (e: Exception) {
            e.message?.let { Log.d("MainFragment", it) };
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }
}


