package com.ekenya.rnd.tmd.ui.fragments.selfie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.mycards.databinding.FragmentSelfieBinding
import com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment.OnboardViewModel

class SelfieFragment(private val viewmodel: OnboardViewModel, private val next: () -> Unit) : Fragment() {

    private lateinit var binding: FragmentSelfieBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentSelfieBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            button4.setOnClickListener {
                next()
            }
        }
    }
}
