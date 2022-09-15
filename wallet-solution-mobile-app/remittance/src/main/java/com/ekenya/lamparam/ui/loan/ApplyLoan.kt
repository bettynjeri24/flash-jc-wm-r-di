package com.ekenya.lamparam.ui.loan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.header_layout.*


class ApplyLoan : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_apply_loan, container, false)
       // ((activity as LampMainActivity).hideActionBar())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Apply Loan"
        //btn back
        ((activity as LampMainActivity).onBackClick(btn_back, view))
    }

}