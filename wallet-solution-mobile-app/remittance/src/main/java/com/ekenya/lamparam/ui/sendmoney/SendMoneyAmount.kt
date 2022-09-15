package com.ekenya.lamparam.ui.sendmoney

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
import kotlinx.android.synthetic.main.fragment_set_receive_amount.*
import kotlinx.android.synthetic.main.header_layout.*


class SendMoneyAmount : Fragment() {

    lateinit var navOptions: NavOptions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_send_money_amount, container, false)
      //  ((activity as LampMainActivity).hideActionBar())
        navOptions = UtilityClass().getNavoptions()
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Send to Alladin"
        //btn back
        ((activity as LampMainActivity). onBackClick(btn_back,view))
        var confBundle = requireArguments()
        btSetAmount.setOnClickListener { findNavController().navigate(R.id.nav_sendConfirmation,confBundle, navOptions) }
    }
}