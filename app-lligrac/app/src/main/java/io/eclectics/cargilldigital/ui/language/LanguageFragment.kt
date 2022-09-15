package io.eclectics.cargilldigital.ui.language


import android.content.res.Configuration
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.LanguageFragmentBinding
import io.eclectics.cargilldigital.utils.PreferenceProvider
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.GlobalMethods
import java.util.*

@AndroidEntryPoint
class LanguageFragment : Fragment() {

    private val viewModel: LanguageViewModel by viewModels()
    lateinit var navOptions: NavOptions
    private var _binding: LanguageFragmentBinding? = null
    private val binding get() = _binding!!
    private var show = false
    private var language: String = "en"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LanguageFragmentBinding.inflate(inflater, container, false)
        navOptions = GlobalMethods.navigation.options()
        show = true
        animate(container!!, binding)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
      //UtillPreference().setLoggedphoneNumber(requireActivity(),"2250594851583")//2250703035850  2250701686379 2250594851580-(2250594851583) 2250150466275 254798997948
       UtilPreference().setTirstTimeLogin(requireActivity(),false)
        UtilPreference().setIsPrintableData(requireActivity(),false)
        binding.btnContinueLanguage.setOnClickListener { viewModel.navigate() }


        setDefaultFr()
        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it)
                navigate()
        })

        var index = 0
        for (l in resources.getStringArray(R.array.language_array_values)) {
            val locale = Locale(l)
            //locale = Locale("fr")
            if (locale.language.equals(Locale("en").language)) {
                binding.rbEnglish.isSelected = true
                break
            }else{
                binding.rbFrench.isSelected = true
            }
            index++
        }


        binding.rbEnglish.setOnClickListener {
            language = "en"
            binding.tvLanguageTitle.text = resources.getString(R.string.select_your_language_preference_en)
            changeLanguage() }
        binding.rbFrench.setOnClickListener {
            language = "fr"
            binding.tvLanguageTitle.text = resources.getString(R.string.select_your_language_preference_fr)
            changeLanguage() }

    }

    private fun setDefaultFr() {
        binding.rbFrench.isChecked = true
        language = "fr"
        binding.tvLanguageTitle.text = resources.getString(R.string.select_your_language_preference_fr)
        changeLanguage()
    }

    private fun changeLanguage() {
        PreferenceProvider(requireContext()).setLanguage(language)
        Locale.setDefault(Locale(language))
        val resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(Locale(language))
        resources.updateConfiguration(config, resources.displayMetrics)
        requireActivity().onConfigurationChanged(config)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun animate(parent: ViewGroup, binding: LanguageFragmentBinding) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 1500
        transition.addTarget(binding.clLanguageChooser)

        TransitionManager.beginDelayedTransition(parent, transition)
        binding.clLanguageChooser.visibility = if (show) View.VISIBLE else View.GONE
    }


    private fun navigate() {

        //TODO MAIN NAVIGATION
        var isFirstLogin = UtilPreference().getTirstTimeLogin(requireActivity())
        if(isFirstLogin) {
            view?.findNavController()?.navigate(R.id.nav_phoneLookupFragment, null, navOptions)
        }else{
            view?.findNavController()?.navigate(R.id.nav_loginAccountFragment, null, navOptions)
        }
        viewModel.hasNavigated()

    }

}