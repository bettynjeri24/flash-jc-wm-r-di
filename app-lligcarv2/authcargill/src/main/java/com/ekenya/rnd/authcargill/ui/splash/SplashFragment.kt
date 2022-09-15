package com.ekenya.rnd.authcargill.ui.splash

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.databinding.SplashFragmentBinding
import com.ekenya.rnd.common.utils.base.BaseCommonAuthCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.getUTCtime
import com.ekenya.rnd.common.utils.services.HuaweiHashManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashFragment : BaseCommonAuthCargillDIFragment<SplashFragmentBinding>
(SplashFragmentBinding::inflate) {

    // val viewModel: SplashViewModel by viewModels()

    // Animation
    private lateinit var animRotate: Animation

    /** Duration of wait  */
    private val SPLASH_DISPLAY_LENGTH = 1000L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateUi()
    }

    private fun inflateUi() {
//        UtilPreferenceCommon()
//            .setLoggedPhoneNumber(
//                requireActivity(),
//                "2250703035850" // response.accountIdData!!.phonenumber.toString()
//            )

        animRotate = AnimationUtils.loadAnimation(
            requireActivity(),
            com.ekenya.rnd.common.R.anim.rotate_common
        )
        animRotate.fillAfter = true
        binding.ivLoader.startAnimation(animRotate)

        UtilPreferenceCommon().setTirstTimeLogin(requireActivity(), false)
        lifecycleScope.launch {
            delay(SPLASH_DISPLAY_LENGTH)
            // findNavController().navigate(R.id.action_splashFragment_to_languageFragment)
            findNavController().navigate(R.id.lookUpPhoneNumberCoopIdFragment)
        }
        testDate()
    }

    private fun testDate() {
        val hashValue = HuaweiHashManager().getHashValue(requireContext())
        Timber.e("==================HASH VALUE============= $hashValue")

        val dates = getUTCtime()
        Timber.e("DATES", "DATES-$dates")
    }
}
