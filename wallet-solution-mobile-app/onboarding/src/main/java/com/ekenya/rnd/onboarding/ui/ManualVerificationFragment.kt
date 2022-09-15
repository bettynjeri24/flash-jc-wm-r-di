package com.ekenya.rnd.onboarding.ui

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.storage.SharedPreferencesManager.setDateOfBirth
import com.ekenya.rnd.common.storage.SharedPreferencesManager.setDob
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.changeActionbarColor
import com.ekenya.rnd.dashboard.utils.getDateFromDialog
import com.ekenya.rnd.dashboard.viewmodels.TransactionConfirmationViewModel
import com.ekenya.rnd.ethiopiacalender.EnglishEthiopianDatePickerDialogFragment
import com.ekenya.rnd.ethiopiacalender.EthiopicCalendar
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.ManualVerificationFragmentBinding
import net.time4j.android.ApplicationStarter
import java.text.SimpleDateFormat
import java.util.*

class ManualVerificationFragment : BaseDaggerFragment()
{
    private lateinit var binding: ManualVerificationFragmentBinding
    private lateinit var user_id: String
    private lateinit var firstname: String
    private lateinit var secondname: String
    private lateinit var lastname: String
    private lateinit var nationalid: String
    private var documentSelected: String = "Kebele ID"
    private lateinit var dob: String


    private var cal = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ManualVerificationFragmentBinding.inflate(layoutInflater)
        initUi()
        initViewModel()

        prepopulateEditexts()

        initclickListeners()


