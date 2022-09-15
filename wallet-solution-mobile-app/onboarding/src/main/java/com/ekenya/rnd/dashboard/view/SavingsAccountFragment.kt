package com.ekenya.rnd.dashboard.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekenya.rnd.onboarding.R

class SavingsAccountFragment : Fragment() {


    companion object {
        fun newInstance() = SavingsAccountFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.savings_account_fragment, container, false)
    }



}