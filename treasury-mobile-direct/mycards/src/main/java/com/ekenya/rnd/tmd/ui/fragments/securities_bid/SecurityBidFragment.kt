package com.ekenya.rnd.tmd.ui.fragments.securities_bid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentSecurityBidBinding

class SecurityBidFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentSecurityBidBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentSecurityBidBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            buttonContinue.setOnClickListener {
                val amount = textInputLaoutBidAmount.text.toString()
                val rate = textInputLayoutRate.text.toString()
                val bundle = arguments
                bundle?.apply {
                    putString("amount", amount)
                    putString("rate", rate)
                }
                findNavController().navigate(R.id.action_securityBidFragment_to_confirmFragment, bundle)
            }
        }
    }
}
