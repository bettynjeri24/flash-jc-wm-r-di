package com.ekenya.rnd.tijara.ui.auth.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentPassportBinding
import com.ekenya.rnd.tijara.network.model.GenderItems
import com.ekenya.rnd.tijara.requestDTO.BasicInfoDTO
import com.ekenya.rnd.tijara.utils.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import java.text.SimpleDateFormat
import java.util.*

class PassportFragment : Fragment() {
private lateinit var binding:FragmentPassportBinding
    private lateinit var viewModel:RegistrationViewModel
    private var adapter: ArrayAdapter<*>? = null
    private var genderName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPassportBinding.inflate(layoutInflater)
        viewModel= ViewModelProvider(requireActivity()).get(RegistrationViewModel::class.java)
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
        val gender= listOf(  "Female",
            "Male",
            "Others"
        )
        /**gender spinner impl*/
        viewModel.genderListProperties.observe(viewLifecycleOwner) {
            if (it != null) {
                populateGender(it)
            } else {
                Toasty.error(
                    requireContext(),
                    "An error occurred. Please try again",
                    Toasty.LENGTH_LONG
                ).show()
            }
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etDob.setOnClickListener {
            pickDob()
        }
        requireActivity().window.statusBarColor = resources.getColor(R.color.spinkit_color)
        binding.progressr.visibility=View.VISIBLE
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_gender)
        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        binding.apply {
            btnContinue.setOnClickListener {
                val fName=etFname.text.toString().trim()
                val lName=etSurname.text.toString().trim()
                val id=etIdNo.text.toString().trim()
                val datob=etDob.text.toString().trim()
                val mobile= PrefUtils.getPreferences(requireContext(), "mobile")
                when {
                    fName.isEmpty() -> {
                        tlFname.error=getString(R.string.required)
                    }
                    lName.isEmpty() -> {
                        tlFname.error=""
                        tlFname.clearFocus()
                        tlSname.error=getString(R.string.required)
                    }
                    genderName.isEmpty() -> {
                        toastyInfos(getString(R.string.select_gender))
                    }
                    id.isEmpty() -> {
                        tlFname.error=""
                        genderSpinner.clearFocus()
                        tlIdNo.error=getString(R.string.required)
                    }
                    datob.isEmpty() -> {
                        tlIdNo.error=""
                        tlIdNo.clearFocus()
                        tlDob.error = getString(R.string.required)
                    }
                    else -> {
                        tlFname.error=""
                        tlSname.error=""
                        tlIdNo.error=""
                        tlDob.error=""
                        requireActivity().window.statusBarColor = resources.getColor(R.color.spinkit_color)
                        val basicInfoDTO=BasicInfoDTO()
                        basicInfoDTO.first_name=fName
                        basicInfoDTO.last_name=lName
                        basicInfoDTO.national_identity=id
                        basicInfoDTO.gender=genderName
                        basicInfoDTO.dob= datob.trim()
                        basicInfoDTO.org_id=Constants.SIGNUPORGID
                        basicInfoDTO.phone=mobile.toString()
                        binding.progressr.visibility=View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility=View.GONE
                        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                        binding.btnContinue.isEnabled=false
                        viewModel.registerBasicInfo(basicInfoDTO)
                    }
                }



            }
            viewModel.status.observe(viewLifecycleOwner) {
                if (null != it) {
                    requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                    binding.btnContinue.isEnabled = true
                    binding.progressr.makeGone()
                    viewModel.stopObserving()
                    when (it) {
                        1 -> {
                            viewModel.stopObserving()
                            binding.btnContinue.isEnabled = true
                            binding.progressr.makeGone()
                            Constants.FromID = 1
                            findNavController().navigate(R.id.action_passportFragment_to_passPortPhotoFragment)

                        }
                        0 -> {
                            viewModel.stopObserving()
                            binding.btnContinue.isEnabled = true
                            binding.progressr.makeGone()
                            onInfoDialog(requireContext(), viewModel.statusMessage.value)
                        }

                        else -> {
                            viewModel.stopObserving()
                            binding.btnContinue.isEnabled = true
                            binding.progressr.makeGone()
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        }
                    }
                }
            }

        }
        viewModel.genderCode.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.progressr.visibility = View.GONE
                when (it) {
                    1 -> {
                        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                        binding.progressr.visibility = View.GONE
                        viewModel.stopObserving()
                    }
                    0 -> {
                        waringAlertDialogUp(
                            requireContext(),
                            requireView(),
                            getString(R.string.oops_we_are_sorry),
                            getString(R.string.unable_to_complete_your_request)
                        )
                        binding.progressr.visibility = View.GONE
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.progressr.visibility = View.GONE
                    }
                }
            }
        }
    }
    private fun pickDob() {
        val dateListener: DatePickerDialog.OnDateSetListener
        val myCalendar = Calendar.getInstance()
        val currYear = myCalendar[Calendar.YEAR]
        val currMonth = myCalendar[Calendar.MONTH]
        val currDay = myCalendar[Calendar.DAY_OF_MONTH]
        dateListener =
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                var isDateOk = true
                if (currYear - year < 18) isDateOk = false else if (currYear == 18) {
                    if (currMonth - monthOfYear < 0) isDateOk = false
                    if (currMonth == monthOfYear && currDay - dayOfMonth < 0) isDateOk = false
                }
                if (isDateOk) {
                    val preferredFormat = "dd-MM-yyyy"
                    val date =
                        SimpleDateFormat(preferredFormat, Locale.US).format(myCalendar.time)
                    binding.etDob.setText(date)
                } else {
                    binding.tlDob.error = getString(R.string.age_should_be_more_than_18years)
                }
            }
        DatePickerDialog(
            requireContext(), dateListener, myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
    private fun populateGender(genderList: List<GenderItems>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genderList)
        binding.genderSpinner.setAdapter(typeAdapter)
        binding.genderSpinner.keyListener = null
        binding.genderSpinner.setOnItemClickListener { parent, _, position, _ ->
            val selected: GenderItems = parent.adapter.getItem(position) as GenderItems
            genderName=selected.name
        }
    }


}