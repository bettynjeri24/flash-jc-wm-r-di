package com.ekenya.rnd.tijara.ui.auth.onboarding

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentLandingPageBinding
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.TijaraRoomDatabase
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories.SaccoDetailsRepository
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.ekenya.rnd.tijara.utils.makeStatusBarTransparent
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@Suppress("DEPRECATION")
class LandingPageFragment : Fragment() {
    private lateinit var binding: FragmentLandingPageBinding
    private lateinit var saccoRepository: SaccoDetailsRepository

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        makeStatusBarTransparent()
        binding = FragmentLandingPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Constants.isFromUpdate == 0) {
            binding.btnContinue.text = getString(R.string.login)
        } else {
            binding.btnContinue.text = getString(R.string.get_started)
        }
        binding.apply {
            ivOpenAcc.setOnClickListener {
                val directions = LandingPageFragmentDirections.actionLandingPageFragmentToCountryFragment(fragmentType = 2)
                findNavController().navigate(directions)
            }
            ivHelp.setOnClickListener {
                toastyInfos("COMING SOON")
            }
            ivAbtUs.setOnClickListener {
                toastyInfos("COMING SOON")
            }
        }
        handleBackButton()
        setClickListeners()
        val list = requireContext().resources!!.getStringArray(R.array.language_array_titles)
        val adapter: ArrayAdapter<String> = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_dropdown_item_1line,
                list
            )
        }!!
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

        binding.spChooseLanguage.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val current = resources.configuration.locale
                try {
                    (parent?.getChildAt(0) as? TextView)?.setTextColor(Color.BLACK)
                    // (parent.getChildAt(0) as TextView).textSize = 5f
                    val lng = context?.resources?.getStringArray(R.array.language_array_values)?.get(
                        binding.spChooseLanguage.selectedItemPosition
                    )
                    val locale = Locale(lng)
                    if (locale.language != current.language || locale.country != current.country) {
                        // mApp.setLocale(lng)

                        Locale.setDefault(Locale(lng))
                        val resources = activity!!.resources
                        val config: Configuration = resources.configuration
                        config.setLocale(Locale(lng))
                        resources.updateConfiguration(config, resources.displayMetrics)
                        activity!!.onConfigurationChanged(config)
                        binding.textView3.setText(R.string.welcome_to_tijara)
                        if (PrefUtils.getPreferences(requireContext(), "isFirstLogin") == ("false")) {
                            binding.btnContinue.text = getString(R.string.login)
                        } else {
                            binding.btnContinue.text = getString(R.string.get_started)
                        }
                        binding.tvNewAcc.text = getString(R.string.open_new_account)
                        binding.tvHelp.text = getString(R.string.help)
                        binding.tvSetting.text = getString(R.string.settings)
                        binding.tvAbtUs.text = getString(R.string.about_us)
                    }
                } catch (e: Exception) {
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.btnContinue.setOnClickListener {
            val lng = context?.resources?.getStringArray(R.array.language_array_values)
                ?.get(binding.spChooseLanguage.selectedItemPosition)
            PrefUtils.setPreference(requireContext(), "language", lng)
            if (PrefUtils.getPreferences(requireContext(), "isFirstLogin") == ("false")) {
                GlobalScope.launch(Dispatchers.IO) {
                    val saccoDetails = saccoRepository.getSaccoDetails()
                    Log.d("TAG", "$saccoDetails")
                    if (saccoDetails.size == 1) {
                        Constants.ORGID = saccoDetails.first().orgId.toString().trim()
                        Constants.USERNAME = saccoDetails.first().username.trim()
                        Constants.userFname = saccoDetails.first().firstName.trim()
                        Constants.SaccoName = saccoDetails.first().name.trim()
                        Constants.isSacco = saccoDetails.first().isSacco
                        GlobalScope.launch(Dispatchers.Main) {
                            Constants.SELECTED_TYPE = 1
                            findNavController().navigate(R.id.action_landingPageFragment_to_loginPinFragment)
                        }
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            findNavController().navigate(R.id.action_landingPageFragment_to_loginSaccoFragment)
                        }
                    }
                }
            } else {
                val directions = LandingPageFragmentDirections.actionLandingPageFragmentToCountryFragment(fragmentType = 1)
                findNavController().navigate(directions)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        makeStatusBarTransparent()
    }

    override fun onPause() {
        super.onPause()
        makeStatusBarTransparent()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val saccoDao = TijaraRoomDatabase.getDatabase(requireContext()).getAllSaccoDao()
        saccoRepository = SaccoDetailsRepository(saccoDao)
    }
    private fun handleBackButton() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    exitDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    fun exitDialog() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Confirm Exit!")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("YES") { _, which ->
                requireActivity().finish()
            }
            .setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
        builder.show()
    }
    private fun setClickListeners() {
        binding.ivAbtUs.setOnClickListener {
            AboutUsDialogFragment.instance().show(parentFragmentManager, "aboutUs")
        }
        binding.ivHelp.setOnClickListener {
            HelpDialogFragment.instance().show(parentFragmentManager, "helps")
        }
    }
}
