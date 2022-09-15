package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import cn.pedant.SweetAlert.SweetAlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.PayBillFragmentBinding
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.PayBillDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class PayBillFragment : BaseDaggerFragment() {
    private var billercode=""
    private var billerName=""
    private var hasPresent=-1
    private var totalAmount=""
    private var memName=""
    private var memACCNO=""
    private var primeAccountName=""
    private var primeAccountNo=""
    private var primeAccountNId=-1
    private lateinit var billBinding: PayBillFragmentBinding
    lateinit var cardBinding: DepositDialogLayoutBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private val billersViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BillersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        billBinding= PayBillFragmentBinding.inflate(layoutInflater)
        billBinding.progressr.visibility=View.VISIBLE
        billBinding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_accounts)
        billBinding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        billersViewModel.loadingAccount.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    billBinding.progressr.makeVisible()
                }
                false -> {
                    billBinding.progressr.visibility = View.GONE
                }
                else -> {
                    billBinding.progressr.makeGone()
                }
            }
        }
        billersViewModel.accountCode.observe(viewLifecycleOwner) {
            if (null != it) {
                billBinding.progressr.visibility = View.GONE
                when (it) {

                    1 -> {
                        billBinding.progressr.visibility = View.GONE
                        billersViewModel.stopObserving()
                    }
                    0 -> {
                        waringAlertDialogUp(
                            requireContext(),
                            requireView(),
                            getString(R.string.oops_we_are_sorry),
                            getString(R.string.unable_to_complete_your_request)
                        )
                        billBinding.progressr.visibility = View.GONE
                        billersViewModel.stopObserving()
                    }
                    else -> {
                        billBinding.progressr.visibility = View.GONE
                    }
                }
            }

        }
        var name1=""
        var posone=""
        var postwo=""
        billersViewModel.billerName.observe(viewLifecycleOwner) { name ->
            billBinding.toolbar.custom_toolbar.custom_toolbar_title.text = name
            billerName = name
        }
        billersViewModel.billerUrl.observe(viewLifecycleOwner) { logo ->
            if (logo.isNullOrEmpty()) {
                billBinding.initials.makeVisible()
                val splited: List<String> = billerName?.split("\\s".toRegex())
                if (splited.count() == 2) {
                    val firstName = splited[0]
                    val name2 = (firstName).toUpperCase(Locale.ENGLISH)
                    val lastName = splited[1]
                    name1 = (lastName).toUpperCase(Locale.ENGLISH)
                    posone = name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = name2[0].toString().toUpperCase(Locale.ENGLISH)
                    billBinding.initials.text = " $postwo $posone"
                } else if (splited.count() === 3) {
                    val firstName = splited[0]
                    val name2 = (firstName).toUpperCase(Locale.ENGLISH)
                    val lastName = splited[1]
                    name1 = (lastName).toUpperCase(Locale.ENGLISH)
                    posone = name1[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = name2[0].toString().toUpperCase(Locale.ENGLISH)
                    billBinding.initials.text = " $postwo $posone"
                } else {
                    val names = billerName
                    posone = names[0].toString().toUpperCase(Locale.ENGLISH)
                    postwo = names[0].toString().toUpperCase(Locale.ENGLISH)
                    billBinding.initials.text = " $postwo $posone"
                }

            } else {
                bindImage(billBinding.ivLogo, logo)
            }

        }

        billersViewModel.hasPresentment.observe(viewLifecycleOwner) { present ->
            hasPresent = present
        }

        billersViewModel.billerCode.observe(viewLifecycleOwner) { code ->
            billercode = code

            with(billBinding.content) {
                /**for prepaid acc no and name autofilled,nhif n prepaid*/
                /**for prepaid acc no and name autofilled,nhif n prepaid*/
                if (billercode.contains(getString(R.string.kplc_pre))
                    || billercode.contains(getString(R.string.nhif)) && hasPresent == 1
                ) {
                    etAcNumber.isFocusable = false
                    etAcName.isFocusable = false
                    billersViewModel.accountNumber.observe(viewLifecycleOwner, { number ->
                        etAcNumber.setText(number)
                    })
                    billersViewModel.accountName.observe(viewLifecycleOwner, { name ->
                        etAcName.setText(name)
                    })
                    /**for post-paid billers*/
                    /**for post-paid billers*/
                } else if (billercode.contains(getString(R.string.kplc_post))
                    || billercode.contains(getString(R.string.dstv))
                    || billercode.contains(getString(R.string.n_water))
                    || billercode.contains(getString(R.string.gotv)) && hasPresent == 1
                ) {
                    etAcNumber.isFocusable = false
                    etAcName.isFocusable = false
                    etAmount.isFocusable = false
                    billersViewModel.accountNumber.observe(viewLifecycleOwner) { number ->
                        etAcNumber.setText(number)
                    }
                    billersViewModel.accountName.observe(viewLifecycleOwner) { name ->
                        etAcName.setText(name)
                    }
                    billersViewModel.amount.observe(viewLifecycleOwner) { amount ->
                        etAmount.setText(amount)
                    }
                    /**billers with no lookups*/
                    /**billers with no lookups*/
                } else if (billercode.contains(getString(R.string.county_payment))
                    && hasPresent == 1
                ) {
                    etAcNumber.isFocusable = false
                    etAcName.isFocusable = false
                    etAmount.isFocusable = false
                    tiType.makeVisible()
                    countySpinner.setText("")
                    etAcNumber.hint = getString(R.string.biller_number)
                    etAcName.hint = getString(R.string.member_name)
                    billersViewModel.countyName.observe(viewLifecycleOwner) { countyName ->
                        countySpinner.setText(countyName)
                    }
                    billersViewModel.accountNumber.observe(viewLifecycleOwner) { number ->
                        etAcNumber.setText(number)
                    }
                    billersViewModel.accountName.observe(viewLifecycleOwner) { name ->
                        etAcName.setText(name)
                    }
                    billersViewModel.amount.observe(viewLifecycleOwner) { amount ->
                        etAmount.setText(amount)
                    }
                    /**billers with no lookups*/
                    /**billers with no lookups*/
                } else {
                    etAcNumber.isFocusable = true
                    etAmount.isFocusable = true
                    etAcName.isFocusable = true
                    etAcNumber.setText("")
                    etAmount.setText("")
                    etAcName.setText("")


                }
            }

        }


        setupNavUp()
        return billBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        billersViewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                billBinding.progressr.visibility=View.GONE
                when (it) {
                    1 -> {
                        billBinding.content.btnSubmit.isEnabled=false
                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardBinding= DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            tvHeading.text=getString(R.string.confirm_bill_payment)
                            tvNameValue.text=memName
                            tvBank.text=getString(R.string.account_number)
                            tvBankValue.text=memACCNO
                            tvACNO.text=getString(R.string.amoun)
                            val finalAmount=FormatDigit.formatDigits(totalAmount)
                            tvACNOValue.text=String.format(getString(R.string.kesh),finalAmount)
                            tvAmount.text=getString(R.string.pay_from)
                            val accNumber=primeAccountNo.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                            tvAmountValue.text= "$primeAccountName - A/C $accNumber"
                            tvFrom.text=getString(R.string.charges)
                            billersViewModel.charges.observe(viewLifecycleOwner) { charges ->
                                val finalCharges = FormatDigit.formatDigits(charges)
                                tvFromValur.text =
                                    String.format(getString(R.string.kesh), finalCharges)
                            }
                            tvDutyValue.makeGone()
                            tvDuty.makeGone()

                        }

                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                            billersViewModel.stopObserving()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            findNavController().navigate(R.id.pinFragment)
                            billersViewModel.stopObserving()
                        }

                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)
                        billersViewModel.stopObserving()


                    }
                    0 -> {
                        billBinding.content.btnSubmit.isEnabled=true
                        billBinding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),billersViewModel.statusMessage.value)
                        billersViewModel.stopObserving()
                    }
                    else -> {
                        billBinding.content.btnSubmit.isEnabled=true
                        billBinding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        billersViewModel.stopObserving()

                    }
                }
            }
        })
        //observe commit
        pinViewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                pinViewModel.unsetAuthSuccess()
                billBinding.progressr.visibility = View.VISIBLE
                billBinding.progressr.tv_pbTitle.visibility = View.GONE
                billBinding.progressr.tv_pbTex.text = getString(R.string.we_are_processing_requesrt)
                billersViewModel.billCommitCommit()
                pinViewModel.stopObserving()
                billersViewModel.stopObserving()

            }
        }
        billersViewModel.status.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                billBinding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        billBinding.content.btnSubmit.isEnabled=true
                        with(billBinding.content) {
                            etAcNumber.setText("")
                            etAmount.setText("")
                            etAcName.setText("")
                        }
                        billBinding.progressr.visibility=View.GONE
                        val directions=PayBillFragmentDirections.actionPayBillFragmentToBillSuccessFragment(fragmentType = 0)
                        findNavController().navigate(directions)
                        billersViewModel.stopObserving()
                    }
                    0 -> {
                        billBinding.content.btnSubmit.isEnabled=true
                        billBinding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),billersViewModel.statusCMessage.value)
                        billersViewModel.stopObserving()
                    }
                    else -> {
                        billBinding.content.btnSubmit.isEnabled=true
                        billBinding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        billersViewModel.stopObserving()

                    }
                }
            }
        })
        /**spinner impl*/
        billersViewModel.savingPrimeProperties.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                populateAccount(it)
            }else{
                toastyInfos("No saving account found")
            }
        })
        billBinding.content.btnSubmit.setOnClickListener {
            with(billBinding.content) {

                val amount=etAmount.text.toString()
                val acNumber=etAcNumber.text.toString().trim()
                val acName=etAcName.text.toString().trim()
                when {
                    amount.isEmpty()->{
                        tlEnterAmount.error=getString(R.string.required)
                    }
                    acNumber.isEmpty() -> {
                        etAcNumber.isFocusable
                        tlEnterAmount.error=""
                        tlAcNo.error=getString(R.string.required)
                    }
                    acName.isEmpty() -> {
                        etAcNumber.clearFocus()
                        tlAcNo.error=""
                        etAcName.isFocusable
                        tiAcName.error=getString(R.string.required)
                    }
                    primeAccountName.isEmpty() -> {
                        toastyInfos("Select Account")
                    }else -> {
                    billBinding.content.btnSubmit.isEnabled=false
                    tiAcName.error=""
                    tlAcNo.error=""
                    tlEnterAmount.error=""
                    val payBillDTO= PayBillDTO()
                    payBillDTO.billerCode= billercode
                    payBillDTO.amount=amount
                    payBillDTO.accountNumber=acNumber
                    payBillDTO.accountName=acName
                    payBillDTO.accountId= primeAccountNId
                    billersViewModel.setAccountName(acName)
                    billersViewModel.setAccNumber(acNumber)
                    totalAmount=amount
                    billersViewModel.setAmount(amount)
                    memACCNO=acNumber
                    memName=acName
                    billBinding.progressr.visibility=View.VISIBLE
                    billBinding.progressr.tv_pbTitle.visibility=View.GONE
                    billBinding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    billersViewModel.payBill(payBillDTO)
                    }
                }
            }
        }

    }
    private fun populateAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        with(billBinding.content) {
            accountSpinner.setAdapter(typeAdapter)
            accountSpinner.keyListener = null
            accountSpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                primeAccountName = selected.accountName
                primeAccountNId = selected.accountId
                primeAccountNo = selected.accountNo
                billersViewModel.setPrimeAccNo(selected.accountNo)
                billersViewModel.setPrimeAccName(selected.accountName)
            }
        }
    }


    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        billBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}