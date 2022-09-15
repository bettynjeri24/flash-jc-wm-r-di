package com.ekenya.rnd.authcargill.ui.language

import android.content.res.Configuration
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.databinding.FragmentLanguageBinding
import com.ekenya.rnd.common.utils.base.BaseCommonAuthCargillDIFragment
import com.ekenya.rnd.common.utils.custom.PreferenceProviderCommon
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import timber.log.Timber
import java.util.*

class LanguageFragment : BaseCommonAuthCargillDIFragment<FragmentLanguageBinding>
(FragmentLanguageBinding::inflate) {
    private var show = false
    private var language: String = "en"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        show = true
        animate(binding.root, binding)

        navigate()

        binding.btnContinueLanguage.setOnClickListener {
            // findNavController().navigate(R.id.action_languageFragment_to_lookUpPhoneNumberCoopIdFragment)
            navigate()
        }
        // setDefaultFr()
        var index = 0
        for (l in resources.getStringArray(com.ekenya.rnd.common.R.array.language_array_values)) {
            val locale = Locale(l)
            // locale = Locale("fr")
            if (locale.language.equals(Locale("en").language)) {
                binding.rbEnglish.isSelected = true
                break
            } else {
                binding.rbFrench.isSelected = true
            }
            index++
        }

        binding.rbEnglish.setOnClickListener {
            language = "en"
            binding.tvLanguageTitle.text =
                resources.getString(com.ekenya.rnd.common.R.string.select_your_language_preference_en)
            changeLanguage()
        }
        binding.rbFrench.setOnClickListener {
            language = "fr"
            binding.tvLanguageTitle.text =
                resources.getString(com.ekenya.rnd.common.R.string.select_your_language_preference_fr)
            changeLanguage()
        }
    }

    private fun setDefaultFr() {
        binding.rbFrench.isChecked = true
        language = "fr"
        binding.tvLanguageTitle.text =
            resources.getString(com.ekenya.rnd.common.R.string.select_your_language_preference_fr)
        changeLanguage()
    }

    private fun changeLanguage() {
        PreferenceProviderCommon(requireContext()).setLanguage(language)
        Locale.setDefault(Locale(language))
        val resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(Locale(language))
        resources.updateConfiguration(config, resources.displayMetrics)
        requireActivity().onConfigurationChanged(config)
    }

    private fun animate(parent: ViewGroup, binding: FragmentLanguageBinding) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 1500
        transition.addTarget(binding.clLanguageChooser)

        TransitionManager.beginDelayedTransition(parent, transition)
        binding.clLanguageChooser.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigate() {
        // findNavController().navigate(R.id.action_languageFragment_to_lookUpPhoneNumberCoopIdFragment)
        // val isFirstLogin = UtilPreferenceCommon().getTirstTimeLogin(requireActivity())
        val hasPhoneNumber = UtilPreferenceCommon().getLoggedPhoneNumber(requireActivity())
        Timber.e("==========================$hasPhoneNumber===============================")
        if (hasPhoneNumber.isEmpty()) {
            findNavController().navigate(R.id.action_languageFragment_to_lookUpPhoneNumberCoopIdFragment)
        } else {
            findNavController().navigate(R.id.action_languageFragment_to_loginPinFragment)
        }
    }
}
