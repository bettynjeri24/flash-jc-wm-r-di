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
import kotlinx.android.synthetic.main.fragment_receive_from_ecobank.*
import kotlinx.android.synthetic.main.header_layout.*


class MoneyGramrcvMoney : Fragment() {
    lateinit var navOptions: NavOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_money_gramrcv_money, container, false)
        navOptions = UtilityClass().getNavoptions()
       // ((activity as LampMainActivity).hideActionBar())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var title = requireArguments().getString("title")
        var slogan  =requireArguments().getString("slogan")//slogan
        tvActionTitle.text = "$title$slogan"
        var confBundle = requireArguments()
        // confBundle.putString("")
        //bsck button
        //btn_back
        ((activity as LampMainActivity). onBackClick(btn_back,view))


        //nav_setAmount
        btnReceiveEcobnk.setOnClickListener { findNavController().navigate(R.id.nav_setAmount,confBundle, navOptions) }
    }

}