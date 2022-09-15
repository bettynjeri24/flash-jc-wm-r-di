package com.ekenya.rnd.tmd.ui.fragments.landing

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentLandingBinding
import com.ekenya.rnd.tmd.utils.extendStatusBarBackground
import com.ekenya.rnd.tmd.utils.getHasFinishedLanding
import com.ekenya.rnd.tmd.utils.setHasFinishedLanding
import com.ekenya.rnd.tmd.utils.unExtendStatusBarBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class LandingFragment : BaseDaggerFragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentLandingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLandingBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extendStatusBarBackground()
        setUpUi()
        if (sharedPreferences.getHasFinishedLanding()) {
            findNavController().navigate(R.id.action_landingFragment_to_getStartedFragment)
        }
    }

    private fun setUpUi() {
        setUpBindings()
        setUpViewPager()
    }

    private fun setUpBindings() {
        binding.apply {
            binding.button2.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_getStartedFragment)
                sharedPreferences.setHasFinishedLanding()
            }
        }
    }

    private fun setUpViewPager() {
        binding.apply {
            val adapter = ViewPagerLanding()
            viewPagerLanding.adapter = adapter
            adapter.submitList(landingList)

            viewPagerLanding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    textViewTitle.text = landingList[position].title
                    textViewSubTitle.text = landingList[position].subTitle
                }
            })
            lifecycleScope.launch {
                while (true) {
                    delay(5000)
                    if (viewPagerLanding.currentItem == 2) viewPagerLanding.currentItem = 0 else ++viewPagerLanding.currentItem
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        unExtendStatusBarBackground()
    }
}
