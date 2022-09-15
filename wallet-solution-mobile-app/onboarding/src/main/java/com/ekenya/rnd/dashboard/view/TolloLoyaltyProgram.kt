package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.adapters.ImagesAdapter
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentTolloLoyaltyProgramBinding
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager

class TolloLoyaltyProgram : Fragment() {
    private lateinit var binding: FragmentTolloLoyaltyProgramBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTolloLoyaltyProgramBinding.inflate(layoutInflater)
        setUpViewPager()
        setupOnclickListeners()
        return binding.root
    }

    private fun setupOnclickListeners() {
        binding.cdPaywithPoints.setOnClickListener{
            findNavController().navigate(R.id.payUsingPointsFragment)
        }
        binding.cdRedeemPoints.setOnClickListener{
            toastMessage("Feature Coming soon")
        }
        binding.cdTransferPoints.setOnClickListener{
            findNavController().navigate(R.id.transferPointsFragment)
        }
    }

    private fun setUpViewPager() {
        // viewPager = binding.viewPager

        val images = intArrayOf(
            R.drawable.promotion_image,
            R.drawable.promotion_image,
            R.drawable.promotion_image,

            )

        binding.viewPager.adapter = ImagesAdapter(requireContext(), images)
        binding.viewPager.setInterval(2000)
        binding.viewPager.setDirection(AutoScrollViewPager.Direction.RIGHT)
        binding.viewPager.setCycle(true)
        binding.viewPager.setBorderAnimation(true)
        binding.viewPager.setSlideBorderMode(AutoScrollViewPager.SlideBorderMode.TO_PARENT)
        binding.viewPager.startAutoScroll()


    }


}