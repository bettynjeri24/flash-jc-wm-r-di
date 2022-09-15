package com.ekenya.lamparam.ui.loan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.fragment_micro_loan_menu.*
import kotlinx.android.synthetic.main.header_layout.*


class MicroLoanMenu : Fragment() {

    lateinit var navOptions: NavOptions
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_micro_loan_menu, container, false)
       // ((activity as LampMainActivity).hideActionBar())
        navOptions = UtilityClass().getNavoptions()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = "Micro Loans"
        //nav_applyLoan
        cardApplyLoan.setOnClickListener {
            findNavController().navigate(R.id.nav_applyLoan,
                null,
                navOptions
            )
        }

        cardSalesOrder.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()
        }

        cardReturns.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()
        }

    }
}