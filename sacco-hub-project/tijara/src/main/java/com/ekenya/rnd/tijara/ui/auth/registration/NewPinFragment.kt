package com.ekenya.rnd.tijara.ui.auth.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.MainActivity
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentDefaultPinBinding
import com.ekenya.rnd.tijara.requestDTO.LoginDTO
import com.ekenya.rnd.tijara.utils.*
import timber.log.Timber


class NewPinFragment : Fragment() {
private lateinit var pinBinding:FragmentDefaultPinBinding
private lateinit var pinViewmodel:PinValidationViewmodel
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pinBinding= FragmentDefaultPinBinding.inflate(layoutInflater)
        pinViewmodel=ViewModelProvider(requireActivity()).get(PinValidationViewmodel::class.java)
        pinBinding.tvEnterPin.setText("Create a new 4-digit pin")
        return pinBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pinBinding.apply {
            ivBack.setOnClickListener { findNavController().navigateUp() }
            btnOne.setOnClickListener { controlPinPad2("1") }
            btnTwo.setOnClickListener{controlPinPad2("2")}
            btnThree.setOnClickListener{ controlPinPad2("3")}
            btnFour.setOnClickListener{ controlPinPad2("4")}
            btnFive.setOnClickListener { controlPinPad2("5") }
            btnSix.setOnClickListener{controlPinPad2("6")}
            btnSeven.setOnClickListener { controlPinPad2("7") }
            btnEight.setOnClickListener{controlPinPad2("8")}
            btnNine.setOnClickListener{ controlPinPad2("9")}
            btnZero.setOnClickListener{ controlPinPad2("0")}
            btnDelete.setOnClickListener { deletePinEntry() }


        }




    }
    private fun controlPinPad2(entry: String){
        pinBinding.apply {
            if (one1==null) {
                one1 = entry
                pin1.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }else if (two2==null){
                two2=entry
                pin2.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }else if (three3==null){
                three3=entry
                pin3.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }else if (four4==null){
                four4=entry
                pin4.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }
            if (mConfirmPin == null) {
                mConfirmPin = entry
            } else {
                mConfirmPin += entry
            }
            if (mConfirmPin!!.length == 4) {
                Constants.PIDNO=mConfirmPin.toString()
                findNavController().navigate(R.id.action_newPinFragment_to_confirmPinFragment)
                clearPin()
            }

        }


    }

    private fun deletePinEntry() {
        pinBinding.apply {
            if (mConfirmPin != null && mConfirmPin!!.length > 0) {
                mConfirmPin = mConfirmPin!!.substring(0, mConfirmPin!!.length - 1)
            }
            if (four4 != null) {
                pin4.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                four4 = null
            } else if (three3 != null) {
                pin3.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                three3 = null
            } else if (two2 != null) {
                pin2.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                two2 = null
            } else if (one1 != null) {
                pin1.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                one1 = null
            }
        }
    }
    private fun clearPin(){
        pinBinding.apply {
            pin1.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            one1 = null
            pin2.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            two2 = null
            pin3.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            three3 = null
            pin4.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            four4 = null
            mConfirmPin=null
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
    }}

