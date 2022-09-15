package com.ekenya.lamparam.ui.receiveMoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.FieldValidation
import com.ekenya.lamparam.utilities.UtilityClass
import com.ekenya.lamparam.activities.main.LampMainViewModel
import com.ekenya.lamparam.databinding.FragmentReceiveMoneyConfirmationBinding
import com.ekenya.lamparam.databinding.FragmentSetReceiveAmountBinding
import com.ekenya.lamparam.model.RemittanceTransactionLookupReq
import com.ekenya.lamparam.model.response.RemittanceTransactionLookupResponse
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Constants
import kotlinx.android.synthetic.main.fragment_send_money_general.*
import kotlinx.android.synthetic.main.fragment_set_receive_amount.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class SetReceiveAmount : Fragment() {

    lateinit var lampMainViewModel: LampMainViewModel
    private lateinit var binding: FragmentSetReceiveAmountBinding
    private var phoneNumber = ""
    private var selectedProvider = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentSetReceiveAmountBinding.inflate(inflater)

        binding.appBar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        phoneNumber = SharedPreferencesManager.getPhoneNumber(requireContext()).toString()

        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
        {
            selectedProvider = requireArguments().getString("title").toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lampMainViewModel = ViewModelProvider((activity as LampMainActivity)).get(LampMainViewModel::class.java)

        btSetAmount.setOnClickListener {
            if (validateFields()) {

                doTransactionLookup(
                    RemittanceTransactionLookupReq(
                        phoneNumber,
                        binding.etReceiveAmount.text.toString(),
                        "1",
                        binding.etPurpose.text.toString(),
                        binding.etRefNo.text.toString(),
                        "father"
                    )
                )

            }
        }

        var purposeList = ArrayList<String>()
        purposeList.add("Land Purchase")
        purposeList.add("House Purchase")
        purposeList.add("Loan Repayment")
        purposeList.add("Education Expense")
        purposeList.add("Medical Expenses")
        purposeList.add("Family Support")
        purposeList.add("Savings Deposits")
        purposeList.add("Travelling Expenses")

        val purposeAdapter = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_list_item_1,
                purposeList
            )
        }
        etPurpose.setAdapter(purposeAdapter)
        etPurpose.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) etPurpose.showDropDown() }
        etPurpose.setOnTouchListener(View.OnTouchListener { v, event ->
            etPurpose.showDropDown()
            false
        })
    }

    private fun validateFields(): Boolean {
        val validMsg = FieldValidation.VALIDINPUT
        val amount = etReceiveAmount.text.toString()
        val validAmount = FieldValidation().validAmount(amount, "Amount")

//        if (!validAmount.contentEquals(validMsg)) {
//            etReceiveAmount.requestFocus()
//            tlReceiveAmount.error = validAmount
//            return false
//        } else {
//            tlReceiveAmount.error = null
//            etReceiveAmount.clearFocus()
//        }

        val refNumber = etRefNo.text.toString()
        val validRefNo =
            FieldValidation().validRefNumber(refNumber, "Ref Number")

        if (!validRefNo.contentEquals(validMsg)) {
            etRefNo.requestFocus()
            tlRefNo.error = validRefNo
            return false
        } else {
            tlRefNo.error = null
            etRefNo.clearFocus()
        }

        return true
    }

    private fun doTransactionLookup(req:RemittanceTransactionLookupReq)
    {
        Constants.callDialog2("Please wait...", requireContext())
         lampMainViewModel.doTransactionLookup(req).observe(viewLifecycleOwner) { myAPIResponse ->
            if(myAPIResponse.requestName == "RemittanceTransactionLookupReq") {
                Constants.cancelDialog()
                if (myAPIResponse.code == 200)
                {
                    val resp = myAPIResponse.responseObj as RemittanceTransactionLookupResponse
                    if(resp.status == 200){
                        lampMainViewModel.purpose.postValue(etPurpose.text.toString())
                        lampMainViewModel.expectedAmount.postValue(etReceiveAmount.text.toString().toDouble())
                        var confBundle = requireArguments()
                        confBundle.putInt("next", R.id.nav_receiveConfirmation)
                        confBundle.putString("otp",resp.otp)

                        findNavController().navigate(R.id.nav_receiveConfirmation, confBundle, navOptions)
                    }
                    else if(resp.status == 401)
                    {
                        UtilityClass().confirmTransactionEnd(
                            binding.root,
                            "Info",
                            "Details do not match please confirm and try again",
                            requireActivity()
                        )
                    }

                }

            }
        }
    }


}