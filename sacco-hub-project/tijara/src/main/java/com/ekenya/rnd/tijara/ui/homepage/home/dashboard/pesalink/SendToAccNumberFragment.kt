package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentSendToAccNumberBinding
import com.ekenya.rnd.tijara.requestDTO.STAccountDTO
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.BankAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.network.model.BankList
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SendToAccNumberFragment : BaseDaggerFragment() {
    private lateinit var binding: FragmentSendToAccNumberBinding
    private var accountName=""
    private var accountId=-1
    private var accountNumber=""
    private var totalAmount=""
    private var recName=""
    private var recAccNo=""
    private var bankId=-1
    private var bankName=""

   private lateinit var cardBinding: DepositDialogLayoutBinding
    @Inject
     lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewmodel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SendToAccNumberViewmodel::class.java)
    }
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSendToAccNumberBinding.inflate(layoutInflater)
        setupNavUp()

        /**spinner impl*/
        viewmodel.savingAccountProperties.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                populateAccount(it)
            }else{
                toastyInfos("No saving account found")
                findNavController().navigateUp()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressr.visibility=View.VISIBLE
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_accounts)
        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        viewmodel.loadingAccount.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.progressr.makeVisible()
                }
                false -> {
                    binding.progressr.visibility = View.GONE
                }
                else -> {
                    binding.progressr.makeGone()
                }
            }
        }
        viewmodel.accountCode.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {

                    1 -> {
                        viewmodel.stopObserving()
                    }
                    0 -> {
                        waringAlertDialogUp(
                            requireContext(),
                            requireView(),
                            getString(R.string.oops_we_are_sorry),
                            getString(R.string.unable_to_complete_your_request)
                        )
                        viewmodel.stopObserving()
                    }
                    else -> {
                        viewmodel.stopObserving()
                    }
                }
            }
        }
        /**bank spinner impl*/
        viewmodel.bankListProperties.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                populateBanks(it)
            }else{
                toastyInfos("No bank accounts found")
            }
        })
        viewmodel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {
                    1 -> {
                        binding.content.btnSubmit.isEnabled=false
                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardBinding= DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            tvHeading.text=getString(R.string.confirm_pesalink_to_account)
                            tvNameValue.text=recName
                            tvName.text=getString(R.string.recipient_name)
                            tvBankValue.text=bankName
                            tvBank.text=getString(R.string.recipient_bank)
                            tvACNO.text=getString(R.string.recipient_acc_no)
                            val recAcc= recAccNo.replace("(?<=.{5}).(?=.{5})".toRegex(),"*")
                            tvACNOValue.text=recAcc
                            val amount=FormatDigit.formatDigits(totalAmount)
                            tvAmountValue.text= String.format(getString(R.string.kesh),amount)
                            tvFrom.text=getString(R.string.transfer_from)
                            val accNumber=accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                            tvFromValur.text= "$accountName - A/C $accNumber"
                            tvDuty.text=getString(R.string.charges)
                            viewmodel.charges.observe(viewLifecycleOwner) { charges ->
                                val finalCharges = FormatDigit.formatDigits(charges)
                                tvDutyValue.text =
                                    String.format(getString(R.string.kesh), finalCharges)
                            }
                        }
                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            findNavController().navigate(R.id.pinFragment)
                            viewmodel.stopObserving()
                        }
                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)
                        viewmodel.stopObserving()
                    }
                    0 -> {
                        binding.content.btnSubmit.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewmodel.statusMessage.value)
                        viewmodel.stopObserving()
                    }
                    else -> {
                        binding.content.btnSubmit.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewmodel.stopObserving()
                    }
                }
            }
        })
        pinViewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                pinViewModel.unsetAuthSuccess()
                binding.progressr.visibility = View.VISIBLE
                binding.progressr.tv_pbTitle.visibility = View.GONE
                binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                viewmodel.commitToAccount()
                pinViewModel.stopObserving()
                viewmodel.stopObserving()

            }
        }
        viewmodel.statusCommit.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        binding.content.btnSubmit.isEnabled=true
                        with(binding.content) {
                            etAmount.setText("")
                            etAcNumber.setText("")
                            etRecpName.setText("")
                            etReason.setText("")
                        }
                        val directions=SendToAccNumberFragmentDirections.actionSendToAccNumberFragmentToSendMoneySuccessFragment(fragmentType = 1)
                       findNavController().navigate(directions)
                        viewmodel.stopObserving()

                    }
                    0 -> {
                        binding.content.btnSubmit.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewmodel.statusCMessage.value)
                        viewmodel.stopObserving()
                    }
                    else -> {
                        binding.content.btnSubmit.isEnabled=true
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewmodel.stopObserving()

                    }
                }
            }
        })
        binding.content.btnSubmit.setOnClickListener {
            with(binding.content) {
                val validMsg = FieldValidators.VALIDINPUT
                val amount=etAmount.text.toString()
                val acNumber=etAcNumber.text.toString().trim()
                val validAcNumber= FieldValidators().validBankAccount(acNumber)
                val acName=etRecpName.text.toString().trim()
                val narr=etReason.text.toString().trim()
                when {
                    amount.isEmpty()->{
                        tlEnterAmount.error=getString(R.string.required)
                    }
                    !validAcNumber.contentEquals(validMsg) -> {
                         tlEnterAmount.error=""
                         etAcNumber.isFocusable
                         tlAcNo.error=validAcNumber
                    }
                    acName.isEmpty() -> {
                        etAcNumber.clearFocus()
                        tlAcNo.error=""
                        etRecpName.isFocusable
                        tiRecpName.error=getString(R.string.required)
                    }
                    bankName.isEmpty() -> {
                        tiRecpName.error=""
                        showToast(requireContext(),"Select Bank")
                    }
                    accountName.isEmpty() -> {
                        toastyInfos("Select Account")
                    }else -> {
                    tlEnterAmount.error=""
                    tlAcNo.error=""
                    tiRecpName.error=""
                    binding.content.btnSubmit.isEnabled=false
                    val stAccountDTO= STAccountDTO()
                    stAccountDTO.transferType="A2A"
                    stAccountDTO.amount=amount
                    stAccountDTO.recipientAccountNo=acNumber
                    stAccountDTO.recipientName=acName
                    stAccountDTO.bankId= bankId.toString()
                    stAccountDTO.accountId= accountId
                    stAccountDTO.narration=narr
                    binding.progressr.visibility=View.VISIBLE
                    binding.progressr.tv_pbTitle.visibility=View.GONE
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    recName=stAccountDTO.recipientName
                    viewmodel.setRecName(stAccountDTO.recipientName)
                    recAccNo=stAccountDTO.recipientAccountNo
                    viewmodel.setResAccNo(stAccountDTO.recipientAccountNo)
                    totalAmount=stAccountDTO.amount
                    viewmodel.setAmount(stAccountDTO.amount)
                    viewmodel.sendMoneyToAccount(stAccountDTO)
                    }
                }

            }
        }
    }
    private fun populateAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        with( binding.content){
            accountSpinner.setAdapter(typeAdapter)
            accountSpinner.keyListener = null
            accountSpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                accountName = selected.accountName
                accountId = selected.accountId
                accountNumber = selected.accountNo
                viewmodel.setAccNumber(selected.accountNo)
                viewmodel.setAccountName(selected.accountName)
            }
        }
    }
    private fun populateBanks(bankList: List<BankList>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, bankList)
        with( binding.content){
            bankSpinner.setAdapter(typeAdapter)
            bankSpinner.keyListener = null
            bankSpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: BankList =
                    parent.adapter.getItem(position) as BankList
                bankName = selected.name
                bankId = selected.id
                viewmodel.setBankName(selected.name)
            }
        }
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.pesalink_to_account)
    }


}