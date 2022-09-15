package com.ekenya.lamparam.ui.receiveMoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.activities.main.LampMainViewModel
import com.ekenya.lamparam.databinding.FragmentReceiveMoneyConfirmationBinding
import com.ekenya.lamparam.model.ModelSenderReceiverInfo
import com.ekenya.lamparam.ui.sendmoney.SendMoneyInfoAdapter
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.fragment_confirmation_screen.btnConfirmTrans
import kotlinx.android.synthetic.main.fragment_receive_money_confirmation.*


class ReceiveMoneyConfirmation : Fragment() {

    lateinit var navOptions: NavOptions
    private lateinit var binding: FragmentReceiveMoneyConfirmationBinding
    private lateinit var lampMainViewModel: LampMainViewModel
    private lateinit var sendMneyAdapter: SendMoneyInfoAdapter
    private var gross = 0.0
    private var otp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lampMainViewModel =
            ViewModelProvider((activity as LampMainActivity)).get(LampMainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReceiveMoneyConfirmationBinding.inflate(inflater)

        binding.appbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navOptions = UtilityClass().getNavoptions()

        var confBundle = requireArguments()
        otp = confBundle.getString("otp").toString()

        initRView()

        lampMainViewModel.expectedAmount.observe(viewLifecycleOwner) {
            gross = it
            updateFields()
        }

        rg_payment.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_wallet -> tl_account.visibility = View.GONE
                R.id.rb_acc -> populateBankAccsList()
                R.id.rb_mmoney -> populateMobileMoneyList()
            }
        }

        btnConfirmTrans.setOnClickListener {
            val b = Bundle()
            b.putString("otp",otp.toString())
            findNavController().navigate(
                R.id.nav_secretPin,
                b,
                navOptions
            )
        }
    }

    private fun populateBankAccsList()
    {
        tl_account.visibility = View.VISIBLE

        var bankAccountsList = ArrayList<String>()
        bankAccountsList.add("0220567234")
        bankAccountsList.add("0220008900")
        bankAccountsList.add("0080876321")
        val purposeAdapter = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_list_item_1,
                bankAccountsList
            )
        }

        et_accts.setAdapter(purposeAdapter)
        et_accts.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus -> if (hasFocus) et_accts.showDropDown() }
        et_accts.setOnTouchListener(View.OnTouchListener { v, event ->
            et_accts.showDropDown()
            false
        })
    }

    private fun populateMobileMoneyList()
    {
        var bankAccountsList = ArrayList<String>()
        bankAccountsList.add("AirtelTigo")
        bankAccountsList.add("MTN - Ghana")
        val purposeAdapter = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_list_item_1,
                bankAccountsList
            )
        }

        et_accts.setAdapter(purposeAdapter)
        et_accts.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus -> if (hasFocus) et_accts.showDropDown() }
        et_accts.setOnTouchListener(View.OnTouchListener { v, event ->
            et_accts.showDropDown()
            false
        })
    }

    private fun updateFields() {
        tvGrossValue.text = "GHS $gross"
        if (gross > 40) {
            tvActualTotal.text = "GHS ${gross - 40}"
        }
    }

    private fun initRView() {

        val list = ArrayList<ModelSenderReceiverInfo>()
        list.add(ModelSenderReceiverInfo("Recipient","Daniel nzuma",111111,719147810))
        list.add(ModelSenderReceiverInfo("Sender","Kevin Kiduku",32132312,+254798123657))
        binding.rvReceiverInfo.layoutManager = GridLayoutManager(requireContext(), 2)

        sendMneyAdapter = list?.let { SendMoneyInfoAdapter(requireActivity(),list) }!!
        binding.rvReceiverInfo.adapter = sendMneyAdapter
        sendMneyAdapter.notifyDataSetChanged()
    }

}