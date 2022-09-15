package com.ekenya.lamparam.ui.deposit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.databinding.FragmentCashDepositRemittanceBinding
import com.ekenya.lamparam.utilities.StaticData
import kotlinx.android.synthetic.main.header_layout.*


class CashDeposit : Fragment() {
    private var _binding: FragmentCashDepositRemittanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCashDepositRemittanceBinding.inflate(inflater)
       // ((activity as LampMainActivity).hideActionBar())

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutHeader.tvActionTitle.text = getString(R.string.cash_deposit)
        //btn back
        var listPay = StaticData().paymentModeList()
        ((activity as LampMainActivity). onBackClick(btn_back,view))
        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, listPay) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDepositFrom.adapter = adapter
        //nav_confirmPin
        binding.btnDeposit.setOnClickListener { findNavController().navigate(R.id.nav_confirmPin) }
        //paymentMode
    }
}