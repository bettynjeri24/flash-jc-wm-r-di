package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentAirtimeBinding


class AirtimeFragment : Fragment() {
    private lateinit var binding: FragmentAirtimeBinding

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
        // Inflate the layout for this fragmenta
        binding = FragmentAirtimeBinding.inflate(inflater,container,false)
        setClickListeners()
        return binding.root
    }

    private fun setClickListeners() {
        binding.safAirtime.setOnClickListener{
            findNavController().navigate(R.id.orangeAirtimeFragment)
        }
        binding.telebirrAirtime.setOnClickListener{
           toastMessage("Feature Coming Soon")
        }
        binding.ethiopiatel.setOnClickListener{
            findNavController().navigate(R.id.mascomAirtimeFragment)
        }
    }

}