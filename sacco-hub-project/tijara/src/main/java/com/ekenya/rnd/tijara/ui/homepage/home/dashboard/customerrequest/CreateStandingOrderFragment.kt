package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.FrequenyAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.StandingOrderSpAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentCreateStandingOrderBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.StandingOrderFrequenyData
import com.ekenya.rnd.tijara.requestDTO.BasicInfoDTO
import com.ekenya.rnd.tijara.requestDTO.StandingOrderDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.DashboardViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.MakeDepositFragmentDirections
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CreateStandingOrderFragment : BaseDaggerFragment() {
  private lateinit var binding:FragmentCreateStandingOrderBinding
  private lateinit var viewModel: StandindOrderViewModel
  private lateinit var cardBinding: DepositDialogLayoutBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private val dashboardModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)
    }
    private  var amountTotal=""
    private var payFromName=""
    private var payFromNo=""
    private var frequencyId=""
    private  var frequencyName=""
    private var standingOrderName=""
    private var standingOrderN0=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentCreateStandingOrderBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(StandindOrderViewModel::class.java)
        setupNavUp()
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
                        binding.progressr.makeGone()
                    }
                    0 -> {
                        waringAlertDialogUp(
                            requireContext(),
                            requireView(),
                            getString(R.string.oops_we_are_sorry),
                            getString(R.string.unable_to_complete_your_request)
                        )
                        binding.progressr.makeGone()                    }
                    else -> {
                        binding.progressr.makeGone()
                        viewModel.stopObserving()
                    }
                }
            }
        }
        binding.apply {
            etStartDate.setOnClickListener { pickStartDate() }
            etEndDate.setOnClickListener { pickEndDob() }
        }
        viewModel.savingPrimeProperties.observe(viewLifecycleOwner) {
            if (it != null) {
                populatePrimeAccount(it)
            } else {
                toastyInfos("No saving account found")
            }
        }
        viewModel.frequency.observe(viewLifecycleOwner) {
            if (it != null) {
                populateFrequency(it)
            } else {
                toastyInfos("No saving account found")
            }
        }
        viewModel.savingAlphaProperties.observe(viewLifecycleOwner) {
            if (it != null) {
                populateStandingOrderAcc(it)
            } else {
                toastyInfos("No saving account found")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            binding.btnSubmit.setOnClickListener {
                val amount=etAmount.text.toString().trim()
                val reason=etReason.text.toString().trim()
                val startDate=etStartDate.text.toString().trim()
                val endDate=etEndDate.text.toString().trim()
                if (standingOrderName.isEmpty()){
                    tiaccountSpinner.error= getString(R.string.required)
                }else if (amount.isEmpty()){
                    tiaccountSpinner.error=""
                    tlEnterAmount.error=getString(R.string.required)
                }else if (payFromName.isEmpty()){
                    tlEnterAmount.error=""
                    tispPayFrom.error= getString(R.string.required)
                }else if (startDate.isEmpty()){
                    tispPayFrom.error=""
                    tlStartDate.error = getString(R.string.required)
                }else if(endDate.isEmpty()){
                tlStartDate.error=""
                tlEndDate.error = getString(R.string.required)
            }else if(frequencyName.isEmpty()){
                tlEndDate.error=""
                tiFreQuency.error= getString(R.string.required)
            }else if( reason.isEmpty()) {
                tlReason.error=getString(R.string.required)
            }else{
                    tiaccountSpinner.error=""
                    tispPayFrom.error=""
                    tiFreQuency.error=""
                    tlEnterAmount.error=""
                    tlStartDate.error=""
                    tlEndDate.error=""
                    tlReason.error=""
                    val standingOrderDTO=StandingOrderDTO()
                    standingOrderDTO.toAccountNo=standingOrderN0
                    standingOrderDTO.amount=amount
                    amountTotal=amount
                    viewModel.setAmount(amount)
                    standingOrderDTO.fromAccountNo=payFromNo
                    standingOrderDTO.startDate=startDate
                    standingOrderDTO.endDate=endDate
                    standingOrderDTO.frequency=frequencyId
                    standingOrderDTO.purpose=reason
                    binding.progressr.visibility=View.VISIBLE
                    binding.progressr.tv_pbTitle.visibility=View.GONE
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    binding.btnSubmit.isEnabled=false
                    viewModel.createStandingOrder(standingOrderDTO)
            }

            }
        }
        viewModel.statusCode.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.btnSubmit.isEnabled = true
                binding.progressr.makeGone()
                viewModel.stopObserving()
                when (it) {
                    1 -> {
                        binding.progressr.makeGone()
                        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
                        cardBinding =
                            DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            tvHeading.text = getString(R.string.confirm_create_standing)
                            tvName.text = getString(R.string.standing_order_account)
                            tvNameValue.text = standingOrderName
                            val amount = FormatDigit.formatDigits(amountTotal)
                            val charges = FormatDigit.formatDigits(Constants.CHARGES.toString())
                            tvACNOValue.text = String.format(getString(R.string.kesh), amount)
                            tvAmountValue.text = String.format(getString(R.string.kesh), charges)
                            tvFromValur.text = frequencyName
                            tvBank.text = getString(R.string.pay_from)
                            val accNumber = payFromNo.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                            tvBankValue.text = "${payFromName} - A/C $accNumber"
                            tvACNO.text = getString(R.string.amoun)
                            tvAmount.text = getString(R.string.charges)
                            tvFrom.text = getString(R.string.frequency)
                            tvDuty.makeGone()
                            tvDutyValue.makeGone()

                        }

                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                            viewModel.stopObserving()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_createStandingOrderFragment_to_pinFragment)
                            viewModel.stopObserving()

                        }

                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)

                    }
                    0 -> {
                        viewModel.stopObserving()
                        binding.btnSubmit.isEnabled = true
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(), viewModel.statusMessage.value)
                    }

                    else -> {
                        viewModel.stopObserving()
                        binding.btnSubmit.isEnabled = true
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                    }
                }
            }
        }
        pinViewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                pinViewModel.unsetAuthSuccess()
                binding.progressr.visibility = View.VISIBLE
                binding.progressr.tv_pbTitle.visibility = View.GONE
                binding.progressr.tv_pbTex.text = getString(R.string.we_are_processing_requesrt)
                viewModel.commitStandingOrder()
                pinViewModel.stopObserving()
                viewModel.stopObserving()

            }
        }
        //observe commit
        viewModel.statusCommit.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.progressr.visibility = View.GONE
                when (it) {

                    1 -> {

                        binding.apply {
                            etAmount.setText("")
                            etStartDate.setText("")
                            etEndDate.setText("")
                            etReason.setText("")
                        }
                        binding.progressr.visibility = View.GONE
                        val directions =
                            CreateStandingOrderFragmentDirections.actionCreateStandingOrderFragmentToStandingOrderSuccessFragment(
                                fragmentType = 1
                            )
                        findNavController().navigate(directions)
                        viewModel.stopObserving()
                        dashboardModel.setRefresh(true)
                    }
                    0 -> {
                        viewModel.stopObserving()
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), viewModel.statusCMessage.value)
                    }
                    else -> {
                        viewModel.stopObserving()
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))

                    }
                }
            }
        }
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = "Create Standing Order"
    }
    private fun pickStartDate() {
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
                val preferredFormat = "dd-MM-yyyy"
                val date =
                    SimpleDateFormat(preferredFormat, Locale.US).format(myCalendar.time)
                binding.etStartDate.setText(date)
            }
        DatePickerDialog(
            requireContext(), dateListener, myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
    private fun pickEndDob() {
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
                val preferredFormat = "dd-MM-yyyy"
                val date =
                    SimpleDateFormat(preferredFormat, Locale.US).format(myCalendar.time)
                binding.etEndDate.setText(date)
            }
        DatePickerDialog(
            requireContext(), dateListener, myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
    private fun populatePrimeAccount(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        binding.apply {
            spPayFrom.setAdapter(typeAdapter)
            spPayFrom.keyListener = null
            spPayFrom.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                payFromName = selected.accountName
                payFromNo = selected.accountNo
                viewModel.setPayFromId(selected.accountNo)
                viewModel.setPayFromName(selected.accountName)
            }
        }
    }
    private fun populateStandingOrderAcc(accList: List<SavingAccountData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
      binding.apply {
          accountSpinner.setAdapter(typeAdapter)
          accountSpinner.keyListener = null
          accountSpinner.setOnItemClickListener { parent, _, position, _ ->
                val selected: SavingAccountData =
                    parent.adapter.getItem(position) as SavingAccountData
                standingOrderName = selected.accountName
                standingOrderN0 = selected.accountNo
                viewModel.setAccNumberStandingOrder(selected.accountNo)
                viewModel.setStandingOrderName(selected.accountName)
            }
        }
    }
    private fun populateFrequency(accList: List<StandingOrderFrequenyData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
      binding.apply {
          spFrequency.setAdapter(typeAdapter)
          spFrequency.keyListener = null
          spFrequency.setOnItemClickListener { parent, _, position, _ ->
                val selected: StandingOrderFrequenyData =
                    parent.adapter.getItem(position) as StandingOrderFrequenyData
                frequencyName = selected.label
                frequencyId = selected.id.toString()
                viewModel.setFrequency(selected.label)
            }
        }
    }
}