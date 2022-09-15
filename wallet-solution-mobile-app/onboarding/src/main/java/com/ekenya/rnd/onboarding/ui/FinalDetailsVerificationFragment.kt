package com.ekenya.rnd.onboarding.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.dashboard.DashBoardActivity
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.repositories.TestMainViewModel
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FinalDetailsVerificationFragmentBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*

class FinalDetailsVerificationFragment : BaseDaggerFragment() {
    private var binding: FinalDetailsVerificationFragmentBinding? = null
    private lateinit var testMainViewModel: TestMainViewModel
    private lateinit var viewModel: LoginViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChangeActionBarandBackArrowColor("#100fd6")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FinalDetailsVerificationFragmentBinding.inflate(layoutInflater)


        setupViewModel()
        //initializeUi()
        setupObservers()


        return binding!!.root
    }

    /*private fun initializeUi() {
        binding!!.btnVerifyIdentity.makeInvisible()
        binding!!.btnVerifyIdentity
            .setOnClickListener {
                setupObservers()


            }
    }*/


    private fun setupObservers() {

        val file = File(AppUtils.frontImagePath)
        val backfile = File(AppUtils.backImagePath)
        val mediaType = "image/png".toMediaTypeOrNull()
        val requestFile1 = file.asRequestBody(mediaType)
        val requestFile2 = backfile.asRequestBody(mediaType)

        val frontImage = MultipartBody.Part.createFormData("files", file.name, requestFile1)
        val backImage = MultipartBody.Part.createFormData("files", file.name, requestFile2)

        val parts: ArrayList<MultipartBody.Part> = ArrayList()
        parts.add(frontImage)
        parts.add(backImage)


        val userData = AppUtils.getUserData(context)
        testMainViewModel.registerUser(userData, parts).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {

                    Status.SUCCESS -> {
                        binding!!.progressbar.makeInvisible()
                        binding!!.percent.makeInvisible()

                        //val responsecode = it.data?.data?.response?.responseCode
                        val responsecode = it.data?.data?.responseCode
                        //toastMessage("${it.data?.data?.responseMessage}")

                        val otp = it.data?.data?.otp
                        val devicetoken = it.data?.data?.token

                        if (responsecode.equals("00")) {
                            Log.d("TAG", "tokens: $otp $devicetoken")

                            succesfulRegistrationDialog(
                                "You have been registered to Tollo Cash Wallet"
                            )


                            // findNavController().navigate(R.id.otpVerificationFragment)


                            SharedPreferencesManager.setTolloOtp(requireContext(), otp)
                            SharedPreferencesManager.setTolloDevicetoken(
                                requireContext(),
                                devicetoken
                            )

                        } else {

                            if (it.data?.data?.response?.responseMessage!!.contains("timed")) {
                                showTransactionErrorDialog(it.data?.data?.response?.responseMessage!!)

                            } else {
                                showErrorDialog(it.data?.data?.response?.responseMessage!!)
                            }
                        }


                        //SharedPreferencesManager.setOtpToken(requireContext(),it.data!!.data)


                    }
                    Status.ERROR -> {

                        binding!!.progressbar.makeInvisible()
                        showErrorDialog("Some Error Occured")

                    }
                    Status.LOADING -> {

                        binding!!.percent.makeVisible()
                        binding!!.progressbar.makeVisible()
                        binding!!.btnVerifyIdentity.makeInvisible()

                        val totalSeconds: Long = 30
                        val intervalSeconds: Long = 1

                        val timer: CountDownTimer = object :
                            CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                binding!!.progressbar.progress =
                                    ((totalSeconds * 1000 - millisUntilFinished) / 1000).toInt()
                                binding!!.percent.text =
                                    "${(totalSeconds * 1000 - millisUntilFinished) / 1000 * 2} %"

                            }

                            override fun onFinish() {

                            }
                        }
                        timer.start()


                    }
                }
            }
        })


    }


    fun succesfulRegistrationDialog(message: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.user_succesfully_registered)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAlldone = dialog.findViewById<Button>(R.id.btn_Proceed)
        val tvTitle = dialog.findViewById<TextView>(R.id.tv_sorry)
        val errorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)

        errorMessage.text = message
        // tvTitle.text =

        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, DashBoardActivity::class.java)
            startActivity(intent)
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, DashBoardActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }


    fun showTransactionErrorDialog(error: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.transaction_failed_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)
        btnAlldone.text = "RETRY"
        val errorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)
        val tvSorry = dialog.findViewById<TextView>(R.id.tv_sorry)
        tvSorry.text = error

        errorMessage.text = "Would you like to Retry?"

        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            // findNavController().popBackStack(R.id.navigation_home, false)
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()
            setupObservers()

        }
        //setDialogLayoutParams(dialog)
        dialog.show()
    }


    fun showErrorDialog(error: String) {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.error_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvErrorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)
        val dismissIcon = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        val dialogBtn_remove = dialog.findViewById<Button>(R.id.btn_Cancel)
        val dialogbtn_RecheckNumber = dialog.findViewById<Button>(R.id.btn_recheckNumber)
        val phoneNumber = dialog.findViewById<TextView>(R.id.tv_phoneConfirmation)

        phoneNumber.setText("Kindly recheck\n Your details")

        tvErrorMessage.setText(error)

        dismissIcon.setOnClickListener {
            dialog.dismiss()
        }

        dialogBtn_remove.setOnClickListener {
            dialog.dismiss()
        }
        dialogbtn_RecheckNumber.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.finalDetailsVerificationFragment_toAccountLookUp)
        }
        // setDialogLayoutParams(dialog)

        dialog.show()
    }


    private fun setupViewModel() {
        testMainViewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(TestMainViewModel::class.java)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper(com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)


    }

    override fun onStop() {
        super.onStop()
        //makeStatusBarTransparent()
    }

    override fun onResume() {
        super.onResume()
        extendStatusBarBackground()
        changeActionbarColor(Color.parseColor("#ffee1a23"))


    }


}