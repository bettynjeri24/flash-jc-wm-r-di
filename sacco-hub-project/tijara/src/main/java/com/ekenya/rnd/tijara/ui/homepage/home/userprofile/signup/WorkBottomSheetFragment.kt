package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseBottomSheetDialogFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.EmpTermAdapter
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.EmployerAdapter
import com.ekenya.rnd.tijara.databinding.WorkBottomSheetFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.WorkDTO
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.android.synthetic.main.colored_progressbar.*
import kotlinx.android.synthetic.main.work_bottom_sheet_fragment.*
import java.util.*
import javax.inject.Inject


class WorkBottomSheetFragment: BaseBottomSheetDialogFragment() {
    private lateinit var workBinding: WorkBottomSheetFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val workViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(WorkDetailsViewModel::class.java)
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
        workBinding= WorkBottomSheetFragmentBinding.inflate(layoutInflater)
        workBinding.lifecycleOwner=this
        workBinding.wViewModel=workViewModel

        return workBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**employer/company spinner implementation*/
        workViewModel.employerListProperties.observe(viewLifecycleOwner, Observer {
            val workAdapter = EmployerAdapter(requireContext(), it!!)
            workAdapter.notifyDataSetChanged()
            workBinding.empSpinner.adapter = workAdapter
            workBinding.empSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Constants.EMPLOYERID = it[position].id.toString()
                        Constants.EMPLOYERNAME = it[position].name

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }
        })
        /**employment term spinner implementation*/
        workViewModel.empTermListProperties.observe(viewLifecycleOwner, Observer {
            val empTermAdapter = EmpTermAdapter(requireContext(), it!!)
            empTermAdapter.notifyDataSetChanged()
            workBinding.empTermSpinner.adapter = empTermAdapter
            workBinding.empTermSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Constants.EMPTERMID= it[position].id.toString()
                        Constants.EMPTERMNAME= it[position].name

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }
        })
        workViewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                progressbar.visibility = View.GONE
                when (it) {
                    1 -> {
                        val successAlert = SweetAlertDialog(
                            context,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                        successAlert.setTitleText(getString(R.string.success))
                            .setContentText(getString(R.string.work_details_added_successfully))
                            .setConfirmClickListener { sDialog ->
                                dismiss()
                                workViewModel.stopObserving()
                                sDialog.dismissWithAnimation()
                            }
                            .show()
                        successAlert.setCancelable(false)
                    }
                    0 -> {
                        onInfoDialog(
                            requireContext(),
                            workViewModel.statusMessage.value
                        )
                        //  showErrorDialog(requireContext(),getString(R.string.invalid_passwordorusername))
                    }
                    else -> {
                        onInfoDialog(
                            requireContext(),
                            getString(R.string.error_occurred)
                        )
                    }
                }
            }
        })

        workBinding.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }
            ivCancel.setOnClickListener {
                dismiss()
            }
            btnSave.setOnClickListener {
                val fadingCircle: Sprite = ThreeBounce()
                spin_kit.setIndeterminateDrawable(fadingCircle)
                val dpment = etDepartmnt.text.toString().trim()
                val workStation=etWorkStation.text.toString().trim()
                val employeeId = etEmployee.text.toString().trim()
                val userId= PrefUtils.getPreferences(requireContext(),"userid")!!

                if (Constants.EMPLOYERNAME.toLowerCase(Locale.ROOT).contains("select")) {
                    showToast(requireContext(), getString(R.string.select_company_to_continue))
                }else if (Constants.EMPTERMNAME.toLowerCase(Locale.ROOT).contains("select")){
                    showToast(requireContext(), getString(R.string.select_employment_term_to_continue))

                }else if (employeeId.isEmpty()) {
                    tlEmployee.error = getString(R.string.enter_employee_number)
                }else if (dpment.isEmpty()) {
                    tlEmployee.error =""
                        tlDepartmnt.error = getString(R.string.enter_your_department)
                } else if (workStation.isEmpty()){
                    tlDepartmnt.error=""
                    tlWorkStation.error=getString(R.string.enter_your_workstation)
                }else{
                    tlWorkStation.error=""
                    progressbar.visibility = View.VISIBLE
                   val workDTO= WorkDTO()
                    workDTO.employer_id= Constants.EMPLOYERID
                    workDTO.employment_term_id= Constants.EMPTERMID
                    workDTO.employment_id=employeeId
                    workDTO.department=dpment
                    workDTO.work_station=workStation
                    workViewModel.getWorkInfo(workDTO)
                }
            }
        }
    }

}