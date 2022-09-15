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
import com.ekenya.rnd.tijara.databinding.FragmentNationalIdBinding
import com.ekenya.rnd.tijara.network.model.GenderItems
import com.ekenya.rnd.tijara.requestDTO.BasicInfoDTO
import com.ekenya.rnd.tijara.utils.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import java.text.SimpleDateFormat
import java.util.*


class NationalIDFragment : Fragment() {
  private lateinit var binding:FragmentNationalIdBinding
  private lateinit var viewModel:RegistrationViewModel
    private var genderName=""

    /**Instantiate our class scanner interface*/
   /* private var scannerInterfaceImp = ScannerInterfaceImp()
    *//**call the fun CardResult from class ScannerInterfaceImp that override two fun*//*
    private val cardResultsCallback: ScannerInterfaceImp.CardResults = object : ScannerInterfaceImp.CardResults {
        *//**OnCardResults we are setting the scan details we have retrieved from the ID Card*//*

        override fun onCardResult(scanResult: IdScanResult?) {
            if (scanResult != null) {
                // val ivCardPrev=binding.imgTemplate
                //  ivCardPrev.visibility = View.GONE
                *//**set the face image bitmap from the ID as a content of imageview*//*
                //  binding.ivFaceId.setImageBitmap(scanResult.face)
                //  idBitmap = scanResult.face
                extractData(scanResult.details)
            }
        }

        override fun onCardFailure(error: String) {
            toastyInfos( "Error: $error")
        }
    }
    val args:NationalIDFragmentArgs by navArgs()*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNationalIdBinding.inflate(layoutInflater)
        viewModel=ViewModelProvider(requireActivity()).get(RegistrationViewModel::class.java)
        initUI()
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

        binding.apply {
            imageBack.setOnClickListener {
                findNavController().navigateUp()
            }

        }
        binding.apply {
            btnContinue.setOnClickListener {
                val fName=etFname.text.toString().trim()
                val mName=etMName.text.toString().trim()
                val lName=etSurname.text.toString().trim()
                val id=etIdNo.text.toString().trim()
                val datob=etDob.text.toString().trim()
                val mobile= PrefUtils.getPreferences(requireContext(), "mobile")
                when {
                    fName.isEmpty() -> {
                      tlFname.error=getString(R.string.required)
                    }
                    mName.isEmpty() -> {
                        tlFname.error=""
                        tlFname.clearFocus()
                      tlMName.error=getString(R.string.required)
                    }
                    lName.isEmpty() -> {
                        tlMName.error=""
                        tlMName.clearFocus()
                        tlSname.error=getString(R.string.required)
                    }
                    genderName.isEmpty() -> {
                        toastyInfos(getString(R.string.select_gender))
                    }
                    id.isEmpty() -> {
                        tlIdNo.error=getString(R.string.required)
                    }
                    datob.isEmpty() -> {
                    tlIdNo.error=""
                    tlIdNo.clearFocus()
                    tlDob.error = getString(R.string.required)
                    }
                    else -> {
                        tlFname.error=""
                        tlMName.error=""
                        tlSname.error=""
                        tlIdNo.error=""
                        tlDob.error=""
                        requireActivity().window.statusBarColor = resources.getColor(R.color.spinkit_color)
                        binding.btnContinue.isEnabled=false
                        val basicInfoDTO=BasicInfoDTO()
                        basicInfoDTO.first_name=fName
                        basicInfoDTO.middle_name=mName
                        basicInfoDTO.last_name=lName
                        basicInfoDTO.national_identity=id
                        basicInfoDTO.gender=genderName
                        basicInfoDTO.dob= datob.trim()
                        basicInfoDTO.org_id=Constants.SIGNUPORGID
                        basicInfoDTO.phone=mobile.toString()
                        binding.progressr.visibility=View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility=View.GONE
                        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                        viewModel.registerBasicInfo(basicInfoDTO)


                    }
                }



            }
            viewModel.status.observe(viewLifecycleOwner) {
                if (null != it) {
                    binding.btnContinue.isEnabled = true
                    requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                    binding.progressr.makeGone()
                    viewModel.stopObserving()
                    when (it) {
                        1 -> {
                            viewModel.stopObserving()
                            binding.btnContinue.isEnabled = true
                            binding.progressr.makeGone()
                            Constants.FromID = 0
                            findNavController().navigate(R.id.action_nationalIDFragment_to_capturePhotoFragment)

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etDob.setOnClickListener {
            pickDob()
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
                    toastyErrors(getString(R.string.age_should_be_more_than_18years))
                }
            }
        myCalendar.add(Calendar.YEAR, -18)
        val dialog = DatePickerDialog(
            requireContext(), dateListener, myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
        dialog.datePicker.maxDate = myCalendar.timeInMillis
        dialog.show()
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

    private fun initUI() {
        /**On this button click we call the fun ScanId which opens a scanning area
         * params ->context, ID Card and The scanned results form the captured image*/

    }

    /**
     * Method for requesting camera permission
     */



}