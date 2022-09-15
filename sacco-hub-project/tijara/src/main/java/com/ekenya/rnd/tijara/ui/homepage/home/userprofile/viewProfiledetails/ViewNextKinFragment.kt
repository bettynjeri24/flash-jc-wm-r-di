package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.ViewNextKinFragmentBinding

import kotlinx.android.synthetic.main.custom_toolbar.view.*
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class ViewNextKinFragment : Fragment() {
private lateinit var binding: ViewNextKinFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ViewNextKinFragmentBinding.inflate(layoutInflater)
        val kinsItemProperty= ViewNextKinFragmentArgs.fromBundle(requireArguments()).kinsListProperties
        binding.lifecycleOwner=this
        binding.apply {
            etMemberNo.setText(kinsItemProperty.fullName)
            etEmail.setText(kinsItemProperty.id.toString().trim())
            etPhone.setText(kinsItemProperty.gender)
            etKraPin.setText(kinsItemProperty.dob)
            etAllocation.setText(kinsItemProperty.allocation)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.next_of_kin_details)
    }

}