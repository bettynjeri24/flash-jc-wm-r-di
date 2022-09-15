package com.ekenya.lamparam.ui.schoolfees

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.header_layout.*


class SchoolFeeBalance : Fragment() {

    lateinit var navOptions: NavOptions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_school_fee_balance, container, false)
       // ((activity as LampMainActivity).hideActionBar())
        navOptions = UtilityClass().getNavoptions()
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Alladin Nginyo"
        //back button
        ((activity as LampMainActivity).onBackClick(btn_back, view))
    }


}