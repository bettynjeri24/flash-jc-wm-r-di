package com.ekenya.rnd.tijara.ui.auth.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.pagersadapter.OnBoardingAdapter
import com.ekenya.rnd.tijara.databinding.FragmentOnBoardingBinding
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.ekenya.rnd.tijara.utils.autoPlayAdvertisement

class OnBoardingFragment : Fragment() {
   private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
        if (PrefUtils.getPreferences(requireContext(), "isExisting") == ("false")) {
            findNavController().navigate(R.id.action_onBoardingFragment_to_landingPageFragment)
        }
        binding=FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSliders()
        binding.apply {
            btnGetStarted.setOnClickListener {
               PrefUtils.setPreference(requireContext(), "isExisting","false")
              //  findNavController().navigate(R.id.action_onBoardingFragment_to_landingPageFragment)
              findNavController().navigate(R.id.action_onBoardingFragment_to_landingPageFragment)
            }

        }


    }
    private fun loadSliders(){
        val onBoardingAdapter =
            OnBoardingAdapter(
                requireContext()
            )
        binding.pager.adapter =onBoardingAdapter
        binding.tabDots.setupWithViewPager(binding.pager, true)
        onBoardingAdapter.notifyDataSetChanged()

        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                when (position) {
                    0 -> {
                        binding.tvSliderDescr.setText(R.string.you_can_now_start)
                    }
                    1 -> {
                        binding.tvSliderDescr.setText(R.string.you_can_now_send)
                    }
                    else -> {
                        binding.tvSliderDescr.setText(R.string.you_can_now_get_instant)

                    }
                }
            }

            override fun onPageSelected(position: Int) {



            }
        })
        autoPlayAdvertisement(binding.pager)

    }


}