package com.ekenya.rnd.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.onboarding.databinding.MakeDepositFragmentBinding

class MakeDepositFragment : Fragment() {
    private var binding: MakeDepositFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = MakeDepositFragmentBinding.inflate(layoutInflater)
        binding!!.btnContinue.setOnClickListener{
           // Navigation.findNavController(it).navigate(R.id.action_makedeposit_to_dashboard)
        }
    return  binding!!.root

    }





}