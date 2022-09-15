package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.GenderAdapter

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseBottomSheetDialogFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.RelationshipAdapter
import com.ekenya.rnd.tijara.databinding.KinBottomSheetLayoutBinding
import com.ekenya.rnd.tijara.requestDTO.NextKinDTO
import com.ekenya.rnd.tijara.utils.FieldValidators
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.android.synthetic.main.colored_progressbar.*
import kotlinx.android.synthetic.main.kin_bottom_sheet_layout.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class KinBottomSheetFragment: BaseBottomSheetDialogFragment() {
    private lateinit var kinBinding: KinBottomSheetLayoutBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val kinViewmodel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(NextKinViewModel::class.java)
    }
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        kinBinding= KinBottomSheetLayoutBinding.inflate(layoutInflater)
        kinBinding.lifecycleOwner=this
        kinBinding.kinViewModel=kinViewmodel
        return kinBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kinViewmodel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                progressbar.visibility = View.GONE
                when (it) {
                    1 -> {
                        val successAlert = SweetAlertDialog(
                            context,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                        successAlert.setTitleText(getString(R.string.success))
                            .setContentText(getString(R.string.next_of_kin_details_added_successfully))
                            .setConfirmClickListener { sDialog ->
                                dismiss()
                                kinViewmodel.stopObserving()
                                sDialog.dismissWithAnimation()
                            }
                            .show()
                        successAlert.setCancelable(false)
                    }
                    0 -> {
                     //   progressbar.visibility = View.GONE
                        onInfoDialog(requireContext(),kinViewmodel.statusMessage.value)
                    }
                    else -> {
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                    }
                }
            }
        })
        /**gender spinner impl*/
        kinViewmodel.genderListProperties.observe(viewLifecycleOwner, Observer {
            val adapter= GenderAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            kinBinding.spinnerGender.adapter=adapter
            kinBinding.spinnerGender.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    PrefUtils.setPreference(context!!, "genderName", it[position].name)
                    PrefUtils.setPreference(context!!, "genderId", it[position].id.toString())
                    Constants.GENDER_ID= it[position].id.toString()
                    Constants.GENDER_NAME= it[position].name
                    Timber.d("GENDER NAME ${Constants.GENDER_NAME}" )
                    Timber.d("GENDER IDDDDD${Constants.GENDER_ID}" )
                }
            }
        })
        /**next of kin spinner impl.*/
        kinViewmodel.rShipListProperties.observe(viewLifecycleOwner,Observer {
            val rShipAdapter= RelationshipAdapter(requireContext(),it!!)
            rShipAdapter.notifyDataSetChanged()
            kinBinding.rshipSpinner.adapter=rShipAdapter
            kinBinding.rshipSpinner.onItemSelectedListener =object  : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Constants.RSHIPID= it[position].id.toString()
                    Constants.RSHIPNAME= it[position].name
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        })
        kinBinding.etDob.setOnClickListener {
            pickDob()
        }
        kinBinding.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }
            ivCancel.setOnClickListener {
                dismiss()
            }
            btnSave.setOnClickListener {
                val fadingCircle: Sprite = ThreeBounce()
                spin_kit.setIndeterminateDrawable(fadingCircle)
                val fullName=etFullName.text.toString().trim()
                val idNo=etKinId.text.toString().trim()
                val validID= FieldValidators().validNationalID(idNo)
                val phoneNo= FieldValidators().formatPhoneNumber(etKinPhoneNumber.text.toString().trim())
                val validPhone = FieldValidators().validPhoneNUmber(phoneNo)
                val mail=etKinEmail.text.toString().trim()
                val validEmail= FieldValidators().isEmailValid(mail)
                val alocate=etAllocation.text.toString().trim()
                val dob=etDob.text.toString().trim()
                val validMsg = FieldValidators.VALIDINPUT
                val userId=PrefUtils.getPreferences(requireContext(),"userid")!!


                if (fullName.isEmpty()){
                    tlFullName.error=getString(R.string.enter_full_names)
                }else if (Constants.GENDER_NAME.toLowerCase(Locale.ROOT).contains("select")) {
                    tlFullName.error=""
                    showToast(requireContext(),"Select Gender")
                }else if (Constants.RSHIPNAME.toLowerCase(Locale.ROOT).contains("select")) {
                    showToast(requireContext(),"Select Relationship")
                }else if (!validID.contentEquals(validMsg)) {
                    tlKinId.error=validID
                }else if (!validPhone.contentEquals(validMsg))  {
                tlKinPhoneNo.error=validPhone
                } else if(!validEmail.contentEquals(validMsg)) {
                    tlKinPhoneNo.error=""
                tlKinEmail.error=validEmail
                }else if (dob.isEmpty()){
                    tlKinEmail.error=""
                    tlDob.error=getString(R.string.please_enter_your_DOB)
                }else if (alocate.isEmpty()){
                    tlDob.error=""
                    tlAllocation.error=getString(R.string.enter_percentage_you_want_to_allocate)
                }else{
                    tlAllocation.error=""
                    progressbar.visibility = View.VISIBLE
                    val nextKinDTO= NextKinDTO()
                    nextKinDTO.full_name=fullName
                    nextKinDTO.relationship_id= Constants.RSHIPID
                    nextKinDTO.gender_id= Constants.GENDER_ID
                    nextKinDTO.national_identity=idNo
                    nextKinDTO.phone=phoneNo
                    nextKinDTO.email=mail
                    nextKinDTO.dob=dob
                    nextKinDTO.allocation=alocate
                    kinViewModel!!.nextOfKinInfo(nextKinDTO)
                }
            }
        }
    }
    private  fun pickDob() {
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
                    kinBinding.etDob.setText(date)
                } else {
                    kinBinding.etDob.error = "Age should be more than 18 years"
                }
            }
        DatePickerDialog(
            requireContext(), dateListener, myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}