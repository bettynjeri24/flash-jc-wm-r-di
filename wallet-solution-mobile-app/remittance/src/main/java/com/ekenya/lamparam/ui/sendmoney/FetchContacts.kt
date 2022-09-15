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
import kotlinx.android.synthetic.main.fragment_fetch_contacts.*
import kotlinx.android.synthetic.main.header_layout.*


class FetchContacts : Fragment() {

    lateinit var navOptions: NavOptions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_fetch_contacts, container, false)
        //((activity as LampMainActivity).hideActionBar())
        navOptions = UtilityClass().getNavoptions()
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Enter Recipient Name"
        //nav_confirmPin
        //btn back
        ((activity as LampMainActivity). onBackClick(btn_back,view))

        var title = requireArguments().getString("title")
        var slogan = requireArguments().getString("slogan")//slogan

        var confBundle = requireArguments()
        layout_contact1.setOnClickListener { findNavController().navigate(R.id.nav_sendAmount,confBundle,
            navOptions
        ) }
        layout_contact2.setOnClickListener { findNavController().navigate(R.id.nav_sendAmount,confBundle,
            navOptions
        ) }
    }
}