package com.ekenya.lamparam.ui.paybill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.fragment_pay_umeme.*


class PayUmeme : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var rootiew = inflater.inflate(R.layout.fragment_pay_umeme, container, false)
       // ((activity as LampMainActivity).showToolbar("Payment Page", "Please Enter your Meter Details"))
        return rootiew
    }
//nav_confirmationPaybill
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    //tvActionTitle.text = "Create student"
    //nav_schoolList
    btnPayumeme.setOnClickListener { findNavController().navigate(R.id.nav_confirmationPaybill) }
}


}