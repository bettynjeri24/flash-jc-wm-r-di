package com.ekenya.rnd.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.onboarding.databinding.FragmentIntermediateBinding

class IntermediateFragment : Fragment() {
    private lateinit var binding : FragmentIntermediateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntermediateBinding.inflate(inflater,container,false)
       // val isFirstTimeUser = SharedPreferencesManager.isFirstTimeUser(requireContext())
       /* if (isFirstTimeUser == true) {
            parentFragment?.findNavController()?.navigate(R.id.action_intermediateFragment_to_loginFragment)

        } else {*/
           // parentFragment?.findNavController()?.navigate(R.id.action_intermediateFragment_to_segFragment)
        //}


        return binding!!.root
    }









}