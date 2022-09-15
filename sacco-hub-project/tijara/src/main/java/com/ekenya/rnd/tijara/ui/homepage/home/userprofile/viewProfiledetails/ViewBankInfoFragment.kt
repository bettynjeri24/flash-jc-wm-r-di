package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.ViewBankInfoFragmentBinding
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.nav_custom_toolbar.view.*
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class ViewBankInfoFragment : Fragment() {
private lateinit var binding: ViewBankInfoFragmentBinding
private lateinit var viewModel: ViewBankInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.statusBarColor = resources.getColor(R.color.buttonColor)
        binding= ViewBankInfoFragmentBinding.inflate(layoutInflater)
        val bankItemProperty= ViewBankInfoFragmentArgs.fromBundle(requireArguments()).bankInfoListProperties
        binding.apply {

            etMemberNo.setText(bankItemProperty.bank)
            etKraPin.setText(bankItemProperty.accountNo)
            etPhone.setText(bankItemProperty.accountName)
            etEmail.setText(bankItemProperty.branch)
        }
        setupNavUp()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.bank_details)
    }


}
