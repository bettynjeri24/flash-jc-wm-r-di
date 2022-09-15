package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentSharePhoneLookupBinding
import com.ekenya.rnd.tijara.databinding.ShareDialogLayoutBinding
import com.ekenya.rnd.tijara.requestDTO.CommitShareDTO
import com.ekenya.rnd.tijara.requestDTO.ShareDTO
import com.ekenya.rnd.tijara.requestDTO.ShareLookupDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import javax.inject.Inject


class SharePhoneLookupFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private lateinit var plookupBinding:FragmentSharePhoneLookupBinding
    private lateinit var cardShareBinding: ShareDialogLayoutBinding
    private lateinit var viewModel: SharePhoneLookupViewModel
    private var isCardSelected = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharePhoneLookupViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        plookupBinding= FragmentSharePhoneLookupBinding.inflate(layoutInflater)
        setupNavUp()
        plookupBinding.apply {
            when(isCardSelected){
                1->{
                    etNoOfShares.addTextChangedListener(object : TextWatcher {

                        override fun afterTextChanged(s: Editable) {}

                        override fun beforeTextChanged(s: CharSequence, start: Int,
                                                       count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence, start: Int,
                                                   before: Int, count: Int) {
                            val number=Constants.SHAREVALUE
                            tvPKESShare.text = "${number*s.toString().toLong()}.00"
                        }
                    })
                }
                2->{
                    etMnoOfShares.addTextChangedListener(object : TextWatcher {

                        override fun afterTextChanged(s: Editable) {}

                        override fun beforeTextChanged(s: CharSequence, start: Int,
                                                       count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence, start: Int,
                                                   before: Int, count: Int) {
                            val number=Constants.SHAREVALUE
                            tvKESShare.text = "${number*s.toString().toLong()}.00"


                        }
                    })
                }
            }


            btnPhoneLookUP.setOnClickListener {
                when (isCardSelected) {
                    1 -> {
                        isCardSelected = 1
                        Timber.d("CARD  $isCardSelected")
                        plookupBinding.btnContinue.visibility=View.GONE
                        plookupBinding.btnPhoneLookUP.visibility=View.VISIBLE
                        tvDetails.text=getString(R.string.enter_mobile_number_to_continue)
                        rbPhoneN.isChecked = true
                        rbMemberNo.isChecked = false
                        llPhone.visibility = View.VISIBLE
                        tlMemberNo.visibility = View.GONE
                        ClMemnberPhoneNumber.visibility = View.GONE
                        ClMemnberNumber.visibility = View.GONE
                        val validMsg = FieldValidators.VALIDINPUT
                        val phoneNumber = FieldValidators().formatPhoneNumber(etPhone.text.toString())
                        val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                        if (!validPhone.contentEquals(validMsg)){
                            etPhone.requestFocus()
                            tlPhone.isErrorEnabled=true
                            tlPhone.error=validPhone
                        } else {
                            tlPhone.isErrorEnabled=false
                            tlPhone.error=""
                            val shareLookupDTO = ShareLookupDTO()
                            shareLookupDTO.phoneNumber = phoneNumber
                            plookupBinding.progressbar.visibility=View.VISIBLE
                            plookupBinding.progressbar.tv_pbTitle.visibility=View.GONE
                            plookupBinding.progressbar.tv_pbTex.text=getString(R.string.please_wait)
                            btnPhoneLookUP.isEnabled=false
                            viewModel.shareNoCheck(shareLookupDTO)
                         }
                    }
                    2 -> {
                        plookupBinding.btnContinue.visibility=View.GONE
                        plookupBinding.btnPhoneLookUP.visibility=View.VISIBLE
                        tvDetails.text=getString(R.string.enter_memebr_number_to_continue)
                        etMemberNo.isFocusable=true
                        isCardSelected = 2
                        rbPhoneN.isChecked = false
                        rbMemberNo.isChecked
                        llPhone.visibility = View.GONE
                        tlMemberNo.visibility = View.VISIBLE
                        ClMemnberPhoneNumber.visibility = View.GONE
                        ClMemnberNumber.visibility = View.GONE
                        val membNo = etMemberNo.text.toString()
                        if (membNo.isEmpty()) {
                            tlMemberNo.error = "Enter member number"
                        } else {
                            val shareLookupDTO = ShareLookupDTO()
                            shareLookupDTO.memberNumber = membNo
                            plookupBinding.progressbar.visibility=View.VISIBLE
                            plookupBinding.progressbar.tv_pbTitle.visibility=View.GONE
                            plookupBinding.progressbar.tv_pbTex.text=getString(R.string.please_wait)
                            btnPhoneLookUP.isEnabled=false
                            viewModel.shareNoCheck(shareLookupDTO)


                        }
                    }
                }

            }
        }
        return plookupBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plookupBinding.apply {
            btnContinue.setOnClickListener {
              when(isCardSelected){
                  1->{
                      val validMsg = FieldValidators.VALIDINPUT
                      val phoneNumber = FieldValidators().formatPhoneNumber(etPhone.text.toString())
                      val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                      val shareNo=etNoOfShares.text.toString().trim()
                      val memberNo=etMEMNo.text.toString().trim()

                      if (!validPhone.contentEquals(validMsg)){
                          etPhone.requestFocus()
                          tlPhone.isErrorEnabled=true
                          tlPhone.error=validPhone
                      }else if (shareNo.isEmpty()){
                          tlMEMNo.error=""
                          tlNoOfShares.error="Enter number of shares to transfer"
                      }else{
                          tlNoOfShares.error=""
                          val shareDTO=ShareDTO()
                          shareDTO.numberOfShares=shareNo
                          shareDTO.recipientMemberNumber=memberNo
                          plookupBinding.progressbar.visibility=View.VISIBLE
                          plookupBinding.progressbar.tv_pbTitle.visibility=View.GONE
                          plookupBinding.progressbar.tv_pbTex.text=getString(R.string.please_wait)
                          if (Constants.DIALOGSELETED==1){
                              //sell
                              shareDTO.transactionType=3
                          }
                          if (Constants.DIALOGSELETED==2){
                              //transfer-2
                              shareDTO.transactionType=2
                          }
                          viewModel.transactShare(shareDTO)
                      }
                  }
                  2->{
                      val memNumber=etMemberNo.text.toString().trim()
                     val shareNo=etMnoOfShares.text.toString().trim()
                      if (memNumber.isEmpty()){
                          tlMemberNo.error="Enter the recipient member number"
                      }else if (shareNo.isEmpty()){
                          tlMemberNo.error=""
                          tlMNoOfShares.error="Enter number of shares to transfer"
                      }else{
                          tlNoOfShares.error=""
                          val shareDTO=ShareDTO()
                          shareDTO.numberOfShares=shareNo
                          shareDTO.recipientMemberNumber=memNumber
                          plookupBinding.progressbar.visibility=View.VISIBLE
                          plookupBinding.progressbar.tv_pbTitle.visibility=View.GONE
                          plookupBinding.progressbar.tv_pbTex.text=getString(R.string.please_wait)
                          if (Constants.DIALOGSELETED==1){
                              //sell
                              shareDTO.transactionType=3
                          }
                          if (Constants.DIALOGSELETED==2){
                              //transfer-2
                              shareDTO.transactionType=2
                          }
                          viewModel.transactShare(shareDTO)

                      }

                  }
              }
            }


            rbPhoneN.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    isCardSelected = 1
                    plookupBinding.btnContinue.visibility=View.GONE
                    plookupBinding.btnPhoneLookUP.visibility=View.VISIBLE
                    tvDetails.text=getString(R.string.enter_mobile_number_to_continue)
                    Timber.d("CARD  $isCardSelected")
                    rbPhoneN.isChecked = true
                    rbMemberNo.isChecked = false
                    plookupBinding.etMemberNo.isFocusable=true
                    llPhone.visibility=View.VISIBLE
                    tlMemberNo.visibility=View.GONE
                    ClMemnberPhoneNumber.visibility=View.GONE
                    ClMemnberNumber.visibility=View.GONE

                }

            }
            rbMemberNo.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    isCardSelected = 2
                    tvDetails.text=getString(R.string.enter_memebr_number_to_continue)
                    etMemberNo.isFocusable=true
                    plookupBinding.btnContinue.visibility=View.GONE
                    plookupBinding.btnPhoneLookUP.visibility=View.VISIBLE
                    rbPhoneN.isChecked = false
                    rbMemberNo.isChecked
                    llPhone.visibility=View.GONE
                    ClMemnberPhoneNumber.visibility=View.GONE
                    ClMemnberNumber.visibility=View.GONE
                    tlMemberNo.visibility=View.VISIBLE
                    etMnoOfShares.addTextChangedListener(object : TextWatcher {

                        override fun afterTextChanged(s: Editable) {}

                        override fun beforeTextChanged(s: CharSequence, start: Int,
                                                       count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence, start: Int,
                                                   before: Int, count: Int) {
                            val number=Constants.SHAREVALUE
                            tvKESShare.text = "${number*s.toString().toLong()}.00"
                        }
                    })
                }

            }

            if (Constants.DIALOGSELETED==2){
                tvKSha.visibility=View.GONE
                tvKesSha.visibility=View.GONE
                tvValuSha.visibility=View.GONE
                tvValueShare.visibility=View.GONE
                tvShareEquity.visibility=View.GONE
                tvKESShare.visibility=View.GONE
                tvPShareEquity.visibility=View.GONE
                PKES.visibility=View.GONE
                KES.visibility=View.GONE

            }else{

                tvKSha.visibility=View.VISIBLE
                tvKesSha.visibility=View.VISIBLE
                tvValuSha.visibility=View.VISIBLE
                tvValueShare.visibility=View.VISIBLE
                tvPShareEquity.visibility=View.VISIBLE
                PKES.visibility=View.VISIBLE
                KES.visibility=View.VISIBLE
            }
        }
        viewModel.status.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                plookupBinding.progressbar.visibility=View.GONE
                when (it) {

                    1 -> {
                        when(isCardSelected){

                            1->{
                                viewModel.stopObserving()
                                plookupBinding.btnPhoneLookUP.isEnabled=true
                                plookupBinding.apply {
                                    ClMemnberPhoneNumber.visibility=View.VISIBLE
                                    plookupBinding.btnPhoneLookUP.visibility=View.GONE
                                    plookupBinding.btnContinue.visibility=View.VISIBLE
                                    etName.setText(Constants.SHARENAMELOOKUP)
                                    etMEMNo.setText(Constants.SHAREMEMBERLOOKUP)
                                    tvPShareEquity.text=Constants.SHAREVALUE.toString()
                                    PKES.text=Constants.SHAREKES
                                }
                            }
                            2->{
                                plookupBinding.apply {
                                    ClMemnberNumber.visibility = View.VISIBLE
                                    plookupBinding.btnPhoneLookUP.visibility=View.GONE
                                    plookupBinding.btnContinue.visibility=View.VISIBLE
                                    etMemberNo.isFocusable=true
                                    etMname.setText(Constants.SHARENAMELOOKUP)
                                    etRecPhoneNo.setText(Constants.SHAREPHONELOOKUP)
                                    tvShareEquity.text=Constants.SHAREVALUE.toString()

                                    KES.text=Constants.SHAREKES

                                }
                            }
                        }

                    }
                    0 -> {
                        viewModel.stopObserving()
                        plookupBinding.etMemberNo.isFocusable=true
                        plookupBinding.btnPhoneLookUP.isEnabled=true
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                    }
                    else -> {
                        viewModel.stopObserving()
                        plookupBinding.etMemberNo.isFocusable=true
                        plookupBinding.btnPhoneLookUP.isEnabled=true
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))

                    }
                }
            }
        })
        viewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                plookupBinding.progressbar.visibility=View.GONE
                //for transfer
                if (Constants.DIALOGSELETED==2) {
                    when (it) {

                        1 -> {
                            viewModel.allTransferShareResponse.observe(
                                viewLifecycleOwner,
                                Observer { share ->
                                    val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                                    cardShareBinding =ShareDialogLayoutBinding.inflate(LayoutInflater.from(context))
                                    cardShareBinding.apply {
                                        tvHeading.text = getString(R.string.transfer_share_request)
                                        tvNoOfShareValue.text = "${share.numberOfShares} Shares"
                                        tvValuecharges.text = share.charges.toString().trim()
                                        tvDutyValue.text = share.exciseDuty.toString().trim()
                                        tvCurrencycharges.text = share.currency
                                        tvCurrencyDuty.text = share.currency
                                        // tvCurrencyVshare.text=it.currency
                                        tvVOfShare.visibility = View.GONE
                                        tvValueOfShare.visibility = View.GONE

                                    }
                                    cardShareBinding.btnCancel.setOnClickListener {
                                        dialog.cancel()
                                    }
                                    cardShareBinding.btnSubmit.setOnClickListener {
                                        dialog.cancel()
                                        val commitShareDTO = CommitShareDTO()
                                        commitShareDTO.numberOfShares = share.numberOfShares
                                        commitShareDTO.valuePerShare = share.valuePerShare
                                        commitShareDTO.currency = share.currency
                                        commitShareDTO.charges = share.charges
                                        commitShareDTO.exciseDuty = share.exciseDuty
                                        commitShareDTO.transactionType = share.transactionType
                                        commitShareDTO.fromAccountId = share.fromAccountId
                                        commitShareDTO.toClientId = share.toClientId
                                        commitShareDTO.transactionDate = share.transactionDate
                                        commitShareDTO.transactionCode = share.transactionCode
                                        commitShareDTO.orgId = share.orgId
                                        commitShareDTO.fromClientId = share.fromClientId
                                        cardShareBinding.progressr.visibility=View.VISIBLE
                                        cardShareBinding.progressr.tv_pbTitle.visibility=View.GONE
                                        cardShareBinding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                                        viewModel.commitShare(commitShareDTO)
                                        viewModel.stopObserving()

                                    }
                                    dialog.setContentView(cardShareBinding.root)
                                    dialog.show()
                                    dialog.setCancelable(false)
                                    viewModel.stopObserving()
                                })


                        }
                        0 -> {
                            onInfoDialog(requireContext(), viewModel.shareMessage.value)
                        }
                        else -> {
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))

                        }
                    }
                }else{
                   // for sell
                    when (it) {

                        1 -> {
                        viewModel.allTransferShareResponse.observe(
                        viewLifecycleOwner,
                        Observer { share ->
                            val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                            cardShareBinding =
                                ShareDialogLayoutBinding.inflate(LayoutInflater.from(context))
                            cardShareBinding.apply {
                                tvHeading.text = getString(R.string.sell_share_request)
                                tvNoOfShareValue.text = "${share.numberOfShares} Shares"
                                tvValuecharges.text = share.charges.toString().trim()
                                tvDutyValue.text = share.exciseDuty.toString().trim()
                                tvCurrencycharges.text = share.currency
                                tvCurrencyDuty.text = share.currency
                                 tvCurrencyVshare.text=share.currency
                                val valuue=share.valuePerShare
                                val no=share.numberOfShares
                                tvValueOfShare.text= "${valuue*no}"
                                val total="${valuue*no}"
                                Timber.d("VALUUUE $total")
                                Constants.TOTALSVALUE=total

                            }
                            cardShareBinding.btnCancel.setOnClickListener {
                                dialog.cancel()
                            }
                            cardShareBinding.btnSubmit.setOnClickListener {
                                val commitShareDTO = CommitShareDTO()
                                commitShareDTO.numberOfShares = share.numberOfShares
                                commitShareDTO.valuePerShare = share.valuePerShare
                                commitShareDTO.currency = share.currency
                                commitShareDTO.charges = share.charges
                                commitShareDTO.exciseDuty = share.exciseDuty
                                commitShareDTO.transactionType = share.transactionType
                                commitShareDTO.fromAccountId = share.fromAccountId
                                commitShareDTO.toClientId = share.toClientId
                                commitShareDTO.transactionDate = share.transactionDate
                                commitShareDTO.transactionCode = share.transactionCode
                                commitShareDTO.orgId = share.orgId
                                commitShareDTO.fromClientId = share.fromClientId
                                plookupBinding.progressbar.visibility=View.VISIBLE
                                plookupBinding.progressbar.tv_pbTitle.visibility=View.GONE
                                plookupBinding.progressbar.tv_pbTex.text=getString(R.string.please_wait)
                                dialog.dismiss()
                                viewModel.commitShare(commitShareDTO)
                                viewModel.stopObserving()

                            }
                            dialog.setContentView(cardShareBinding.root)
                            dialog.show()
                            dialog.setCancelable(false)

                        })


                        }
                        0 -> {
                            onInfoDialog(requireContext(), viewModel.shareMessage.value)
                        }
                        else -> {
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))

                        }
                    }
                }
            }
        })
        viewModel.commitCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                plookupBinding.progressbar.visibility=View.GONE
                cardShareBinding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        if (Constants.DIALOGSELETED==2){
                            Constants.isFROMPESALINKPHONE=4
                            val directions=SharePhoneLookupFragmentDirections.actionSharePhoneLookupFragmentToShareSuccessFragment(fragmentType = 0)
                            findNavController().navigate(directions)
                            viewModel.stopObserving()
                        }else{
                            Constants.isFROMPESALINKPHONE=5
                            val directions=SharePhoneLookupFragmentDirections.actionSharePhoneLookupFragmentToShareSuccessFragment(fragmentType = 1)
                            findNavController().navigate(directions)
                            viewModel.stopObserving()
                        }

                    //    findNavController().navigate(R.id.action_sharePhoneLookupFragment_to_successDepositFragment)
                        viewModel.stopObserving()
                    }
                    0 -> {
                        onInfoDialog(requireContext(),viewModel.commitMessage.value)
                    }
                    else -> {
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))

                    }
                }
            }
        })
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        plookupBinding.toolbar.custom_toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )
        if (Constants.DIALOGSELETED == 1) {
            plookupBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.sell_shares)

        } else {
            plookupBinding.toolbar.custom_toolbar.custom_toolbar_title.text =
                getString(R.string.transfer_shares)
        }
    }



}