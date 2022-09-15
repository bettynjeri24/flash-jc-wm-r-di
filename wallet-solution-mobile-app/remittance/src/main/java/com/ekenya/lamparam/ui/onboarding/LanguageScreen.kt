package com.ekenya.lamparam.ui.onboarding

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.R

import android.widget.ArrayAdapter
import android.widget.AdapterView

import android.widget.TextView

import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.app.ActivityCompat
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.databinding.FragmentChooseLanguageBinding
import com.ekenya.lamparam.network.DefaultResponse
import com.ekenya.lamparam.network.Status
import com.ekenya.lamparam.user.UserDataRepository
import com.ekenya.lamparam.utilities.GlobalMethods
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LanguageScreen : Fragment() {

    private var _binding: FragmentChooseLanguageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Inject
    lateinit var onboardingViewModel: OnBoardingViewModel

    @Inject
    lateinit var globalMethods: GlobalMethods

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle? ): View {
        _binding = FragmentChooseLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPerms()

        val list = requireContext().resources!!.getStringArray(R.array.language_array_titles)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list)
        binding.spChooseLanguage.adapter = adapter

        val current = resources.configuration.locale
        var index = 0
        for (l in resources.getStringArray(R.array.language_array_values)) {
            val locale = Locale(l)
            if (locale.language == current.language || locale.country == current.country) {
                binding.spChooseLanguage.setSelection(index)
                break
            }
            index++
        }

        binding.spChooseLanguage.onItemSelectedListener =  object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val current = resources.configuration.locale
                try {
                    (parent?.getChildAt(0) as? TextView)?.setTextColor(Color.WHITE)
                    //(parent.getChildAt(0) as TextView).textSize = 5f
                    val lng = requireContext().resources.getStringArray(R.array.language_array_values)[binding.spChooseLanguage.selectedItemPosition]
                    val locale = Locale(lng)
                    if(locale.language != current.language || locale.country != current.country){
                        //mApp.setLocale(lng)

                        Locale.setDefault(Locale(lng))
                        val resources = activity!!.resources
                        val config: Configuration = resources.configuration
                        config.setLocale(Locale(lng))
                        resources.updateConfiguration(config, resources.displayMetrics)

                        activity!!.onConfigurationChanged(config)
                    }
                }catch (e: Exception){}
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnNext.setOnClickListener {
           navigate()
        }

        if (!userDataRepository.tokenAvailable) {
            sendRequest()
        }
    }

    private fun navigate() {
        val lng = requireActivity().resources.getStringArray(R.array.language_array_values)[binding.spChooseLanguage.selectedItemPosition]
        userDataRepository.setLanguage(lng)

        if (userDataRepository.registered){
            findNavController().navigate(R.id.nav_pin)
            //todo send change language request
        }else{
            findNavController().navigate(R.id.nav_mainOnBoarding)
        }
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

    /**
     * App needs to read IMEI
     */
    private fun checkPerms() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
        } else {
            userDataRepository.storeImei(globalMethods.getDeviceId(requireActivity()))
            //then store in shared preferences
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
                    return
                }
                userDataRepository.storeImei(globalMethods.getDeviceId(requireActivity()))
                //then store in shared preferences
            } else {
                globalMethods.transactionWarning(requireActivity(), getString(R.string.permission_warning))
                requireActivity().finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}