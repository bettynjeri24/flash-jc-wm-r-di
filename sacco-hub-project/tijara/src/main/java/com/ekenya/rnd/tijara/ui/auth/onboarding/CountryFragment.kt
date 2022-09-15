package com.ekenya.rnd.tijara.ui.auth.onboarding

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.EmptySaccoDialogBinding
import com.ekenya.rnd.tijara.databinding.FragmentCountryBinding
import com.ekenya.rnd.tijara.requestDTO.CountryDTO
import com.ekenya.rnd.tijara.requestDTO.NewAccDTO
import com.ekenya.rnd.tijara.utils.*
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CountryFragment : BaseDaggerFragment(), CountryCodePicker.OnCountryChangeListener {
    lateinit var binding: FragmentCountryBinding
    private lateinit var cardBinding: EmptySaccoDialogBinding
    private var selectedCode = ""
    private var ccp: CountryCodePicker? = null
    private var selectedLanguage = ""
    private var phoneNumber = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(CountryViewmodel::class.java)
    }
    val args: CountryFragmentArgs by navArgs()
    private var adapter: ArrayAdapter<*>? = null

    private val occupationList =
        listOf(
            "Teacher",
            "Radiographer",
            "Engineer"
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryBinding.inflate(layoutInflater)

        /*adapter = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, occupationList)
        binding.autocompleteOccupation.setAdapter<ArrayAdapter<*>>(adapter)*/
        // binding.tvTerms.paintFlags.under
        binding.apply {
/*
            tvTerms.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("")))
            }
*/
            tvTerms.paint.isUnderlineText = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.e("=============\n CountryFragment \n=============")
       // binding.getStarted.setOnClickListener {
            Log.e("", "=============\n CountryFragment \n=============")
       // }
         inflateUi()
    }

    private fun inflateUi() {
        ccp = binding.ccp
        ccp!!.setOnCountryChangeListener(this)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        viewModel.statusCode.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.getStarted.isEnabled = true
                binding.progressbar.visibility = View.GONE
                requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                when (it) {
                    1 -> {
                        viewModel.stopObserving()
                        binding.getStarted.isEnabled = true
                        binding.progressbar.visibility = View.GONE
                        findNavController().navigate(R.id.action_countryFragment_to_newSaccoFragment)
                    }
                    0 -> {
                        viewModel.stopObserving()
                        binding.getStarted.isEnabled = true
                        binding.progressbar.visibility = View.GONE
                        onInfoDialog(requireContext(), viewModel.statusMessage.value)
                    }
                    else -> {
                        viewModel.stopObserving()
                        binding.getStarted.isEnabled = true
                        binding.progressbar.visibility = View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                    }
                }
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.getStarted.isEnabled = true
                binding.progressbar.visibility = View.GONE
                requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                when (it) {
                    1 -> {
                        when (args.fragmentType) {
                            1 -> {
                                viewModel.stopObserving()
                                if (viewModel.mySaccoDetails.isEmpty()) {
                                    showEmptySacco()
                                } else {
                                    findNavController().navigate(R.id.action_countryFragment_to_deviceVerificationFragment)
                                }
                                binding.getStarted.isEnabled = true
                                binding.progressbar.visibility = View.GONE
                            }
                            2 -> {
                                /* viewModel.stopObserving()
                                 binding.getStarted.isEnabled=true
                                 binding.progressbar.visibility=View.GONE*/
                                //  toastySuccess("success")
                                //   findNavController().navigate(R.id.action_countryFragment_to_newSaccoFragment)
                            }
                        }
                    }
                    0 -> {
                        viewModel.stopObserving()
                        binding.getStarted.isEnabled = true
                        binding.progressbar.visibility = View.GONE
                        onInfoDialog(requireContext(), viewModel.statusMessage.value)
                    }
                    else -> {
                        viewModel.stopObserving()
                        binding.getStarted.isEnabled = true
                        binding.progressbar.visibility = View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                    }
                }
            }
        }
        binding.apply {
            getStarted.setOnClickListener {
                val validMsg = FieldValidators.VALIDINPUT
                phoneNumber = FieldValidators().formatPhoneNumber(binding.etPhone.text.toString())
                val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                if (!validPhone.contentEquals(validMsg)) {
                    etPhone.requestFocus()
                    tlPhone.isErrorEnabled = true
                    tlPhone.error = validPhone
                } else if (!cbAccount.isChecked) {
                    toastyInfos(getString(R.string.please_accept_terms_and_coditions))
                } else {
                    tlPhone.error = null
                    tlPhone.isErrorEnabled = false
                    getStarted.isEnabled = false
                    when (args.fragmentType) {
                        1 -> {
                            val countryDTO = CountryDTO()
                            countryDTO.phone = phoneNumber
                            requireActivity().window.statusBarColor =
                                resources.getColor(R.color.spinkit_color)
                            binding.progressbar.visibility = View.VISIBLE
                            binding.progressbar.tv_pbTitle.text =
                                getString(R.string.validating_your_phone)
                            binding.progressbar.tv_pbTex.text = getString(R.string.please_wait)
                            viewModel.getSaccos(countryDTO)
                            PrefUtils.setPreference(requireContext(), "mobile", countryDTO.phone)
                        }
                        2 -> {
                            val newAccDTO = NewAccDTO()
                            newAccDTO.phone = phoneNumber
                            newAccDTO.notYetJoined = 1
                            requireActivity().window.statusBarColor =
                                resources.getColor(R.color.spinkit_color)
                            binding.progressbar.visibility = View.VISIBLE
                            binding.progressbar.tv_pbTitle.text =
                                getString(R.string.validating_your_phone)
                            binding.progressbar.tv_pbTex.text = getString(R.string.please_wait)
                            if (isNetwork(requireContext())) {
                                viewModel.getNewSaccos(newAccDTO)
                                PrefUtils.setPreference(requireContext(), "mobile", newAccDTO.phone)
                            } else {
                                onNoNetworkDialog(requireContext())
                            }
                        }
                    }
                }
            }
        }

        /* binding.ivLogo.setOnClickListener {
             findNavController().navigate(R.id.landingPageFragment)
         }*/
        /**Language Spinner */
        //  binding.spinnerLanguage.setSelection(1, true)

        /* val languages: Array<String> = resources.getStringArray(R.array.language)
         val languagesAdapter =
             ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, languages)
         binding.spinnerLanguage.adapter = languagesAdapter
         binding.spinnerLanguage.onItemSelectedListener =
             object : AdapterView.OnItemSelectedListener {
                 override fun onItemSelected(
                     arg0: AdapterView<*>?,
                     arg1: View?,
                     position: Int,
                     arg3: Long
                 ) {
                     Constants.SELECTED_LANGUAGE_POSITION = arg0!!.selectedItemPosition
                     if (Constants.SELECTED_LANGUAGE_POSITION>0) {
                         Constants.SELECTED_LANGUAGE = arg0!!.selectedItem.toString()
                         PrefUtils.setPreference(
                             requireContext(),
                             "selectedLanguage",
                             arg0!!.selectedItem.toString()
                         )

                         when(Constants.SELECTED_LANGUAGE){
                             "English" ->{
                                 updateSelectedLang("en")
                             }
                             "French"->{
                                 updateSelectedLang("fr")
                             }
                         }
                         binding.tvWelcome.setText(R.string.welcome_to)
                         binding.tvSelectCountry.setText(R.string.select_your_country)
                         binding.selctLanguage.setText(R.string.select_preferred_language)
                         binding.getStarted.setText(R.string.get_started)
                         Timber.d("LANGUAGE  ${Constants.SELECTED_LANGUAGE}")
                     }
                 }
                 override fun onNothingSelected(arg0: AdapterView<*>?) {
                     Constants.SELECTED_LANGUAGE = ""
                 }
             }*/

        /**Country Spinner */

        /*val flag=PrefUtils.getPreferences(this,"countryFlag")!!.toInt()
        binding.locPin.setImageResource(flag)
        val country:Array<String> = resources.getStringArray(R.array.country)
        val countryAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, country)
        binding.spinnerCountry.adapter=countryAdapter
        binding.spinnerCountry.onItemSelectedListener =
            object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(selectedCountry){
                        "Kenya"-> {
                            PrefUtils.setPreference(this@CountryActivity,"countryCode","254")
                            PrefUtils.setPreference(this@CountryActivity,"countryCode","Kenya")
                            PrefUtils.setPreference(this@CountryActivity,"countryFlag",R.drawable.flag_kenya.toString())
                        }
                        "Uganda"-> {
                            PrefUtils.setPreference(this@CountryActivity,"countryCode","256")
                            PrefUtils.setPreference(this@CountryActivity,"countryCode","Uganda")
                            PrefUtils.setPreference(this@CountryActivity,"countryFlag",R.drawable.flag_uganda.toString())
                        }

                    }
                    Timber.d("SELECTED COUNTRY IS  $selectedCountry")

                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }*/
    }

    override fun onCountrySelected() {
        PrefUtils.setPreference(
            requireContext(),
            "countryCode",
            ccp!!.selectedCountryCodeWithPlus.substring(
                1
            )
        )
        PrefUtils.setPreference(requireContext(), "countryName", ccp!!.selectedCountryName)
        selectedCode = ccp!!.selectedCountryCodeWithPlus.substring(1)
        Timber.d("SELECTED COUNTRY ${ccp!!.selectedCountryName}")
        Timber.d("SELECTED COUNTRY CODE ${ccp!!.selectedCountryCodeWithPlus.substring(1)}")
    }

    private fun showEmptySacco() {
        val dialog = Dialog(requireContext())
        cardBinding =
            EmptySaccoDialogBinding.inflate(LayoutInflater.from(context))
        cardBinding.ivCancel.setOnClickListener {
            dialog.dismiss()
            viewModel.stopObserving()
        }
        cardBinding.btnNotNow.setOnClickListener {
            dialog.dismiss()
            viewModel.stopObserving()
        }
        cardBinding.btnREGISTER.setOnClickListener {
            dialog.dismiss()
            // dialog.hide()
            findNavController().navigate(R.id.action_countryFragment_to_saccoListFragment)
            viewModel.stopObserving()
        }

        dialog.setContentView(cardBinding.root)
        dialog.show()
        dialog.setCancelable(false)
    }

    override fun onResume() {
        super.onResume()

        if (selectedLanguage.equals("en", ignoreCase = true)) {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val config: Configuration = requireActivity().baseContext.resources.configuration
            config.locale = locale
            requireActivity().baseContext.resources
                .updateConfiguration(config, requireActivity().baseContext.resources.displayMetrics)
        } else if (selectedLanguage.equals("fr", ignoreCase = true)) {
            val locale = Locale("fr")
            Locale.setDefault(locale)
            val config: Configuration = requireActivity().baseContext.resources.configuration
            config.locale = locale
            requireActivity().baseContext.resources
                .updateConfiguration(config, requireActivity().baseContext.resources.displayMetrics)
        }
    }
}
