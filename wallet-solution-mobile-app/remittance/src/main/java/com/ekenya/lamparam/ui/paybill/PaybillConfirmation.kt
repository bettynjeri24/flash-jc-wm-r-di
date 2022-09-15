package com.ekenya.lamparam.ui.paybill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.fragment_confirmation_screen.*
import kotlinx.android.synthetic.main.header_layout.*


class PaybillConfirmation : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_paybill_confirmation, container, false)
       // ((activity as LampMainActivity).hideActionBar())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Confirm & Make Payment"
        //nav_confirmPin
        btnConfirmTrans.setOnClickListener { findNavController().navigate(R.id.nav_confirmPin) }
    }
}