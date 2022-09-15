package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney.internaltransfer

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.InternalTransferFragmentBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.InternalTDTO
import com.ekenya.rnd.tijara.requestDTO.MemberTDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class InternalTransferFragment : BaseDaggerFragment() {
    private lateinit var binding:InternalTransferFragmentBinding
    private lateinit var cardBinding: DepositDialogLayoutBinding
    private var totalAmount=""
    private var accountName=""
    private var accId=-1
    private var accountNumber=""
    private var accountNameTo=""
    private var accIdTo=-1
    private var accountNumberTo=""
    private lateinit var viewModel: SelfTransferViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private var isCardSelected = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= InternalTransferFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(SelfTransferViewModel::class.java)
        binding.progressr.visibility=View.VISIBLE
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_accounts)
        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        viewModel.loadingAccount.observe(viewLifecycleOwner) {
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

        viewModel.accountCode.observe(viewLifecycleOwner) {
            if (null != it) {
                when (it) {
                    1 -> {
                        requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                        viewModel.stopObserving()
                    }
                    0 -> {
                        waringAlertDialogUp(
                            requireContext(),
                            requireView(),
                            getString(R.string.oops_we_are_sorry),
                            getString(R.string.unable_to_complete_your_request)
                        )
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.progressr.visibility = View.GONE
                        viewModel.stopObserving()
                    }
                }
            }
        }
        with(binding.content) {
            if (isCardSelected==1){
                isCardSelected=1
                rbMyself.isChecked=true
                rbOthers.isChecked=false
                CLSELFTRANSFER.makeVisible()
                CLMEMBERTRANSFER.makeGone()
            }else{
                isCardSelected=2
                rbMyself.isChecked=false
                rbOthers.isChecked
                CLMEMBERTRANSFER.makeVisible()
                CLSELFTRANSFER.makeGone()

            }
            rbMyself.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    isCardSelected=1
                    rbMyself.isChecked=true
                    rbOthers.isChecked=false
                    CLSELFTRANSFER.makeVisible()
                    CLMEMBERTRANSFER.makeGone()
                }

            }
            rbOthers.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    isCardSelected=2
                    rbMyself.isChecked=false
                    rbOthers.isChecked
                    CLMEMBERTRANSFER.makeVisible()
                    CLSELFTRANSFER.makeGone()

                }

            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        /**spinner  From impl*/
        viewModel.savingPrimeProperties.observe(viewLifecycleOwner) {
            if (it != null) {
                populateMemberPrimeAccount(it)
                populateSelfPrimeAccount(it)
            } else {
                toastyInfos("No saving account found")
            }
        }
        /**spinner TO impl*/
        viewModel.savingAlphaProperties.observe(viewLifecycleOwner) {
            if (it != null) {
                populateAlphaAccount(it)
            } else {
                toastyInfos("No saving account found")
            }
        }

        binding.content.btnTransfer.setOnClickListener {
            with(binding.content) {
                when(isCardSelected){
                    /**self transfer*/
                    1->{
                        val amount=etAmount.text.toString()
                        if (amount.isEmpty()){
                            tlEnterAmount.error=getString(R.string.amount_cannot_be_empty)
                        }else if (accountName.isEmpty() ) {
                            toastyInfos("Select Account")
                            tlEnterAmount.error=""
                        }else if (accountNameTo.isEmpty() ) {
                            toastyInfos("Select Account")
                            tlEnterAmount.error=""
                        }else {
                            binding.content.btnTransfer.isEnabled=false
                            tlEnterAmount.error=""
                            val internalTDTO= InternalTDTO()
                            internalTDTO.amount=amount
                            internalTDTO.fromAccountId=accId
                            internalTDTO.toAccountId=accIdTo
                            internalTDTO.transferType="SELF"
                            totalAmount=internalTDTO.amount
                            viewModel.setAmount(internalTDTO.amount)
                            binding.progressr.visibility=View.VISIBLE
                            binding.progressr.tv_pbTitle.visibility=View.GONE
                            binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                            viewModel.selfTransferPreview(internalTDTO)
                        }
                    }
                    /**member transfer*/
                    2->{
                        val amount=etAmount.text.toString()
                        val accountNo=etAccNumber.text.toString()
                        if (amount.isEmpty()){
                            tlEnterAmount.error=getString(R.string.amount_cannot_be_empty)
                        } else if (accountNo.isEmpty()){
                            tlEnterAmount.error=""
                            tlAcNumber.error=getString(R.string.please_provide_the_account_number)
                        }else if (accountName.isEmpty() ) {
                            toastyInfos("Select Account")
                            tlAcNumber.error=""
                        }else {
                            binding.content.btnTransfer.isEnabled=false
                            tlEnterAmount.error=""
                            tlAcNumber.error=""
                            val memberTDTO= MemberTDTO()
                            memberTDTO.amount=amount
                            memberTDTO.accountNumber=accountNo
                            memberTDTO.fromAccountId=accId
                            memberTDTO.transferType="OTHER"
                            totalAmount=memberTDTO.amount
                            viewModel.setAmount(memberTDTO.amount)
                            binding.progressr.visibility=View.VISIBLE
                            binding.progressr.tv_pbTitle.visibility=View.GONE
                            binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                            viewModel.memberTransferPreview(memberTDTO)
                        }
                    }
                }
            }
        }
        viewModel.statusCode.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.progressr.visibility = View.GONE
                viewModel.stopObserving()
                when (it) {

                    1 -> {
                        binding.content.btnTransfer.isEnabled=false
                        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
                        cardBinding =
                            DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        when (isCardSelected) {
                            1 -> {
                                cardBinding.apply {
                                    tvHeading.text = getString(R.string.confirm_self_transfer)
                                    tvName.text = getString(R.string.amoun)
                                    val finalAmount = FormatDigit.formatDigits(totalAmount)
                                    tvNameValue.text =
                                        String.format(getString(R.string.kesh), finalAmount)
                                    tvBank.text = getString(R.string.transfer_from)
                                    val fromId =
                                        accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                                    tvBankValue.text = "$accountName - A/C $fromId"
                                    tvACNO.text = getString(R.string.transfer_to)
                                    val fromIdTo =
                                        accountNumberTo.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                                    tvACNOValue.text = "$accountNameTo - A/C $fromIdTo"
                                    tvAmount.text = getString(R.string.charges)
                                    tvFrom.makeGone()
                                    tvDutyValue.makeGone()
                                    tvDuty.makeGone()

                                    viewModel.charges.observe(viewLifecycleOwner) { charges ->
                                        val finalCharges = FormatDigit.formatDigits(charges)
                                        tvAmountValue.text =
                                            String.format(getString(R.string.kesh), finalCharges)
                                    }
                                    tvFromValur.makeGone()
                                }

                            }
                            2 -> {
                                cardBinding.apply {
                                    tvHeading.text = getString(R.string.confirm_member_transfer)
                                    tvName.text = getString(R.string.amoun)
                                    val finalAmount = FormatDigit.formatDigits(totalAmount)
                                    tvNameValue.text =
                                        String.format(getString(R.string.kesh), finalAmount)
                                    tvBank.text = getString(R.string.transfer_from)
                                    val fromIdTo =
                                        accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                                    tvBankValue.text = "$accountName - A/C $fromIdTo"
                                    tvACNO.text = getString(R.string.recipient_acc_no)
                                    viewModel.resAccNo.observe(viewLifecycleOwner) { number ->
                                        val accountNo =
                                            number.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                                        tvACNOValue.text = accountNo
                                    }
                                    tvAmount.text = getString(R.string.recipient_name)
                                    viewModel.recName.observe(viewLifecycleOwner) { namer ->
                                        val name =
                                            namer.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                                        tvAmountValue.text = name
                                    }
                                    tvFrom.text = getString(R.string.charges)
                                    tvDuty.makeGone()

                                    viewModel.charges.observe(viewLifecycleOwner) { charges ->
                                        val finalCharges = FormatDigit.formatDigits(charges)
                                        tvFromValur.text =
                                            String.format(getString(R.string.kesh), finalCharges)
                                    }
                                    tvDutyValue.makeGone()
                                }
                            }

                        }
                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_internalTransferFragment_to_pinFragment)
                            viewModel.stopObserving()
                        }

                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)


                    }
                    0 -> {
                        binding.content.btnTransfer.isEnabled=true
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), viewModel.statusMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.content.btnTransfer.isEnabled=true
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()

                    }
                }
            }
        }
        pinViewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                when (isCardSelected) {
                    1 -> {
                        pinViewModel.unsetAuthSuccess()
                        binding.progressr.visibility = View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility = View.GONE
                        binding.progressr.tv_pbTex.text =
                            getString(R.string.we_are_processing_requesrt)
                        viewModel.commitSelfTransfer()
                        pinViewModel.stopObserving()
                        viewModel.stopObserving()
                    }
                    2 -> {
                        pinViewModel.unsetAuthSuccess()
                        binding.progressr.visibility = View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility = View.GONE
                        binding.progressr.tv_pbTex.text =
                            getString(R.string.we_are_processing_requesrt)
                        viewModel.commitMemberTransfer()
                        pinViewModel.stopObserving()
                        viewModel.stopObserving()
                    }
                }


            }
        }
        viewModel.statusCommit.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.progressr.visibility = View.GONE
                when (it) {

                    1 -> {
                        binding.content.btnTransfer.isEnabled=true
                        when (isCardSelected) {
                            1 -> {

                                binding.content.etAmount.setText("")
                                binding.progressr.visibility = View.GONE
                                val directions =
                                    InternalTransferFragmentDirections.actionInternalTransferFragmentToSendMoneySuccessFragment(
                                        fragmentType = 2
                                    )
                                findNavController().navigate(directions)
                                viewModel.stopObserving()
                            }
                            2 -> {
                                with(binding.content) {
                                    etAmount.setText("")
                                    etAccNumber.setText("")
                                }
                                binding.progressr.visibility = View.GONE
                                val directions =
                                    InternalTransferFragmentDirections.actionInternalTransferFragmentToSendMoneySuccessFragment(
                                        fragmentType = 3
                                    )
                                findNavController().navigate(directions)
                                viewModel.stopObserving()
                            }
                        }


                    }
                    0 -> {
                        binding.content.btnTransfer.isEnabled=true
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), viewModel.statusCMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        binding.content.btnTransfer.isEnabled=true
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()

                    }
                }
            }
        }
    }
    private fun populateSelfPrimeAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        with(binding.content) {
            spFrom.setAdapter(typeAdapter)
            spFrom.keyListener = null
            spFrom.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                accountName = selected.accountName
                accId = selected.accountId
                accountNumber = selected.accountNo
                viewModel.setAccNumber(selected.accountNo)
                viewModel.setAccountName(selected.accountName)
            }
        }
    }
    private fun populateMemberPrimeAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        with(binding.content) {
            spinnerTFrom.setAdapter(typeAdapter)
            spinnerTFrom.keyListener = null
            spinnerTFrom.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                accountName = selected.accountName
                accId = selected.accountId
                accountNumber = selected.accountNo
                viewModel.setAccNumber(selected.accountNo)
                viewModel.setAccountName(selected.accountName)
            }
        }
    }
    private fun populateAlphaAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        with(binding.content) {
            spTo.setAdapter(typeAdapter)
            spTo.keyListener = null
            spTo.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                accountNameTo = selected.accountName
                accIdTo = selected.accountId
                accountNumberTo = selected.accountNo
                viewModel.setAccNumberTo(selected.accountNo)
                viewModel.setAccountNameTo(selected.accountName)
            }
        }
    }


    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = "Internal Transfer"
    }

}
