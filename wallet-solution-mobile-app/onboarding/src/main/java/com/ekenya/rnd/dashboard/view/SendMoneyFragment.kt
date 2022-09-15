package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.dashboard.utils.setStatusBArColor
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.R

import com.ekenya.rnd.onboarding.databinding.SendMoneyFragmentBinding

class SendMoneyFragment : BaseDaggerFragment() {
    private var _binding: SendMoneyFragmentBinding? = null


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SendMoneyFragmentBinding.inflate(inflater, container, false)

        setListeners()


        return binding.root

    }

    private fun setListeners() {
        binding.tvCreatebankrecipientcard.setOnClickListener{
            findNavController().navigate(R.id.action_sendMoneyFragment2_to_sendToBankFragment)
        }
        binding.tvToMobilecard.setOnClickListener{
            findNavController().navigate(R.id.action_sendMoneyFragment2_to_sendToMobileFragment)
        }
        binding.toWalletcard.setOnClickListener{
            findNavController().navigate(R.id.action_sendMoneyFragment2_to_sendToWalletFragment)
        }    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onStop() {
        super.onStop()
        showSupportActionBar()
    }
    override fun onResume() {
        super.onResume()
        showSupportActionBar()
    }

}