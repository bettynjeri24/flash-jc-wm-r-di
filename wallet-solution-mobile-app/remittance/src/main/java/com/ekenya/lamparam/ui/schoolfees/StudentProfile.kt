package com.ekenya.lamparam.ui.schoolfees

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
import kotlinx.android.synthetic.main.fragment_student_profile.*
import kotlinx.android.synthetic.main.header_layout.*


class StudentProfile : Fragment() {

    lateinit var navOptions: NavOptions
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_student_profile, container, false)
       // ((activity as LampMainActivity).hideActionBar())
        navOptions = UtilityClass().getNavoptions()
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Alladin Nginyo"
        //back button
        ((activity as LampMainActivity). onBackClick(btn_back,view))
        //nav_paySchoolFee
        cardPayFee.setOnClickListener { findNavController().navigate(R.id.nav_paySchoolFee,null,navOptions) }
        //nav_schoolFeeBalance
        cardBalance.setOnClickListener {  findNavController().navigate(R.id.nav_schoolFeeBalance,null,navOptions) }
    }


}