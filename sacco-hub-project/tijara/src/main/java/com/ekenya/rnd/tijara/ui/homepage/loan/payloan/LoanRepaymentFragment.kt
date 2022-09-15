package com.ekenya.rnd.tijara.ui.homepage.loan.payloan

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ServiceProviderAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.LoanRepaymentFragmentBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.PayFrmAccDTO
import com.ekenya.rnd.tijara.requestDTO.PayLoanDTO
import com.ekenya.rnd.tijara.requestDTO.SavingAccDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.DashboardViewModel
import com.ekenya.rnd.tijara.ui.homepage.loan.getloan.LoanProductViewModel
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

class LoanRepaymentFragment : BaseDaggerFragment() {
    private lateinit var binding: LoanRepaymentFragmentBinding
    private val loanPViewModel: LoanProductViewModel by activityViewModels()
    private var totalAmount=""
    private var phoneNo=""
    private var accountName=""
    private var accId=-1
    private var accountNumber=""

    private lateinit var cardBinding: DepositDialogLayoutBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private val dashboardModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)
    }
    private lateinit var viewModel : LoanRepaymentViewModel
    private var isCardSelected = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= LoanRepaymentFragmentBinding.inflate(layoutInflater)
       viewModel= ViewModelProvider(requireActivity()).get(LoanRepaymentViewModel::class.java)
        viewModel.savingPStatus.observe(viewLifecycleOwner){
            if (null!=it){
                when(it){
                    GeneralResponseStatus.LOADING->{
                    binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_accounts)
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    binding.progressr.makeVisible()
                    }
                    GeneralResponseStatus.DONE->{
                        binding.progressr.makeGone()
                    }

                }
            }
        }
        pinViewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                pinViewModel.unsetAuthSuccess()
                binding.progressr.makeVisible()
                binding.progressr.tv_pbTitle.makeGone()
                binding.progressr.tv_pbTex.text = getString(R.string.we_are_processing_requesrt)
                viewModel.loanRepaymentCommit()
                pinViewModel.stopObserving()
                viewModel.stopObserving()

            }

        }
        viewModel.statusPay.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                when (it) {
                    1 -> {
                        binding.BtnPayLoan.isEnabled=true
                        binding.apply {
                            etLoanAmount.setText("")
                            etPhone.setText("")
                        }
                        binding.progressr.makeGone()
                        val directions=LoanRepaymentFragmentDirections.actionLoanRepaymentFragmentToLoanSuccessFragment(fragmentType = 1)
                        findNavController().navigate(directions)
                        viewModel.stopObserving()
                        dashboardModel.setRefresh(true)
                    }
                    0 -> {
                        binding.BtnPayLoan.isEnabled=true
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(),viewModel.messagePay.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.BtnPayLoan.isEnabled=true
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        binding.progressr.makeGone()
                        viewModel.stopObserving()

                    }
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        loanPViewModel.amountBorrowed.observe(viewLifecycleOwner){
            binding.etLoanAmount.setText(it)
        }
        binding.clServiceProvider.makeVisible()
        binding.etPhone.setText(Constants.PHONENUMBER)
        binding.apply {
            /*tvTotalLoan.text= Constants.AMOUNTAPPLIED
            tvAmountPaid.text= Constants.AMOUNTPAID
            tvLoanBal.text= Constants.LOANBALANCE*/
            rbMobileMoney.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked){
                    rbMobileMoney.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_less_radius)
                    rbSavingAccount.background = ContextCompat.getDrawable(requireContext(), R.drawable.transparent_bg)
                    rbSavingAccount.isChecked=false
                    clServiceProvider.visibility=View.VISIBLE
                    CLAccount.visibility=View.GONE
                    isCardSelected=1
                }

            }
            rbSavingAccount.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked){
                    rbMobileMoney.background = ContextCompat.getDrawable(requireContext(), R.drawable.transparent_bg)
                    rbSavingAccount.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_less_radius)
                    rbMobileMoney.isChecked=false
                    CLAccount.visibility=View.VISIBLE
                    clServiceProvider.visibility=View.GONE
                    isCardSelected=2

                }

            }
        }
        viewModel.sProviderProperties.observe(viewLifecycleOwner, Observer {
            val adapter= ServiceProviderAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.providerSpinner.adapter=adapter
            binding.providerSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.SPROVIDERID= it[position].id.toString()
                    Constants.SPROVIDERNAME= it[position].name
                    Timber.d("SPROVIDERNAME is:  ${Constants.SPROVIDERNAME}")
                }
            }
        })
        viewModel.savingAccountProperties.observe(viewLifecycleOwner) {
            if (it!=null){
                populateAccount(it)
            }else{
                findNavController().navigateUp()
                toastyInfos("No saving account found")
            }
        }
        viewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.makeGone()
                when (it) {
                    1 -> {
                        binding.BtnPayLoan.isEnabled=false
                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardBinding= DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            tvHeading.text=getString(R.string.confirm_loan_repayment)
                            tvName.text=getString(R.string.type)
                            tvNameValue.text=Constants.PRODUCTNAME
                            tvBank.text=getString(R.string.amoun)
                            tvBankValue.text= String.format(getString(R.string.kesh,FormatDigit.formatDigits(totalAmount)))
                            if (isCardSelected==1){
                                Constants.SELECTED_TYPE=1
                                tvACNOValue.text=phoneNo
                            }else{
                                Constants.SELECTED_TYPE=2
                                val fromIdTo=accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                                tvACNOValue.text="$accountName - A/C $fromIdTo"
                            }
                            tvACNO.text=getString(R.string.pay_from)
                            tvAmount.text=getString(R.string.charges)
                            tvFrom.makeGone()
                            viewModel.charges.observe(viewLifecycleOwner){charge->
                                tvAmountValue.text=String.format(getString(R.string.kesh),FormatDigit.formatDigits(charge))
                            }
                            tvFromValur.makeGone()
                            tvDuty.makeGone()
                            tvDutyValue.makeGone()
                        }

                        cardBinding.btnCancel.setOnClickListener {
                            binding.BtnPayLoan.isEnabled=true
                            dialog.dismiss()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_loanRepaymentFragment_to_pinFragment)
                            viewModel.stopObserving()
                        }

                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)
                    }
                    0 -> {
                        binding.BtnPayLoan.isEnabled=true
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.BtnPayLoan.isEnabled=true
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()
                    }
                }
            }
        })
        binding.BtnPayLoan.setOnClickListener {
            validateFields()
        }

    }
    private fun validateFields(){
        binding.apply {
            var amount = etLoanAmount.text.toString()
            val validMsg = FieldValidators.VALIDINPUT
            val phoneNumber = FieldValidators().formatPhoneNumber(binding.etPhone.text.toString())
            val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
            when(isCardSelected){
                1-> {

                    if (amount.isEmpty()) {
                        tlEnterAmount.error = getString(R.string.amount_cannot_be_empty)
                    } else if (Constants.SPROVIDERNAME.isEmpty()) {
                        toastyErrors("Select Service Provider")
                    } else if (!validPhone.contentEquals(validMsg)) {
                        etPhone.requestFocus()
                        tlPhone.isErrorEnabled = true
                        tlPhone.error = validPhone
                    }else{
                        binding.BtnPayLoan.isEnabled=false
                        tlEnterAmount.error=""
                        tlPhone.error=""
                        binding.progressr.makeVisible()
                        binding.progressr.tv_pbTitle.makeGone()
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        val payLoanDTO=PayLoanDTO()
                        payLoanDTO.amount = amount
                        payLoanDTO.loanId = Constants.LOANID
                        payLoanDTO.providerId=Constants.SPROVIDERID
                        totalAmount= payLoanDTO.amount.toString()
                        payLoanDTO.providerPhone=phoneNumber
                        phoneNo=payLoanDTO.providerPhone
                        viewModel.phone.postValue(phoneNo)
                        viewModel.amountValue.postValue(totalAmount)
                        payLoanDTO.payAll=0
                        viewModel.repayLoan(payLoanDTO)

                    }
                }
                2->{
                    if (accountName.isEmpty() ) {
                        toastyErrors("Select Account")
                    }else{
                        binding.BtnPayLoan.isEnabled=false
                        binding.progressr.makeVisible()
                        binding.progressr.tv_pbTitle.makeGone()
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        val loanRepayDTO = PayFrmAccDTO()
                        loanRepayDTO.amount = amount
                        totalAmount= loanRepayDTO.amount.toString()
                        viewModel.amountValue.postValue(totalAmount)
                        loanRepayDTO.loanId = Constants.LOANID
                        loanRepayDTO.accountId= accId
                        loanRepayDTO.payAll=0
                        viewModel.payFromAccount(loanRepayDTO)
                    }
                }
            }

        }
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = String.format(getString(R.string.repay),Constants.PRODUCTNAME)
    }
    private fun populateAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        binding.apply {
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