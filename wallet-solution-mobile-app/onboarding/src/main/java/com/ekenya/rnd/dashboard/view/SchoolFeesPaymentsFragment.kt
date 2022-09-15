package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentSchoolFeesPaymentsBinding
import com.ekenya.rnd.onboarding.databinding.FragmentTrafficFinesBinding
import kotlinx.android.synthetic.main.manual_verification_dialog.*


class SchoolFeesPaymentsFragment : Fragment() {

    private lateinit var  binding: FragmentSchoolFeesPaymentsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSchoolFeesPaymentsBinding.inflate(layoutInflater)
        setonClickListeners()
        return binding.root
    }

    private fun setonClickListeners() {
        binding.btnContinue.setOnClickListener{
            toastMessage("Feature Coming Soon")
        }
    }

}