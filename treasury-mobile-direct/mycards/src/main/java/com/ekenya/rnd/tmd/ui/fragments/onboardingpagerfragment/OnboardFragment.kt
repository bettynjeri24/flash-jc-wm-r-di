package com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.ekenya.rnd.baseapp.di.injectables.ViewModelFactory
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.sms.SmsService
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentOnboardBinding
import com.ekenya.rnd.tmd.ui.fragment_pager.FragmentAdapter
import com.ekenya.rnd.tmd.ui.fragments.identification.IdentificationFragment
import com.ekenya.rnd.tmd.ui.fragments.personal.PersonalFragment
import com.ekenya.rnd.tmd.ui.fragments.selfie.SelfieFragment
import com.ekenya.rnd.tmd.ui.fragments.selfie.TakeSelfieFragment
import com.ekenya.rnd.tmd.ui.fragments.verificationcode.VerificationCodeFragment
import com.ekenya.rnd.tmd.utils.savePhoneNumber
import javax.inject.Inject

class OnboardFragment : BaseDaggerFragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var smsService: SmsService

    private val viewmodel by lazy {
        ViewModelProvider(this, viewModelFactory)[OnboardViewModel::class.java]
    }
    lateinit var binding: FragmentOnboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentOnboardBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        viewmodel.newUserRequest.observe(viewLifecycleOwner) {
            sharedPreferences.savePhoneNumber(it.phoneNumber.toString())
        }
    }

    private fun setUpUi() {
        binding.apply {
            val fragmentAdapter = FragmentAdapter(childFragmentManager)
            fragmentAdapter.addFragment(PersonalFragment(viewmodel) { nextPage() }, "")
            fragmentAdapter.addFragment(IdentificationFragment(viewmodel) { nextPage() }, "")
            fragmentAdapter.addFragment(SelfieFragment(viewmodel) { nextPage() }, "")
            fragmentAdapter.addFragment(TakeSelfieFragment(viewmodel) { nextPage() }, "")
            fragmentAdapter.addFragment(VerificationCodeFragment(viewmodel, smsService) { login() }, "")

            viewPagerOnboarding.apply {
                adapter = fragmentAdapter
                addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        binding.segmentedProgressBar.apply {
                            setPosition(position)
                            next()
                        }
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                    }
                })
            }
        }
    }

    fun login() {
        findNavController().navigate(R.id.action_onboardFragment_to_newPinFragment)
    }

    private fun nextPage() {
        ++binding.viewPagerOnboarding.currentItem
    }

    private fun previousPage() {
        --binding.viewPagerOnboarding.currentItem
    }

    // register on back presses on attach
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.viewPagerOnboarding.currentItem == 0) {
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigateUp()
                } else {
                    previousPage()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}
