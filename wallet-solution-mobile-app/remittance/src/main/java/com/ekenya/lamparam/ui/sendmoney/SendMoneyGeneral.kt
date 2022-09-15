package com.ekenya.lamparam.ui.sendmoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.main.LampMainViewModel
import com.ekenya.lamparam.databinding.FragmentReceiveMoneyConfirmationBinding
import com.ekenya.lamparam.databinding.FragmentSendMoneyGeneralBinding
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.fragment_receive_from_ecobank.*
import kotlinx.android.synthetic.main.fragment_send_money_general.*
import kotlinx.android.synthetic.main.fragment_set_receive_amount.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*


class SendMoneyGeneral : Fragment() {

    lateinit var navOptions: NavOptions
    lateinit var lampMainViewModel: LampMainViewModel
    private lateinit var binding: FragmentSendMoneyGeneralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lampMainViewModel = ViewModelProvider((activity as LampMainActivity)).get(LampMainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSendMoneyGeneralBinding.inflate(inflater)

         navOptions = UtilityClass().getNavoptions()
       // ((activity as LampMainActivity).hideActionBar())

        binding.mtoolBar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var confBundle = requireArguments()
        var title = confBundle.getString("title")
        var slogan = confBundle.getString("slogan")//slogan
        tvActionTitleSendMoney.text = "Send money via $title"


        var purposeList = ArrayList<String>()
        purposeList.add("Land Purchase")
        purposeList.add("House Purchase")
        purposeList.add("House Building")
        purposeList.add("Loan Repayment")
        purposeList.add("Education Expense")
        purposeList.add("Medical Expenses")
        purposeList.add("Family Expenses")
        purposeList.add("Savings Deposits")
        purposeList.add("Marriage Expenses")
        purposeList.add("Travelling Expenses")
        purposeList.add("Gifts")
        //mobileMoneyServicesList.add("MTN Money")
        val purposeAdapter = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_list_item_1,
                purposeList
            )
        }
        etSendPurpose.setAdapter(purposeAdapter)
        etSendPurpose.setOnFocusChangeListener(View.OnFocusChangeListener {
                v, hasFocus -> if (hasFocus) etSendPurpose.showDropDown() })
        etSendPurpose.setOnTouchListener(View.OnTouchListener { v, event ->
            etSendPurpose.showDropDown()
            false
        })

        /////////////////

        //nav_setAmount
        btnSendMoney.setOnClickListener {
            val amt = valid()
            if(amt.isNotEmpty()){

                confBundle.putString("idNumber",tlIdNoV.editText!!.text.toString())
                confBundle.putString("name",tlFullName.editText!!.text.toString())
                confBundle.putString("phoneNumber",etPhoneNo.text.toString())

                lampMainViewModel.receiverName.postValue(etFullName.text.toString())
                lampMainViewModel.receiverPhoneNumber.postValue(etPhoneNo.text.toString())
                lampMainViewModel.expectedAmount.postValue(amt.toDouble())
                lampMainViewModel.purpose.postValue(etSendPurpose.text.toString())
                ////////
                findNavController().navigate(
                    R.id.nav_sendConfirmation,
                    confBundle,
                    navOptions
                )
            }else{
                Toast.makeText(requireContext(), "Amount is required, ", Toast.LENGTH_LONG).show()
            }


        }
    }

    private fun valid(): String {
        val amt = etSendAmount.text.toString()
        if (amt.isEmpty()){
            Toast.makeText(requireContext(), "Amount required", Toast.LENGTH_LONG).show()
            return ""
        }
        return amt
    }
}