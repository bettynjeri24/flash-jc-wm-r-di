package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
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
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ChequeBranchAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.SavingAccountAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentChequeBinding
import com.ekenya.rnd.tijara.network.model.ChequeData
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.ChequeDTO
import com.ekenya.rnd.tijara.requestDTO.StandingOrderDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.DashboardViewModel
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ChequeFragment : BaseDaggerFragment() {
private lateinit var binding:FragmentChequeBinding
    private lateinit var viewModel: StandindOrderViewModel
    private lateinit var cardBinding: DepositDialogLayoutBinding
    var bankName=""
    var bankId=""
    var payee=""
    var date=""
    var amountTotal=""
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private val dashboardModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChequeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(StandindOrderViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chequeBranch.observe(viewLifecycleOwner) {
            if (it != null) {
                populateBranch(it)
            } else {
                toastyInfos("No pickup branch found")
            }
        }
        binding.etStartDate.setOnClickListener { pickStartDate() }
        setupNavUp()

        binding.apply {
            binding.btnSubmit.setOnClickListener {
                val amount=etAmount.text.toString().trim()
                val payAble=etPayable.text.toString().trim()
                val startDate=etStartDate.text.toString().trim()
                if (payAble.isEmpty()){
                    tlpayableTo.error=getString(R.string.required)
                }else if (amount.isEmpty()){
                    tlEnterAmount.error=getString(R.string.required)
                }else if (bankName.isEmpty()){
                    toastyInfos("select branch")
                }else if (startDate.isEmpty()){
                    tlEnterAmount.error=""
                    tlStartDate.error = getString(R.string.required)
                }else{
                    tlEnterAmount.error=""
                    tlStartDate.error=""
                    tlpayableTo.error=""
                    val chequeDTO=ChequeDTO()
                    chequeDTO.requestType="CHEQUE"
                    chequeDTO.amount=amount
                    chequeDTO.payee=payAble
                    payee=payAble
                    viewModel.setPayee(payee)
                    chequeDTO.branchId=bankId
                    chequeDTO.collectionDate=startDate
                    date=startDate
                    viewModel.setDate(startDate)
                    amountTotal=amount
                    viewModel.setAmount(amount)
                    binding.progressr.visibility=View.VISIBLE
                    binding.progressr.tv_pbTitle.visibility=View.GONE
                    binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    binding.btnSubmit.isEnabled=false
                    viewModel.createChequePreview(chequeDTO)
                }

            }
        }
        viewModel.statusCheque.observe(viewLifecycleOwner) {
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
                            tvHeading.text = "Confirm Creating Cheque"
                            tvName.text = "Payable To:"
                            tvNameValue.text = payee

                            val amount = FormatDigit.formatDigits(amountTotal)
                            tvBank.text = getString(R.string.amoun)
                            tvBankValue.text = String.format(getString(R.string.kesh), amount)
                            tvACNOValue.text = bankName
                            tvACNO.text = "Bank Branch"
                            tvAmount.text = "Collection Date"
                            tvAmountValue.text = date
                            tvFrom.text = getString(R.string.charges)
                            val charges = FormatDigit.formatDigits(Constants.CHARGES.toString())
                            tvFromValur.text = String.format(getString(R.string.kesh), charges)
                            tvDuty.visibility=View.GONE
                            tvDutyValue.visibility=View.GONE
                        }

                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                            viewModel.stopObserving()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_chequeFragment_to_pinFragment)
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

                    /*else -> {
                        viewModel.stopObserving()
                        binding.btnSubmit.isEnabled=true
                        binding.progressr.makeGone()
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                    }*/
                }
            }
        }
        pinViewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                pinViewModel.unsetAuthSuccess()
                binding.progressr.visibility = View.VISIBLE
                binding.progressr.tv_pbTitle.visibility = View.GONE
                binding.progressr.tv_pbTex.text = getString(R.string.we_are_processing_requesrt)
                viewModel.commitCheque()
                pinViewModel.stopObserving()
                viewModel.stopObserving()

            }
        }
        //observe commit
        viewModel.commitCheque.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.progressr.visibility = View.GONE
                when (it) {

                    1 -> {
                        viewModel.stopObserving()
                        binding.apply {
                            etAmount.setText("")
                            etStartDate.setText("")
                            etPayable.setText("")
                        }
                        binding.progressr.visibility = View.GONE
                        val directions =
                            ChequeFragmentDirections.actionChequeFragmentToStandingOrderSuccessFragment(
                                fragmentType = 2
                            )
                        findNavController().navigate(directions)
                        viewModel.stopObserving()
                        dashboardModel.setRefresh(true)
                    }
                    0 -> {
                        viewModel.stopObserving()
                        binding.progressr.visibility = View.GONE
                        onInfoDialog(requireContext(), viewModel.commiitCheMessage.value)
                    }
                    /* else -> {
                         viewModel.stopObserving()
                         binding.progressr.visibility=View.GONE
                         onInfoDialog(requireContext(), getString(R.string.error_occurred))

                     }*/
                }
            }
        }
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = "Cheque Request"
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
    private fun populateBranch(accList: List<ChequeData>) {
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, accList)
        binding.apply {
            spBranch.setAdapter(typeAdapter)
            spBranch.keyListener = null
            spBranch.setOnItemClickListener { parent, _, position, _ ->
                val selected: ChequeData =
                    parent.adapter.getItem(position) as ChequeData
                bankName = selected.name
                bankId = selected.id.toString()
                viewModel.setBankName(selected.name)
            }
        }
    }



}