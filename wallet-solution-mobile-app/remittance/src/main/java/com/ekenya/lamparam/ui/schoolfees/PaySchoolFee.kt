package com.ekenya.lamparam.ui.schoolfees

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.fragment_pay_school_fee.*
import kotlinx.android.synthetic.main.fragment_pay_school_fee.spn_payfrom
import kotlinx.android.synthetic.main.header_layout.*


class PaySchoolFee : Fragment() {

    lateinit var navOptions: NavOptions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_pay_school_fee, container, false)
        //((activity as LampMainActivity).hideActionBar())
        navOptions = UtilityClass().getNavoptions()
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Pay School fee"
        //back button
        ((activity as LampMainActivity).onBackClick(btn_back, view))
        btnPayFee.setOnClickListener { findNavController().navigate(R.id.nav_confirmPin,null,navOptions) }

        val accountList: ArrayList<String> = ArrayList()
        accountList.add("Lamparam account 2727827827")
        accountList.add("Orange Money")
        accountList.add("MTN Money")

        val adapter2 = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, accountList) }
        adapter2?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_payfrom.adapter = adapter2

    }

}