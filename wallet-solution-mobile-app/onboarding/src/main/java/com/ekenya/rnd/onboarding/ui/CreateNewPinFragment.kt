package com.ekenya.rnd.onboarding.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.DashBoardActivity
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.CreateNewPinFragmentBinding

class CreateNewPinFragment : Fragment() {
    private lateinit var binding: CreateNewPinFragmentBinding
    private lateinit var pin :String
    private var one1: String? = null
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
            binding = CreateNewPinFragmentBinding.inflate(layoutInflater)

            // Inflate the layout for this fragment
            initUI()


            return binding!!.root
        }
        private fun initUI() {
            binding.apply {
                binding?.btnOne?.setOnClickListener { controlPinPad2("1") }
                binding?.btnTwo?.setOnClickListener { controlPinPad2("2") }
                binding?.btnThree?.setOnClickListener { controlPinPad2("3") }
                binding?.btnFour?.setOnClickListener { controlPinPad2("4") }
                binding?.btnFive?.setOnClickListener { controlPinPad2("5") }
                binding?.btnSix?.setOnClickListener { controlPinPad2("6") }
                binding?.btnSeven?.setOnClickListener { controlPinPad2("7") }
                binding?.btnEight?.setOnClickListener { controlPinPad2("8") }
                binding?.btnNine?.setOnClickListener { controlPinPad2("9") }
                binding?.btnZero?.setOnClickListener { controlPinPad2("0") }
                binding?.btnDelete?.setOnClickListener { deletePinEntry() }
            }
        }
        private fun controlPinPad2(entry: String) {
            binding.apply {
                if (one1 == null) {
                    binding.pin1.background = context?.let { ContextCompat.getDrawable(it, R.drawable.activestepsbackground) };
                    one1 = entry
                } else if (two2 == null) {
                    binding.pin2.background = context?.let { ContextCompat.getDrawable(it, R.drawable.activestepsbackground) };
                    two2 = entry
                } else if (three3 == null) {
                    binding.pin3.background = context?.let { ContextCompat.getDrawable(it, R.drawable.activestepsbackground) };
                    three3 = entry
                } else if (four4 == null) {
                    binding.pin4.background = context?.let { ContextCompat.getDrawable(it, R.drawable.activestepsbackground) };
                    four4 = entry
                    isDone = true
                }

                if (isDone){

                    if (validDetails()){
                                pin= one1+two2+three3+four4

                        context?.let { SharedPreferencesManager.setPin(it,pin) }
                               // mainViewModel.resetPin(user_id,pin)
                       // pinCapturedDialog("Pin Captured Succesfully, Proceed to Pin Confirmation")
                        toastMessage("Pin Captured Succesfully")
                        findNavController().navigate(R.id.actioncreatenewpin2_toconfirmnewpin)

                            }
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


    fun pinCapturedDialog(message: String) {
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
           /* val intent = Intent(activity, DashBoardActivity::class.java)
            startActivity(intent)*/
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.actioncreatenewpin2_toconfirmnewpin)

            /*val intent = Intent(activity, DashBoardActivity::class.java)
            startActivity(intent)*/
        }
        dialog.show()
    }



    private fun deletePinEntry() {
            binding.apply {
                if (mConfirmPin != null && mConfirmPin!!.length > 0) {
                    mConfirmPin = mConfirmPin!!.substring(0, mConfirmPin!!.length - 1)
                }
                if (four4 != null) {
                    binding.pin4.background=resources.getDrawable(R.drawable.inactive_pin_bg)
                    four4 = null
                    isDone = false

                } else if (three3 != null) {
                    binding.pin3.background=resources.getDrawable(R.drawable.inactive_pin_bg)
                    three3 = null
                } else if (two2 != null) {
                    binding.pin2.background=resources.getDrawable(R.drawable.inactive_pin_bg)
                    two2 = null
                } else if (one1 != null) {
                    binding.pin1.background=resources.getDrawable(R.drawable.inactive_pin_bg)
                    one1 = null
                }
            }
        }
    private fun validDetails(): Boolean {
        /*otpdigit1 = binding?.tilDigit1?.text.toString().trim()
        otpdigit2 = binding?.tilDigit2?.text.toString().trim()
        otpdigit3 = binding?.tilDigit3?.text.toString().trim()
        otpdigit4 = binding?.tilDigit4?.text.toString().trim()
        otpdigit5 = binding?.tilDigit5?.text.toString().trim()
        if(user_id.isNullOrBlank() || user_id == "-1"){
            //binding?.tilFullName?.error = "Please enter a valid Name"
            return false
        }
        if(otpdigit1.isNullOrBlank()){
            binding?.tilDigit1?.error = "Please retry otp"
            binding?.tilDigit1?.error
            return false
        }*/


        return true
    }


}


