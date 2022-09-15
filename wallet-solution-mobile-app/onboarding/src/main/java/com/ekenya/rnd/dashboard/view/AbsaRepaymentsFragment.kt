package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.databinding.FragmentAbsaRepaymentsBinding

class AbsaRepaymentsFragment : BaseDaggerFragment() {
    private lateinit var  binding: FragmentAbsaRepaymentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentAbsaRepaymentsBinding.inflate(inflater,container,false)

        return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }



}