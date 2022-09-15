package com.ekenya.rnd.tijara.ui.homepage.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.tijara.databinding.FragmentTransferFundBinding
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class TransferFundFragment : Fragment() {
   private lateinit var binding:FragmentTransferFundBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTransferFundBinding.inflate(layoutInflater)
        setupNavUp()
        return binding.root
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = "SERVICES"
    }


}