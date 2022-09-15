package com.ekenya.rnd.tijara.ui.homepage.loan.getloan

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.TempGuarantorAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.databinding.AddGuarantorsDialogBinding
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.LoanRequestFragmentBinding
import com.ekenya.rnd.tijara.databinding.UpdateGuarantorsDialogBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.TempGuarantor
import com.ekenya.rnd.tijara.requestDTO.*
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.DashboardViewModel
import com.ekenya.rnd.tijara.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class LoanRequestFragment : BaseDaggerFragment(),TempGuarantorsCallBack {
    private lateinit var loanBinding: LoanRequestFragmentBinding
    private lateinit var viewModel: LoanRequestViewModel
    private lateinit var cardBinding: DepositDialogLayoutBinding
    private lateinit var addGuarantorDialog: AddGuarantorsDialogBinding
    private lateinit var upDateBinging: UpdateGuarantorsDialogBinding
    private lateinit var tempGuarantorAdapter: TempGuarantorAdapter
    private val loanPViewModel:LoanProductViewModel by activityViewModels()
    private var totalAmount=""
    private var accountName=""
    private var accId=-1
   private var accountNumber=""
   private var memberAmount=""
   private var memberNo=""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private val dashboardModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loanBinding = LoanRequestFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(LoanRequestViewModel::class.java)
        loanBinding.lifecycleOwner = this
        loanBinding.lRequestVeiwModel = viewModel
        viewModel.savingStatus.observe(viewLifecycleOwner){
            if (null!=it){
                when(it){
                    GeneralResponseStatus.LOADING->{
                       loanBinding.progressr.makeVisible()
                    }
                    GeneralResponseStatus.DONE->{
                        loanBinding.progressr.makeGone()
                    }
                    else->{
                        loanBinding.progressr.makeGone()
                    }
                }
            }
        }
        /**spinner TO impl*/
        viewModel.savingAccountProperties.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                populateAccount(it)
            }else{
                findNavController().navigateUp()
                toastyInfos("No saving account found")
            }
        })
        handleBackButton()
        val loanDetailsDTO = LoanDetailsDTO()
        loanDetailsDTO.productId = Constants.PRODUCTID
        viewModel.getTempGuarantors(loanDetailsDTO)
        return loanBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        /**add guarantors*/
        loanPViewModel.mustGuaranteed.observe(viewLifecycleOwner){
            if (it==true) {
                loanBinding.ClGuaantors.makeVisible()
            } else {
                loanBinding.ClGuaantors.makeGone()
            }
        }
        loanBinding.tvAddGuarantors.setOnClickListener { addGuarantors() }
        tempGuarantorAdapter = TempGuarantorAdapter(TempGuarantorAdapter.OnClickListener {  }, this)
        loanBinding.rvTempGuarantor.layoutManager = GridLayoutManager(requireActivity(), 1)
        loanBinding.rvTempGuarantor.adapter = tempGuarantorAdapter
        viewModel.statusRemove.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                loanBinding.removeSpin.visibility = View.GONE
                when (it) {
                    1 -> {
                        val loanDetailsDTO = LoanDetailsDTO()
                        loanDetailsDTO.productId = Constants.PRODUCTID
                        viewModel.getTempGuarantors(loanDetailsDTO)
                        toastySuccess(requireContext(), "Guarantor removed successfully")
                        viewModel.stopObserving()
                    }
                    0 -> {
                        loanBinding.removeSpin.visibility = View.GONE
                        onInfoDialog(requireContext(), viewModel.removeMessage.value)
                        viewModel.stopObserving()

                    }
                    else -> {
                        loanBinding.removeSpin.visibility = View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()


                    }
                }
            }
        })
        /**apply loan and commit loan*/
        loanBinding.apply {
            loanPViewModel.loanLimit.observe(viewLifecycleOwner){
                tvLoanLimitValue.text = it
            }
            btnSubmit.setOnClickListener {
                val amount = etAmount.text.toString()
                val reason = etReason.text.toString()
                if (amount.isEmpty()) {
                    tlEnterAmount.error = getString(R.string.amount_cannot_be_empty)
                } else if (accountName.isEmpty()) {
                    toastyErrors(getString(R.string.select_account))
                    tlEnterAmount.error = ""
                } else if (reason.isEmpty()) {
                    tlReason.error = getString(R.string.enter_reason_for_application)
                } else {
                    tlEnterAmount.error = ""
                    tlReason.error = ""
                    btnSubmit.isEnabled=false
                    loanBinding.progressr.makeVisible()
                    loanBinding.progressr.tv_pbTitle.makeGone()
                    loanBinding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                    val loanRequestDTO = LoanRequestDTO()
                    loanRequestDTO.amount = amount.toInt()
                    loanRequestDTO.productId = Constants.PRODUCTID.toInt()
                    loanRequestDTO.depositAccountId = accId
                    loanRequestDTO.applicationReason = reason
                    totalAmount = loanRequestDTO.amount.toString()
                    viewModel.amount.postValue(totalAmount)
                    viewModel.applyLoan(loanRequestDTO)
                }

            }
            viewModel.applyStatusCode.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    loanBinding.progressr.visibility = View.GONE
                    when (it) {
                        1 -> {
                            btnSubmit.isEnabled=false
                            val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
                            cardBinding =
                                DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                            cardBinding.apply {
                                tvHeading.text = getString(R.string.confirm_loan_request)
                                tvName.text = getString(R.string.type)
                                tvNameValue.text = Constants.PRODUCTNAME
                                tvBank.text = getString(R.string.amoun)
                                tvBankValue.text = String.format(
                                    getString(
                                        R.string.kesh,
                                        totalAmount
                                    )
                                )
                                tvACNO.text = getString(R.string.deposit_to)
                                val fromIdTo = accountNumber.replace(
                                    "(?<=.{3}).(?=.{3})".toRegex(),
                                    "*"
                                )
                                tvACNOValue.text = "$accountName - A/C $fromIdTo"
                                tvAmount.text = getString(R.string.charges)
                                tvFrom.makeGone()
                                viewModel.charges.observe(viewLifecycleOwner){charge->
                                  val formatCharge=FormatDigit.formatDigits(charge)
                                    tvAmountValue.text = String.format(
                                        getString(
                                            R.string.kesh,
                                           formatCharge
                                        )
                                    )
                                }

                                tvFromValur.makeGone()
                                tvDutyValue.makeGone()
                                tvDuty.makeGone()
                            }

                            cardBinding.btnCancel.setOnClickListener {
                                btnSubmit.isEnabled=true
                                dialog.dismiss()
                                viewModel.stopObserving()
                            }
                            cardBinding.btnSubmit.setOnClickListener {
                                dialog.dismiss()
                                loanBinding.progressr.visibility = View.VISIBLE
                                loanBinding.progressr.tv_pbTitle.visibility = View.GONE
                                loanBinding.progressr.tv_pbTex.text =
                                    getString(R.string.please_wait)
                                findNavController().navigate(R.id.action_loanRequestFragment_to_pinFragment)
                                viewModel.stopObserving()
                            }

                            dialog.setContentView(cardBinding.root)
                            dialog.show()
                            dialog.setCancelable(false)


                        }
                        0 -> {
                            btnSubmit.isEnabled=true
                            loanBinding.progressr.visibility = View.GONE
                            onInfoDialog(requireContext(), viewModel.statusMessage.value)
                        }
                        else -> {
                            btnSubmit.isEnabled=true
                            loanBinding.progressr.visibility = View.GONE
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))

                        }
                    }
                }
            })
            pinViewModel.authSuccess.observe(viewLifecycleOwner) {
                if (it == true) {
                    pinViewModel.unsetAuthSuccess()
                    loanBinding.progressr.makeVisible()
                    loanBinding.progressr.tv_pbTitle.makeGone()
                    loanBinding.progressr.tv_pbTex.text = getString(R.string.we_are_processing_requesrt)
                    viewModel.loanApplyCommit()
                    pinViewModel.stopObserving()
                    viewModel.stopObserving()

                }
            }
            viewModel.statusCommit.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    loanBinding.progressr.makeGone()
                    when (it) {
                        1 -> {
                            btnSubmit.isEnabled=true
                            loanBinding.apply {
                                etAmount.setText("")
                                etReason.setText("")
                            }
                            loanBinding.progressr.makeGone()
                            val directions=LoanRequestFragmentDirections.actionLoanRequestFragmentToLoanSuccessFragment(fragmentType = 0)
                            findNavController().navigate(directions)
                            viewModel.stopObserving()
                            dashboardModel.setRefresh(true)
                        }
                        0 -> {
                            btnSubmit.isEnabled=true
                            loanBinding.progressr.makeGone()
                            onInfoDialog(requireContext(), viewModel.statusCMessage.value)
                            viewModel.stopObserving()
                        }
                        else -> {
                            btnSubmit.isEnabled=true
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))
                            viewModel.stopObserving()

                        }
                    }
                }
            })
        }
    }
    private fun addGuarantors() {
        addGuarantorDialog = AddGuarantorsDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(addGuarantorDialog.root)
        addGuarantorDialog.BtnAddGuarantor.setOnClickListener {
            addGuarantorDialog.apply {
                val memberNumber = etMemberNo.text.toString()
                val amount = etLoanAmount.text.toString()
                if (memberNumber.isEmpty()) {
                    tlMemberNo.error = getString(R.string.enter_memebr_number_to_continue)
                } else if (amount.isEmpty()) {
                    tlMemberNo.error = ""
                    tlEnterAmount.error = getString(R.string.amount_cannot_be_empty)
                } else {
                    tlMemberNo.error = ""
                    tlEnterAmount.error = ""
                    BtnAddGuarantor.isEnabled=false
                    addGuarantorDialog.addSpin.makeVisible()
                    val addGuarantedDTO = AdGuarantorDTO()
                    addGuarantedDTO.productId = Constants.PRODUCTID.toInt()
                    addGuarantedDTO.amount = amount.toInt()
                    memberAmount = addGuarantedDTO.amount.toString()
                    addGuarantedDTO.memberNumber = memberNumber
                    memberNo = addGuarantedDTO.memberNumber
                    viewModel.addGuarantors(addGuarantedDTO)

                }
            }


        }


        viewModel.status.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                viewModel.stopObserving()
                addGuarantorDialog.addSpin.makeGone()
                when (it) {
                    1 -> {
                        addGuarantorDialog.BtnAddGuarantor.isEnabled=false
                        val cardDialogs = Dialog(requireContext(), R.style.CustomAlertDialog)
                        cardBinding =
                            DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            tvHeading.text = getString(R.string.confirm_add_guarontors)
                            tvName.text = getString(R.string.type)
                            tvNameValue.text = Constants.PRODUCTNAME
                            tvBank.text = getString(R.string.amoun)

                            tvBankValue.text =
                                String.format(getString(R.string.kesh, FormatDigit.formatDigits(memberAmount)))
                            tvACNOValue.text = memberNo
                            tvACNO.text = getString(R.string.member_number)
                            tvAmount.text = getString(R.string.member_name)
                            tvFrom.makeGone()
                            viewModel.gMemberName.observe(viewLifecycleOwner){ mName->
                                tvAmountValue.text = mName
                            }

                            tvFromValur.makeGone()
                            tvDutyValue.makeGone()
                            tvDuty.makeGone()
                        }

                        cardBinding.btnCancel.setOnClickListener {
                            addGuarantorDialog.BtnAddGuarantor.isEnabled=true
                            cardDialogs.dismiss()
                            viewModel.stopObserving()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            cardDialogs.dismiss()
                            addGuarantorDialog.addSpin.makeVisible()
                            viewModel.addGuarantorCommit()
                           viewModel.stopObserving()
                        }

                        cardDialogs.setContentView(cardBinding.root)
                        cardDialogs.show()
                        cardDialogs.setCancelable(false)
                    }
                    0 -> {
                        addGuarantorDialog.BtnAddGuarantor.isEnabled=true
                        onInfoDialog(requireContext(), viewModel.commitMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        addGuarantorDialog.BtnAddGuarantor.isEnabled=true
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()
                    }
                }
            }
        })
        viewModel.statusGCommit.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                dialog.dismiss()
                addGuarantorDialog.addSpin.makeGone()

                when (it) {
                    1 -> {
                        addGuarantorDialog.etMemberNo.setText("")
                        addGuarantorDialog.etLoanAmount.setText("")
                        addGuarantorDialog.BtnAddGuarantor.isEnabled=true
                        cardBinding.btnSubmit.isEnabled=true
                        toastySuccess(requireContext(), "Guarantor added successfully")
                        viewModel.stopObserving()
                        val loanDetailsDTO = LoanDetailsDTO()
                        loanDetailsDTO.productId = Constants.PRODUCTID
                        viewModel.getTempGuarantors(loanDetailsDTO)
                        dialog.dismiss()
                        viewModel.stopObserving()
                    }
                    0 -> {
                        addGuarantorDialog.BtnAddGuarantor.isEnabled=true
                        cardBinding.btnSubmit.isEnabled=true
                        onInfoDialog(requireContext(), viewModel.statusGMessage.value)
                        dialog.dismiss()
                        viewModel.stopObserving()
                    }
                    else -> {
                        addGuarantorDialog.BtnAddGuarantor.isEnabled=true
                        cardBinding.btnSubmit.isEnabled=true
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        dialog.dismiss()
                        viewModel.stopObserving()
                    }
                }
            }
        })

        dialog.show()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        loanBinding.toolbar.custom_toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )
        loanBinding.toolbar.custom_toolbar.custom_toolbar_title.text = String.format(
            getString(R.string.get),
            camelCase(Constants.PRODUCTNAME)
        )
    }
    override fun onItemSelected(item: TempGuarantor) {
        if (Constants.isMore){
            upDateBinging = UpdateGuarantorsDialogBinding.inflate(layoutInflater)
            val updateDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
            updateDialog.setContentView(upDateBinging.root)
            upDateBinging.etMemberNo.setText(item.tempGuarantorId.toString())
            upDateBinging.etLoanAmount.setText(item.amount.toString())
           upDateBinging.etLoanAmount.requestFocus()
            upDateBinging.BtnUpdate.setOnClickListener {
                upDateBinging.apply {
                    val memberNumber = etMemberNo.text.toString()
                    val amount = etLoanAmount.text.toString()
                    if (memberNumber.isEmpty()) {
                        tlMemberNo.error = getString(R.string.enter_memebr_number_to_continue)
                    } else if (amount.isEmpty()) {
                        tlMemberNo.error = ""
                        tlEnterAmount.error = getString(R.string.amount_cannot_be_empty)
                    } else {
                        tlEnterAmount.error = ""
                        upDateBinging.BtnUpdate.isEnabled=false
                        upDateBinging.addSpin.makeVisible()
                        val updateGuraDTO=UpdateGuraDTO()
                        updateGuraDTO.tempGuarantorId =item.tempGuarantorId
                        updateGuraDTO.amount = amount.toString()
                        updateGuraDTO.memberNumber = memberNumber
                        viewModel.updateTempGuarantors(updateGuraDTO)

                    }


                }


            }
            viewModel.statusUpdate.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    updateDialog.dismiss()
                    upDateBinging.BtnUpdate.isEnabled
                    upDateBinging.addSpin.makeGone()
                    when (it) {
                        1 -> {
                            val loanDetailsDTO = LoanDetailsDTO()
                            loanDetailsDTO.productId = Constants.PRODUCTID
                            viewModel.getTempGuarantors(loanDetailsDTO)
                            updateDialog.dismiss()
                            toastySuccess(requireContext(),getString(R.string.successful))
                            viewModel.stopObserving()
                        }
                        0 -> {
                            onInfoDialog(requireContext(), viewModel.updateMessage.value)
                            viewModel.stopObserving()
                        }
                        else -> {
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))
                            viewModel.stopObserving()
                        }
                    }
                }
            })
            updateDialog.show()
        }else{
            tempGuarantorAdapter.notifyDataSetChanged()
            loanBinding.removeSpin.visibility = View.VISIBLE
            val remGuranDTO = RemGuranDTO()
            remGuranDTO.tempGuarantorId = item.tempGuarantorId
            viewModel.removeTempGuarantors(remGuranDTO)
        }

    }
    private fun handleBackButton() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                    val loanDetailsDTO = LoanDetailsDTO()
                    loanDetailsDTO.productId = Constants.PRODUCTID
                    viewModel.deleteTempGuarantor(loanDetailsDTO)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }
    private fun populateAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
       loanBinding.apply {
            accountSpinner.setAdapter(typeAdapter)
            accountSpinner.keyListener = null
            accountSpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                accountName = selected.accountName
                accId = selected.accountId
                accountNumber = selected.accountNo
                viewModel.accountNumber.postValue(accountNumber)
                viewModel.accountName.postValue(accountName)
            }
        }
    }

}