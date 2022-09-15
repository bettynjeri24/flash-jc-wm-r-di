package com.ekenya.lamparam.ui.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.databinding.FragmentSplashScreenBinding
import com.ekenya.lamparam.network.DefaultResponse
import com.ekenya.lamparam.network.Status
import com.ekenya.lamparam.user.UserDataRepository
import com.ekenya.lamparam.utilities.PreferenceProvider
import com.ekenya.lamparam.viewmodel.SplashViewModel
import javax.inject.Inject

/**
 * Splash screen fragment
 */
class SplashScreen : Fragment() {

    private lateinit var viewModel: SplashViewModel

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var onboardingViewModel: OnBoardingViewModel

    @Inject
    lateinit var userDataRepository: UserDataRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDataRepository.deleteToken() //delete token
        sendRequest()

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        viewModel.eventTimeExpired.observe(viewLifecycleOwner, {
            if (it)
                navigate()
        })
    }

    private fun sendRequest() {
        onboardingViewModel.getAppToken().observe(viewLifecycleOwner, { (status, resData) ->
            when (status) {
                Status.SUCCESS -> {
                    if (resData != null) {
                        val result: DefaultResponse = resData.body()!!
                        userDataRepository.storeToken(result.token)
                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        })
    }

    private fun navigate(){
        findNavController().navigate(R.id.action_nav_splashScreen_to_nav_choose_language)
        viewModel.hasNavigated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}