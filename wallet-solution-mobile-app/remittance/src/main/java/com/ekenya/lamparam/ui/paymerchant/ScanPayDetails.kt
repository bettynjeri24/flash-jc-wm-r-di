package com.ekenya.lamparam.ui.paymerchant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.fragment_scan_pay_details.*
import kotlinx.android.synthetic.main.header_layout.*


class ScanPayDetails : Fragment() {

    lateinit var navOptions: NavOptions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var rootView = inflater.inflate(R.layout.fragment_scan_pay_details, container, false)
        navOptions = UtilityClass().getNavoptions()
       // ((activity as LampMainActivity).hideActionBar())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Limpopo Merchant"
        ((activity as LampMainActivity).onBackClick(btn_back, view))

        //btnScanDetails.setOnClickListener {  }
        btnScanDetails.setOnClickListener { findNavController().navigate(R.id.nav_merchantAmount,null, navOptions) }
    }

}