package com.ekenya.rnd.dashboard.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.data.model.*
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.adapters.PaymentOptionsAdapter
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.model.BillItem
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel

import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentTrafficFinesBinding
import com.ekenya.rnd.onboarding.databinding.PaymentsModeBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ekenya.rnd.dashboard.adapters.PaymentOptionsAdapter.MyClickListener
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_traffic_fines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrafficFinesFragment : Fragment() {

    private lateinit var  binding:FragmentTrafficFinesBinding
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var viewModel: LoginViewModel
    private var region = ""
    private var lincenseGrade = ""
    private var phoneNumber = ""
    private var offenseID = ""
    private var amount = ""



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initUI()
        setupVModel()
        setonClickListeners()

        return binding.root
    }

    private fun setupVModel() {

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.SEND_TO_WALLET)

        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)

    }

    private fun setonClickListeners() {

        binding.btnContinue.setOnClickListener {

//            authorizeTransactionViewModel.setRequestingFragment(Constants.PAY_TRAFFIC_FINE)
//            confirmSendingMoneyViewModel.setRequestingFragment(Constants.PAY_TRAFFIC_FINE)
//            showpaymentOptionsDialog(requireContext())
//            // Inflate the layout for this fragment
//            binding = FragmentTrafficFinesBinding.inflate(layoutInflater)
//            initUI()
//            initViewModel()

              if(binding.btnContinue.text.equals("SUBMIT"))
              {
                  getChargesLookup()
              }
              else
              {
                  if(region.isEmpty() || lincenseGrade.isEmpty() || binding.etLincencseAccno.text.toString().isEmpty())
                  {
                      toastMessage("All fields are mandatory please fill them")

                  }
                  else
                  {
                      doAccountLookUP()

                  }

              }
        }

    }


    private fun initViewModel() {
        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.DSTV_PAYMENTS_FRAGMENT)

    }

    private fun showHiddenLayoutsAfterLookup(isVisible:Boolean)
    {
        if(isVisible)
        {
            binding.btnContinue.text = "SUBMIT"
            binding.tilDriverName.visibility = View.VISIBLE
            binding.tilTicketNumber.visibility = View.VISIBLE
            binding.tilDriverID.visibility = View.VISIBLE
            binding.tilPlateRegion.visibility = View.VISIBLE
            binding.tilPlateNumber.visibility = View.VISIBLE
            binding.tilPlateCode.visibility = View.VISIBLE
            binding.tilDateAccused.visibility = View.VISIBLE
            binding.tilViolationDate.visibility = View.VISIBLE
            binding.tlAutoCmpleteOffenceID.visibility = View.VISIBLE
            binding.lAutoCmpleteViolationGrade.visibility = View.VISIBLE

//            binding.tilPenaltycode.editText!!.setText("1292919192")
//            binding.tilInvoiceNumber.editText!!.setText("1292919192")
        }
        else
        {
            binding.btnContinue.text = "REGISTER"
            binding.tilTicketNumber.visibility = View.GONE
            binding.tilDriverName.visibility = View.GONE
            binding.tilDriverID.visibility = View.GONE
            binding.tilPlateRegion.visibility = View.GONE
            binding.tilPlateNumber.visibility = View.GONE
            binding.tilPlateCode.visibility = View.GONE
            binding.tilDateAccused.visibility = View.GONE
            binding.tilViolationDate.visibility = View.GONE
            binding.tlAutoCmpleteOffenceID.visibility = View.GONE
            binding.lAutoCmpleteViolationGrade.visibility = View.GONE


//            binding.tilPenaltycode.editText!!.setText("")
//            binding.tilInvoiceNumber.editText!!.setText("")
        }
    }

    private fun doAccountLookUP()
    {

         val reqData = TrafficFinesLookupReq(
             "lookup",
             region,
             lincenseGrade,
             binding.etLincencseAccno.text.toString(),
             phoneNumber
        )
        binding.progressBar.visibility = View.VISIBLE
        viewModel.doTrafficFinesLookUP(reqData).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        when (it.data!!.status) {

                            0 -> {
                                binding.progressBar.visibility = View.INVISIBLE
                                showHiddenLayoutsAfterLookup(true)

                                val driverName = it.data!!.data.driverName
                                //val licenseGrade = it.data!!.data.licenseGrade
                                //val licenseNumber = it.data!!.data.licenseNumber
                                val driverID = it.data!!.data.driverID
                                val region = it.data!!.data.region.toString()

                                val sdf = SimpleDateFormat("dd/M/yyyy")
                                val currentDate = sdf.format(Date())


                                binding.tilTicketNumber.visibility = View.VISIBLE
                                binding.tilDriverName.editText!!.setText(driverName)
                                binding.tilDriverID.editText!!.setText(driverID)
                                binding.tilPlateRegion.editText!!.setText(region)
                                binding.tilPlateNumber.visibility = View.VISIBLE
                                binding.tilPlateCode.visibility = View.VISIBLE
                                binding.tilDateAccused.visibility = View.VISIBLE
                                binding.tilDateAccused.editText!!.setText(currentDate)
                                binding.tilViolationDate.visibility = View.VISIBLE
                                binding.tilViolationDate.editText!!.setText(currentDate)

                                binding.tlAutoCmpleteOffenceID.visibility = View.VISIBLE
                                binding.lAutoCmpleteViolationGrade.visibility = View.VISIBLE
                                binding.lAutoCmpleteViolationGrade.visibility = View.VISIBLE

                            }
                            1 -> {
                                showHiddenLayoutsAfterLookup(false)

                                //findNavController().navigate(R.id.action_accountLookUpFragment_to_accountLookupotpfragment)
                                /*SharedPreferencesManager.setOtpToken(
                                requireContext(),
                                it.data!!.data.toString()
                            )*/
                            }
                            else -> {
                                showHiddenLayoutsAfterLookup(false)
                                binding.progressBar.visibility = View.INVISIBLE
                                toastMessage(it.data!!.message)
                            }
                        }

                    }
                    Status.ERROR ->
                    {
                        showHiddenLayoutsAfterLookup(false)
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }

                    Status.LOADING ->
                    {
                        showHiddenLayoutsAfterLookup(false)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun getChargesLookup()
    {
        val reqData = TrafficFinesChargesLookupReq(
            "fine_lookup",
            binding.tilSelectRegion.editText!!.text.toString(),
            lincenseGrade,
            binding.tilLincenseNo.editText!!.text.toString(),
            "phoneno",
            offenseID
        )

        viewModel.doTrafficFinesChargesLookUP(reqData).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        when (it.data!!.status) {
                            0 -> {

                           amount = it.data!!.data.amount.toString()
                           val licenseGrade = it.data!!.data.licenseGrade
                           val licenseNumber = it.data!!.data.licenseNumber
                           val offenseID = it.data!!.data.offenseID
                           val receiptReference = it.data!!.data.receipt_reference
                           val region = it.data!!.data.region

                            val args = Bundle()
                            args.putString("amount", amount)
                            args.putString("driverName",et_driverName.text.toString())
                            args.putString("licenseGrade", licenseGrade)
                            args.putString("licenseNumber",licenseNumber)
                            args.putString("offenseID",offenseID)
                            args.putString("receiptReference",receiptReference)
                            args.putString("region",region)

                            val accountLookupDialog = AccountLookupDialog()
                            accountLookupDialog.setArguments(args)
                            accountLookupDialog.setProcessPaymentListener { showpaymentOptionsDialog(requireContext()) }
                            accountLookupDialog.show(
                                (context as Activity).fragmentManager,
                                "AccountLookupDialog"
                            )

                                binding.progressBar.visibility = View.INVISIBLE
                             }
                            1 -> {
                                //findNavController().navigate(R.id.action_accountLookUpFragment_to_accountLookupotpfragment)
                                /*SharedPreferencesManager.setOtpToken(
                                requireContext(),
                                it.data!!.data.toString()
                            )*/
                            }
                            else -> {
                                toastMessage(it.data!!.message)
                                binding.progressBar.visibility = View.INVISIBLE

                            }
                        }

                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun submitTransaction()
    {
        val reqData = SubmitTrafficFineReq(
            "fine_lookup",
            binding.tilSelectRegion.editText!!.text.toString(),
            lincenseGrade,
            binding.tilLincenseNo.editText!!.text.toString(),
            phoneNumber,
            offenseID,
            amount
        )

        viewModel.doSubmitTrafficFine(reqData).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        when (it.data!!.status) {
                            0 -> {

                                showSuccessDialog()
                                binding.progressBar.visibility = View.INVISIBLE
                            }
                            1 -> {

                            }
                            else ->
                            {
                                toastMessage(it.data!!.message)
                                binding.progressBar.visibility = View.INVISIBLE

                            }
                        }

                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private val clickItem: MyClickListener = object : MyClickListener {
        override fun onItemClick(position: Int) {
            submitTransaction()
        }
    }

    fun showpaymentOptionsDialog(context: Context) {

        val bottomsheetlayoutBinding = PaymentsModeBottomsheetBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        dialog.setContentView(bottomsheetlayoutBinding.root)
        val paymentOptionsAdapter =
            PaymentOptionsAdapter(context, parentFragment, dialog, clickItem)
        val paymentRecyclerView = bottomsheetlayoutBinding.rvPaymentOptions
        paymentRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        paymentOptionsAdapter.setDashBoardItems(getPaymentOptions())
        bottomsheetlayoutBinding.tvSelectSourceofFunds.text = getString(R.string.pay_from)

        dialog.show()
        paymentRecyclerView.adapter = paymentOptionsAdapter

    }

    fun getPaymentOptions(): List<BillItem> {

        val itemlist = ArrayList<BillItem>()

        itemlist.add(
            BillItem(
                R.drawable.ic_towallet,
                "Wallet"
            )
        )

        return itemlist
    }

    private fun initUI() {
        phoneNumber = SharedPreferencesManager.getPhoneNumber(requireContext())!!
        binding = FragmentTrafficFinesBinding.inflate(layoutInflater)

        val lincenseGradeList = arrayOf("Motor","Auto","Taxi 1","Taxi 2","Public 1","Public 2","Cargo 1","Cargo 2",
        "Cargo 3","Liquid 1","Liquid 2","Special 1","Special 2","Special 3","01","02","03","04","05","06","07")
        val lincenseGradeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,lincenseGradeList)
        binding.autoCompleteLincenceGrade.setAdapter(lincenseGradeAdapter)
        binding.autoCompleteLincenceGrade.setAdapter(lincenseGradeAdapter)

        binding.autoCompleteLincenceGrade.setOnItemClickListener { _, _, position, _ ->
            val value = lincenseGradeAdapter.getItem(position) ?:""
            lincenseGrade = value
        }

        val regionList = arrayOf("Addis abeba","Tigray", "Afar", "Amhara", "Oromia","Gambela","Somalia","Harar","Dire Dawa","Federal")
        val regionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,regionList)
        binding.autoCmpleteRegion.setAdapter(regionAdapter)

        binding.autoCmpleteRegion.setOnItemClickListener { _, _, position, _ ->
            val value = regionAdapter.getItem(position) ?: ""
            region = value
        }


        val offenceIDList = arrayOf("Overspeeding", "Drunk Driving", "Overtaking", "Expired lincense")
        val offenceIDAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,offenceIDList)
        binding.autoCompleteOffenceID.setAdapter(offenceIDAdapter)

        binding.autoCompleteOffenceID.setOnItemClickListener { _, _, position, _ ->
            val value = offenceIDAdapter.getItem(position) ?: ""
            offenseID = position.toString()
        }

        val violationGradeList = arrayOf("First offender", "Second offender", "Third offender", "Fourth offender","Fifth offender")
        val violationGradeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,violationGradeList)
        binding.autoCompleteVilationGrade.setAdapter(violationGradeAdapter)

        binding.autoCompleteVilationGrade.setOnItemClickListener { _, _, pos, _ ->
            val value = violationGradeAdapter.getItem(pos) ?: ""
           // region = value
            offenseID = pos.toString()
        }


    }
}