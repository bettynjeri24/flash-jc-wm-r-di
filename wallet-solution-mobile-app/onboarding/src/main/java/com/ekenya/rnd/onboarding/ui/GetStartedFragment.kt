package com.ekenya.rnd.onboarding.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.ekenya.rnd.onboarding.databinding.AccountLookupFragmentBinding
import com.ekenya.rnd.onboarding.databinding.GetStartedFragmentBinding
import java.util.*

class GetStartedFragment : Fragment() {
    private lateinit var binding: GetStartedFragmentBinding
    private var isKiswahiliSelected = false
    private lateinit var selectedValue:String
    val languages = listOf("English ", "Amharic")



    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = GetStartedFragmentBinding.inflate(inflater, container, false)
        initUI()
        initButtonListeners()

        return binding.root
    }

    private fun initUI() {

         selectedValue = (binding.tilFromDestination.editText as AutoCompleteTextView).text.toString()
       // toastMessage("Selected Value1 $selectedValue")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, languages)
        (binding.appLanguage as? AutoCompleteTextView)?.setAdapter(adapter)

        if(SharedPreferencesManager.isAmharicSelected(requireContext())!!)
        {
            binding.appLanguage.setText("Amharic");
        }
        else
        {
            binding.appLanguage.setText("English");
        }

        (binding.tilFromDestination.editText as AutoCompleteTextView).setOnItemClickListener { adapterView, view, position, id ->
            selectedValue = adapter.getItem(position).toString()
            toastMessage("Selected Value2 $selectedValue")
            if (selectedValue == "Amharic") {
                isKiswahiliSelected = true
                SharedPreferencesManager.setIsAmharicSelected(requireContext(), isKiswahiliSelected)
            } else {
                isKiswahiliSelected = false
                SharedPreferencesManager.setIsAmharicSelected(requireContext(), isKiswahiliSelected)
            }
            onResume()

        }

    }
    private fun checkLanguageSelection(){

            if (!isKiswahiliSelected) {
                isKiswahiliSelected = true
                SharedPreferencesManager.setIsAmharicSelected(requireContext(), isKiswahiliSelected)
            } else {
                isKiswahiliSelected = false
                SharedPreferencesManager.setIsAmharicSelected(requireContext(), isKiswahiliSelected)
            }
            onResume()
        }


    private fun initButtonListeners() {
        binding.btnGetStarted.setOnClickListener {
            //findNavController().navigate(R.id.action_getStartedFragment_to_accountLookUpFragment)
            findNavController().navigate(R.id.accountLookUpFragment)
         }
        binding.tvHelp.setOnClickListener {
            findNavController().navigate(R.id.helpFragment2)
        }
        binding.tvAboutUs.setOnClickListener {
            findNavController().navigate(R.id.aboutUsFragment2)
        }
        binding.tvLocation.setOnClickListener{
            toastMessage("Feature Coming Soon")
        }
        binding.tvGreetings.text = "${getTimeofTheDay()}!  \nEnjoy cashless  \nTransactions "

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeStatusBarTransparent()
        hideSupportActionBar()
        /*if (SharedPreferencesManager.hasFinishedSliders(requireContext()) == true && SharedPreferencesManager.hasReachedHomepage(
                requireContext()
            ) == true
        ) {
            //go to get started landing page
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(intent)
        } else if (SharedPreferencesManager.hasFinishedSliders(requireContext()) == true) {
            findNavController().navigate(R.id.action_segFragment_to_getStartedFragment)

        }*/
    }

    override fun onResume() {
        super.onResume()
        if (SharedPreferencesManager.isAmharicSelected(requireContext())!!) {
            val myLocale = Locale("sw")
            val res: Resources = resources
            val dm: DisplayMetrics = res.displayMetrics
            val conf: Configuration = res.configuration
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
        } else {
            val myLocale = Locale("en")
            val res: Resources = resources
            val dm: DisplayMetrics = res.displayMetrics
            val conf: Configuration = res.configuration
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
        }
       // btnGetStarted.setText(getString(R.string.get_started))
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, languages)
        (binding.appLanguage as? AutoCompleteTextView)?.setAdapter(adapter)
    }


}



