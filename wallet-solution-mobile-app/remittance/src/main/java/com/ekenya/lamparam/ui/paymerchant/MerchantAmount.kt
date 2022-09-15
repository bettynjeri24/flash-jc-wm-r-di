package com.ekenya.lamparam.ui.paymerchant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.fragment_set_receive_amount.*
import kotlinx.android.synthetic.main.header_layout.*


class MerchantAmount : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView =  inflater.inflate(R.layout.fragment_merchant_amount, container, false)
       // ((activity as LampMainActivity).hideActionBar())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Guinea Bisau Maize millers"
        //back button
        ((activity as LampMainActivity). onBackClick(btn_back,view))
        btSetAmount.setOnClickListener { findNavController().navigate(R.id.nav_merchantConfirmation) }
    }
}