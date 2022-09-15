package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.databinding.FragmentEthioTelComPostpaidBinding

class EthioTelComPostpaidFragment : Fragment() {

    private lateinit var binding: FragmentEthioTelComPostpaidBinding
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
        binding = FragmentEthioTelComPostpaidBinding.inflate(layoutInflater)
        setonClickListeners()
        return binding.root
    }

    private fun setonClickListeners() {
        binding.btnContinue.setOnClickListener {
            toastMessage("Feature Coming Soon")
        }
    }


}