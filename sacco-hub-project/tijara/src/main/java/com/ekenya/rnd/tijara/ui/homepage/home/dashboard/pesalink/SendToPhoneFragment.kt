package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.PesaLinkBankAdapter
import com.ekenya.rnd.tijara.databinding.FragmentSendToPhoneBinding
import com.ekenya.rnd.tijara.requestDTO.STPhoneDTO
import com.ekenya.rnd.tijara.utils.*
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.network.model.BankList
import com.ekenya.rnd.tijara.network.model.PesalinkBank
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.PesalinkPhoneCheckDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SendToPhoneFragment : BaseDaggerFragment() {
    private lateinit var binding: FragmentSendToPhoneBinding
    lateinit var cardBinding: DepositDialogLayoutBinding
    private var phone=""
    private var name=""
    private var bankName=""
    private var bankCode=""
    private var totalAmount=""
    private var accountName=""
    private var accountId=-1
    private var accountNumber=""
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewmodel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SendToPhoneViewmodel::class.java)
    }
    private val pesalinkPhoneCheckViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PesalinkPhoneCheckViewModel::class.java)
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
        binding= FragmentSendToPhoneBinding.inflate(layoutInflater)
        setupNavUp()
        /*binding.tvChoose.paint?.isUnderlineText = true
        binding.tvChoose.setOnClickListener {
            checkPermission()
        }*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG","ONVIEW CREATED")
        viewmodel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardBinding= DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            tvHeading.text=getString(R.string.confirm_pesalink_to_phone)
                            tvName.text=getString(R.string.recipient_name)
                            tvNameValue.text=name
                            tvBankValue.text=bankName
                            tvBank.text=getString(R.string.recipient_bank)
                            tvACNO.text=getString(R.string.recipient_phone)
                            tvACNOValue.text=phone
                            val amount=FormatDigit.formatDigits(totalAmount)
                            tvAmountValue.text= String.format(getString(R.string.kesh),amount)
                            val accNumber=accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                            tvFromValur.text= "$accountName - A/C $accNumber"
                            tvDuty.text=getString(R.string.charges)
                            viewmodel.charges.observe(viewLifecycleOwner) { charges ->
                                tvDutyValue.text =
                                    String.format(getString(R.string.charges), charges)
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


                    }
                    0 -> {
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewmodel.statusMessage.value)
                        viewmodel.stopObserving()
                    }
                    else -> {
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
                binding.progressr.tv_pbTex.text = getString(R.string.we_are_processing_requesrt)
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
                        with(binding.content) {
                            etAmount.setText("")
                            etName.setText("")
                            etReason.setText("")
                            etPhone.setText("")
                        }
                        val directions=SendToPhoneFragmentDirections.actionSendToPhoneFragmentToSendMoneySuccessFragment(fragmentType = 0)
                        findNavController().navigate(directions)
                        viewmodel.stopObserving()

                    }
                    0 -> {
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewmodel.statusCMessage.value)
                        viewmodel.stopObserving()
                    }
                    else -> {
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewmodel.stopObserving()

                    }
                }
            }
        })

        /**spinner impl*/
        binding.progressr.visibility=View.VISIBLE
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_accounts)
        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        /**spinner impl*/
        viewmodel.savingAccountProperties.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                populateAccount(it)
            }else{
                toastyInfos("No saving account found")
            }
        })
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
            if (null != it) {
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
        viewmodel.pesalinkBankProperties.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                populateBanks(it)
            }else{
                toastyInfos("No bank accounts found")
            }
        })
        pesalinkPhoneCheckViewModel.phone.observe(viewLifecycleOwner) {
            phone = it
            binding.content.etPhone.setText(it)
            val pesalinkPhoneCheckDTO = PesalinkPhoneCheckDTO()
            pesalinkPhoneCheckDTO.phoneNumber = it
            viewmodel.loadPesalinkBankName(pesalinkPhoneCheckDTO)
        }
        pesalinkPhoneCheckViewModel.name.observe(viewLifecycleOwner) {
            name = it!!
            binding.content.etName.setText(it)
        }


        binding.content.btnSubmit.setOnClickListener {
            with(binding.content) {

                val amount=etAmount.text.toString()
                val name =etName.text.toString()
                val narration =etReason.text.toString()
                val validMsg = FieldValidators.VALIDINPUT
                val phoneNumber = FieldValidators().formatPhoneNumber(etPhone.text.toString())
                val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                if (amount.isEmpty()){
                    tlEnterAmount.error=getString(R.string.required)
                }
                else if (!validPhone.contentEquals(validMsg)) {
                    etPhone.requestFocus()
                    tlEnterAmount.error
                    tlPhone.isErrorEnabled = true
                    tlPhone.error = validPhone
                }else if (bankName.isEmpty() ) {
                    tlPhone.error=""
                    toastyInfos("Select Bank")
                }else if (name.isEmpty()){
                    tlName.error=getString(R.string.required)
                }else if (accountName.isEmpty() ) {
                    toastyInfos("Select Account")
                    tlName.error=""
                }else {
                    tlReason.error=""
                    val stPhoneDTO= STPhoneDTO()
                    stPhoneDTO.transferType="A2P"
                    stPhoneDTO.amount=amount
                    stPhoneDTO.destinationPhoneNumber=phoneNumber
                    stPhoneDTO.destinationBankCode=bankCode
                    stPhoneDTO.recipientName=name
                    stPhoneDTO.accountId= accountId
                    stPhoneDTO.narration=narration
                    stPhoneDTO.bankName=bankName
                    viewmodel.setAmount(stPhoneDTO.amount)
                    totalAmount=stPhoneDTO.amount
                    binding.progressr.visibility=View.VISIBLE
                    binding.progressr.tv_pbTitle.visibility=View.GONE
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    viewmodel.sendMoneyToPhone(stPhoneDTO)

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
    private fun populateBanks(bankList: List<PesalinkBank>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, bankList)
        with( binding.content){
            bankSpinner.setAdapter(typeAdapter)
            bankSpinner.keyListener = null
            bankSpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: PesalinkBank =
                    parent.adapter.getItem(position) as PesalinkBank
                bankName = selected.bANKNAME
                bankCode = selected.sORTCODE
                viewmodel.setBankName(selected.bANKNAME)
            }
        }
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.pesalink_to_phone)
    }
}