package com.ekenya.lamparam.ui.withdraw

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.StaticData
import kotlinx.android.synthetic.main.fragment_cash_withdrawal.*
import kotlinx.android.synthetic.main.header_layout.*

/**
 * Fragment for cash withdrawal
 */
class CashWithdrawal : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_cash_withdrawal, container, false)
       // ((activity as LampMainActivity).hideActionBar())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = getString(R.string.cash_withdraw)
        //btn back
        ((activity as LampMainActivity).onBackClick(btn_back, view))
        val listPay = StaticData().paymentModeList()
        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, listPay) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_withdraw.adapter = adapter
        //nav_confirmPin
        btnWithdraw.setOnClickListener { findNavController().navigate(R.id.nav_confirmPin) }
    }
}