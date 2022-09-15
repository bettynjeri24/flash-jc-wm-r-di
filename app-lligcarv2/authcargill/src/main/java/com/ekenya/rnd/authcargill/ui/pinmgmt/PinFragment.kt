package com.ekenya.rnd.authcargill.ui.pinmgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.databinding.FragmentPinBinding
import org.json.JSONObject


class PinFragment : Fragment() {
    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!
    private var input_one_EditText2: ImageView? = null
    private var input_two_EditText2: ImageView? = null
    private var input_three_EditText2: ImageView? = null
    private var input_four_EditText2: ImageView? = null
    private var input_five_EditText2: ImageView? = null

    lateinit var delete_pin_pad: ImageView
    private var one1: String? = null

    /**
     * The Two.
     */
    private var two2: String? = null

    /**
     * The Three.
     */
    private var three3: String? = null

    /**
     * The Four.
     */
    private var four4: String? = null
    /**
     * The Four.
     */
    private var five5: String? = null
    private var mConfirmPin: String? = null
    lateinit var requestJson: JSONObject

    lateinit var pdialog: SweetAlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_pin, container, false)
        _binding = FragmentPinBinding.inflate(inflater, container, false)
       // (activity as MainActivity?)!!.hideToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var transJson = requireArguments().getString("requestJson")
        requestJson = JSONObject(transJson)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        input_one_EditText2 = view.findViewById(R.id.input_one)
        input_two_EditText2 = view.findViewById(R.id.input_two)
        input_three_EditText2 = view.findViewById(R.id.input_three)
        input_four_EditText2 = view.findViewById(R.id.input_four)
        input_five_EditText2 = view.findViewById(R.id.input_five)
        delete_pin_pad = view.findViewById(R.id.delete_pin_pad)
        initUI()

        binding.pinPadOk.setOnClickListener {

            if (mConfirmPin?.isNotBlank() == true) {
                requestJson.put("pin", mConfirmPin)
                var endpoint = requestJson.getString("endPoint")//ApiEndpointObj.verifyAddTelcoAccount
                // (activity as MainActivity?)!!.navigationMgmt()
            }

        }
    }



    private fun initUI() {
       // binding.tvTitle.setText(getString(R.string.we_ve_sent_an_ottc_to, phone))
       // binding.miniTab.setText(label)
        binding.btnNumberOne.setOnClickListener { controlPinPad2("1") }
        binding.btnNumberTwo.setOnClickListener {  controlPinPad2("2") }
        binding.btnNumberThree.setOnClickListener {controlPinPad2("3") }
        binding.btnNumberFour.setOnClickListener {  controlPinPad2("4") }
        binding.btnNumberFive.setOnClickListener {  controlPinPad2("5") }
        binding.btnNumberSix.setOnClickListener {  controlPinPad2("6") }
        binding.btnNumberSeven.setOnClickListener {  controlPinPad2("7") }
        binding.btnNumberEight.setOnClickListener {  controlPinPad2("8") }
        binding.btnNumberNine.setOnClickListener {  controlPinPad2("9") }
        binding.btnNumberZero.setOnClickListener {  controlPinPad2("0") }
        binding.deletePinPad.setOnClickListener {  deletePinEntry() }
    }
    private fun controlPinPad2(entry: String) {
        if (one1 == null) {
            input_one_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_star)
            one1 = entry
        } else if (two2 == null) {
            input_two_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_star)
            two2 = entry
        } else if (three3 == null) {
            input_three_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_star)
            three3 = entry
        } else if (four4 == null) {
            input_four_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_star)
            four4 = entry
        }
        else if (five5 == null) {
            input_five_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_star)
            five5 = entry
        }

        if (mConfirmPin == null) {
            mConfirmPin = entry
        } else {
            mConfirmPin = mConfirmPin + entry
        }
        if (mConfirmPin!!.length == 5) {
            binding.pinPadOk!!.isEnabled = true
        }
    }
    /**
     * pinpad control
     * @param entry the entry
     */

    private fun deletePinEntry() {
        if (mConfirmPin != null && mConfirmPin!!.length > 0) {
            mConfirmPin = mConfirmPin!!.substring(0, mConfirmPin!!.length - 1)
        }
        if (four4 != null) {
            input_four_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_underscore)
            four4 = null
        } else if (three3 != null) {
            input_three_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_underscore)
            three3 = null
        } else if (two2 != null) {
            input_two_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_underscore)
            two2 = null
        } else if (one1 != null) {
            input_one_EditText2!!.setImageResource(com.ekenya.rnd.common.R.mipmap.ic_underscore)
            one1 = null
        }
    }
   

}