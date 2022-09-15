package com.ekenya.rnd.tmd.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentWelcomeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        setUpBindings()
    }

    private fun setUpBindings() {
        binding.buttonStartInvesting.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_onboardFragment)
        }
    }

    private fun setUpUi() {
        binding.recyclerView.apply {
            val reqAdapter = RequirementsAdapter()
            adapter = reqAdapter
            layoutManager = LinearLayoutManager(requireContext())
            reqAdapter.submitList(requirements)
        }
    }
}
