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
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.ekenya.rnd.tijara.adapters.spinnerAdapter.BankAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.BankBranchAdapter
import com.ekenya.rnd.tijara.databinding.BankDetailBottomSheetBinding
import com.ekenya.rnd.tijara.requestDTO.BankBranchDTO

import com.ekenya.rnd.tijara.requestDTO.BankDetailsDTO
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails.BankListViewModel

import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.android.synthetic.main.bank_detail_bottom_sheet.*
import kotlinx.android.synthetic.main.colored_progressbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class BankBottomSheetFragment: BaseBottomSheetDialogFragment() {
    private lateinit var bankBinding: BankDetailBottomSheetBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val bankViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(BankDetailsViewModel::class.java)
    }
    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(BankListViewModel::class.java)
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
        requireActivity().window.statusBarColor = resources.getColor(R.color.buttonColor)
        bankBinding= BankDetailBottomSheetBinding.inflate(layoutInflater)
        bankBinding.lifecycleOwner=this
        bankBinding.bankViewModel=bankViewModel
        return bankBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bankViewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                progressbar.visibility = View.GONE
                when (it) {
                    1 -> {
                        bankViewModel.stopObserving()
                        viewModel.getBankInfo()
                        val successAlert = SweetAlertDialog(
                            context,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                        successAlert.setTitleText(getString(R.string.success))
                            .setContentText(getString(R.string.bank_details_added_successfully))
                            .setConfirmClickListener { sDialog ->
                                dismiss()
                                bankViewModel.stopObserving()
                                sDialog.dismissWithAnimation()
                            }
                            .show()
                        successAlert.setCancelable(false)
                    }
                    0 -> {
                        bankViewModel.stopObserving()
                        onInfoDialog(
                            requireContext(),
                            bankViewModel.statusMessage.value
                        )
                    }
                    else -> {
                        bankViewModel.stopObserving()
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                    }
                }
            }
        })
        bankBinding.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }
            ivCancel.setOnClickListener {
                dismiss()
            }
        }
        bankBinding.btnSave.setOnClickListener {
            bankBinding.apply {
                val fadingCircle: Sprite = ThreeBounce()
                spin_kit.setIndeterminateDrawable(fadingCircle)
                val acNumber=etAcNumber.text.toString().trim()
                val acName=etAcName.text.toString().trim()
                val defaultAcc: Int
                if (bankBinding.cbAccount.isChecked){
                    defaultAcc=1
                    Timber.d(" Checked is: $defaultAcc")
                }else{
                    defaultAcc=0
                    Timber.d(" Unchecked is: $defaultAcc")

                }
                when {
                    Constants.BANKNAME.toLowerCase(Locale.ROOT).isEmpty() -> {
                        showToast(requireContext(),"Select Bank")
                    }
                    Constants.BANKBRANCHNAME.toLowerCase(Locale.ROOT).isEmpty() -> {
                        showToast(requireContext(),"Select Bank Branch")
                    }
                    acNumber.isEmpty() -> {
                        etAcNumber.isFocusable
                        tlAcNo.error=getString(R.string.enter_account_number)
                    }
                    acName.isEmpty() -> {
                        etAcNumber.clearFocus()
                        tlAcNo.error=""
                        etAcName.isFocusable
                        tiAcName.error=getString(R.string.enter_account_name)
                    }
                    else -> {
                        tiAcName.error=""
                        progressbar.visibility = View.VISIBLE
                        val bankDetailsDTO= BankDetailsDTO()
                        bankDetailsDTO.bank_id= Constants.BANKID
                        bankDetailsDTO.branch_id= Constants.BANKBRANCHID
                        bankDetailsDTO.account_no=acNumber
                        bankDetailsDTO.account_name=acName
                        bankDetailsDTO.is_default_account= defaultAcc
                        bankViewModel!!.bankDetails(bankDetailsDTO)

                    }
                }
            }



        }
        /**bank spinner impl*/
        bankViewModel.bankListProperties.observe(viewLifecycleOwner, Observer {
            val adapter= BankAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            bankBinding.bankSpinner.adapter=adapter
            bankBinding.bankSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.BANKNAME=it[position].name
                    Constants.BANKID=it[position].id.toString()
                    bankViewModel.setBankId(it[position].id.toString())
                    val bankBranchDTO= BankBranchDTO()
                    bankBranchDTO.bank_id= it[position].id.toString()
                    bankViewModel.getBankBraches(bankBranchDTO)
                }
            }
        })
        bankViewModel.bankId.observe(viewLifecycleOwner,{
            val bankBranchDTO= BankBranchDTO()
            bankBranchDTO.bank_id= it.toString()
            bankViewModel.getBankBraches(bankBranchDTO)
        })
        /**bank branches spinner impl*/
        bankViewModel.bBranchListProperties.observe(viewLifecycleOwner, Observer {
            val adapter= BankBranchAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            bankBinding.bankBranchSpinner.adapter=adapter
            bankBinding.bankBranchSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.BANKBRANCHNAME=it[position].name
                    Constants.BANKBRANCHID=it[position].id.toString()

                }
            }
        })
    }


}