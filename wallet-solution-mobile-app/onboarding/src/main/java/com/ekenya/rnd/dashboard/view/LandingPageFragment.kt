package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.AppUtils.getTimeofTheDay
import com.ekenya.rnd.dashboard.utils.hideSupportActionBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarTransparent
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.LandingPageFragmentBinding

class LandingPageFragment : Fragment() {
    private lateinit var binding: LandingPageFragmentBinding
    private val languages = listOf("English ", "Amharic")

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        if (SharedPreferencesManager.hasReachedHomepage(requireContext()) == false
//        ) {
        //  findNavController().navigate(R.id.action_landingPageFragment_to_loginFragment2)

//        }

        binding = LandingPageFragmentBinding.inflate(inflater, container, false)

        initUI()

        return binding.root
    }

    private fun initUI() {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, languages)
        (binding.appLanguage as? AutoCompleteTextView)?.setAdapter(adapter)

        val name = SharedPreferencesManager.getFirstName(requireContext())
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.accountLookUpFragment)
            // findNavController().navigate(R.id.action_landingPageFragment_to_loginFragment2)
        }
        binding.tvHelp.setOnClickListener {
            findNavController().navigate(R.id.action_landingPageFragment_to_helpfragment)
        }
        binding.tvAboutUs.setOnClickListener {
            findNavController().navigate(R.id.action_landingPageFragment_to_aboutusfragment)
        }
        binding.tvLocation.setOnClickListener {
            toastMessage("Feature Coming Soon")
        }
        binding.tvGreetings.text = "${getTimeofTheDay()} $name!  \n" +
            "Enjoy cashless  \nTransactions "
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeStatusBarTransparent()
        hideSupportActionBar()
    }

    override fun onResume() {
        super.onResume()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, languages)
        (binding.appLanguage as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}
