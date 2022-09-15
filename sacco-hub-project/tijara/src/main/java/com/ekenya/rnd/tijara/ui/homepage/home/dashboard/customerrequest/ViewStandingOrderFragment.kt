package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentViewStandingOrderBinding
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails.ViewBankInfoFragmentArgs
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ViewStandingOrderFragment : Fragment() {
private lateinit var binding:FragmentViewStandingOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentViewStandingOrderBinding.inflate(layoutInflater)
        val stDetails=ViewStandingOrderFragmentArgs.fromBundle(requireArguments()).viewstandingOrder
        binding.apply {
            etstAcc.setText(stDetails.toAccount)
            etAmount.setText(stDetails.amount)
            etPayFrom.setText(stDetails.fromAccount)
            etStartDate.setText(stDetails.startdate)
            etEndDate.setText(stDetails.enddate)
            etFreq.setText(stDetails.frequencyMeasure)
            etReason.setText(stDetails.purpose)
        }
        setupNavUp()
        return binding.root
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = "Standing Order Details"
    }


}