        return binding.root
    }

    private fun initUi()
    {
        binding.btnOmangCard.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.bg_blue);
        binding.btnUsePassport.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.grey_40percent);
        binding.tilIdNumber.hint = "Kebele ID Number"


        binding.tilIdNumber.editText!!.doAfterTextChanged {
            binding?.tilIdNumber?.error = null

        }


    }

    private fun initViewModel() {
        val model =
            ViewModelProvider(requireActivity()).get(TransactionConfirmationViewModel::class.java)
        model.paymentOption.observe(viewLifecycleOwner, {
            binding?.etFirstname?.setText(it)

        })
    }

    private fun initclickListeners() {
        var date: String?
         if (requireActivity().intent != null && requireActivity().intent.extras != null && requireActivity().intent.extras!!.containsKey("SelectedDate")) {
             date = requireActivity().intent.getStringExtra("SelectedDate")
             setSelectedDate(date)
         }
        try {
            if(!arguments?.getString("SelectedDate").isNullOrBlank()){
                setSelectedDate(arguments?.getString("SelectedDate"))
                toastMessage(arguments?.getString("SelectedDate"))
            }


        } catch (e: Exception) {

        }

        ApplicationStarter.initialize(requireContext(), true)

        binding.etCalender.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val englishDatePicker = EnglishEthiopianDatePickerDialogFragment(binding!!.etCalender)
            englishDatePicker.show(fm, "DatePicker")
        }
        //getDateFromDialog(binding!!.etCalender)
        binding.btnOmangCard.setOnClickListener {
            binding.btnOmangCard.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.bg_blue);
            binding.btnOmangCard.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
            )
            binding!!.btnUsePassport.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.bg_grey
                )
            )
            binding!!.btnUsePassport.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.grey_40percent);
            binding!!.tilIdNumber.hint = "Kebele ID"
            documentSelected = "Kebele ID"
            binding!!.etIDNumber.inputType = InputType.TYPE_CLASS_NUMBER


        }
        binding!!.btnUsePassport.setOnClickListener {
            binding!!.btnOmangCard.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.bg_grey
                )
            )
            binding!!.btnUsePassport.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
            )

            binding!!.btnOmangCard.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.grey_40percent);
            binding!!.btnUsePassport.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.bg_blue);
            binding!!.tilIdNumber.hint = "Passport Number"
            documentSelected = "Passport"
            binding!!.etIDNumber.inputType = InputType.TYPE_CLASS_TEXT

        }

        binding!!.buttonContinue.setOnClickListener {
            if (validDetails()) {
                val bundle = bundleOf("documentSelected" to documentSelected)
                findNavController().navigate(
                    R.id.action_manualVerificationFragment_to_capturePhotosFragment,
                    bundle
                )

            }
        }
    }

    private fun prepopulateEditexts() {


        SharedPreferencesManager.setDateOfBirth(requireContext(), false)

        binding?.etFirstname?.setText(context?.let { SharedPreferencesManager.getFirstName(it) })

        binding?.etLastname?.setText(context?.let { SharedPreferencesManager.getLastName(it) })

        binding?.etMiddlename?.setText(context?.let { SharedPreferencesManager.getMiddleName(it) })
    }


    private fun validDetails(): Boolean {
        firstname = binding?.etFirstname?.text.toString().trim()
        secondname = binding?.etMiddlename?.text.toString().trim()
        lastname = binding?.etLastname?.text.toString().trim()
        dob = binding?.etCalender?.text.toString().trim()
        nationalid = binding?.etIDNumber?.text.toString().trim()

        if (firstname.isNullOrBlank()) {
            binding?.tilFirstName?.error = "Please enter First Name"
            return false
        } else {
            context?.let { it1 -> SharedPreferencesManager.setFirstName(it1, firstname) }

        }
        if (dob.isNullOrBlank()){
            SharedPreferencesManager.setDateOfBirth(requireContext(),false)
        }else{
            SharedPreferencesManager.setDateOfBirth(requireContext(),true)

        }
        /*if (secondname.isNullOrBlank()) {
            binding?.tilMiddleName?.error = "Please enter Second Name"
            binding?.tilMiddleName?.error
            return false
        } else {*/
        context?.let { it1 -> SharedPreferencesManager.setMiddleName(it1, secondname) }

        //}
        if (lastname.isNullOrBlank()) {
            binding?.tilLastName?.error = "Please enter Last Name"
            binding?.tilLastName?.error
            return false
        } else {
            context?.let { it1 -> SharedPreferencesManager.setLastName(it1, lastname) }

        }
        if (nationalid.isNullOrBlank() || nationalid.length < 8)
        {
            if(documentSelected.contains("Passport"))
            {
                binding?.tilIdNumber?.error = "Please enter Passport Number"
            }
            else if(documentSelected.contains("Kebele ID"))
            {
                binding?.tilIdNumber?.error = "Please enter Kebele ID Number"
            }



            return false
        } else {
            context?.let { it1 -> SharedPreferencesManager.setIDNumber(it1, nationalid) }

        }
        if (SharedPreferencesManager.hasSetDateOfBirth(requireContext()) == false) {
            binding?.etCalender?.error = "Please Select Date"
            binding?.etCalender?.requestFocus()
            return false
        }else{
            var spf = SimpleDateFormat("EEE, dd MMM yyyy")
            val newDate: Date = spf.parse(dob)
            spf = SimpleDateFormat("ddMMyyyy")
            val date = spf.format(newDate)
            SharedPreferencesManager.setDateOfBirth(requireContext(), true)
            SharedPreferencesManager.setDob( requireContext(),date.toString())
        }
        return true
    }

    private fun setSelectedDate(date: String?) {
        val parts: List<String> = date!!.split("-")
        val day = parts[0]
        val month = parts[1]
        val year = parts[2]

        val values = EthiopicCalendar(
            year.trim().toInt(),
            month.trim().toInt(),
            day.trim().toInt()
        ).ethiopicToGregorian()
        val gregDate =
            "" + values[2].toString() + " - " + values[1].toString() + " - " + values[0].toString()
        Log.e("SelectedDate 1", date);
        Log.e("SelectedDate 2", gregDate);

        setGregorianDateTime(gregDate)
        //setEthiopianDateTime(values[2], values[1],values[0])
    }

    private fun setGregorianDateTime(selectedDate: String) {
        val gregorian = binding!!.etCalender

        var spf = SimpleDateFormat("dd - MM - yyyy")
        val newDate: Date = spf.parse(selectedDate)
        spf = SimpleDateFormat("EEE, dd MMM yyyy")
        val date = spf.format(newDate)

        gregorian.setText(date)
    }

    /*
        private fun setEthiopianDateTime(dayOfMonth: Int, monthOfYear: Int, year: Int, ) {
            val ethiopianEnglish =binding!!.etCalender
           // val ethiopianAmharic = findViewById<TextView>(R.id.ethiopianAmharicDate)

            // printing to English
            val englishFormatterDate = ChronoFormatter.setUp(EthiopianCalendar.axis(), Locale.ENGLISH).addPattern("EEE, d MMMM yyyy", PatternType.CLDR_DATE).build()

            val today: PlainDate = SystemClock.inLocalView().today()
            val actualDay = today.with(DAY_OF_MONTH.newValue(dayOfMonth)).with(MONTH_OF_YEAR.newValue(
                Month.valueOf(monthOfYear))).with(YEAR.newValue(year))

            val ethiopianDate = actualDay.transform(EthiopianCalendar::class.java)
            ethiopianEnglish.text = englishFormatterDate.format(ethiopianDate)

            // parsing text in Amharic (requires Ethiopic unicode font for proper view in browser)
            val amharicFormatter = ChronoFormatter.setUp(EthiopianCalendar::class.java, Locale("am"))
                .addPattern("EEE, d MMMM ", PatternType.CLDR_DATE)
                .startSection(Attributes.NUMBER_SYSTEM, NumberSystem.ETHIOPIC)
                .addInteger(EthiopianCalendar.YEAR_OF_ERA, 1, 9)
                .endSection()
                .build()

            ethiopianAmharic.text = amharicFormatter.format(ethiopianDate)
        }
    */
    override fun onResume() {
        super.onResume()
        changeActionbarColor(Color.parseColor("#ffffff"))


    }

    /* override fun onStop() {
         super.onStop()
         makeStatusBarTransparent()
         // showSupportActionBar()
     }*/


}