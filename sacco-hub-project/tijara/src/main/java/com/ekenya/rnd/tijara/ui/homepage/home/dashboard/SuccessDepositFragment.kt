package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentDepositSuccessBinding
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime.BuyAirtimeViewmodel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares.SharePhoneLookupViewModel
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.camelCase
import com.ekenya.rnd.tijara.utils.makeGone
import kotlinx.android.synthetic.main.custom_toolbar.view.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SuccessDepositFragment:BaseDaggerFragment() {
    private lateinit var binding: FragmentDepositSuccessBinding
    var accNumber=""
    val args:SuccessDepositFragmentArgs by navArgs()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(BuyAirtimeViewmodel::class.java)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDepositSuccessBinding.inflate(layoutInflater)
        binding.apply {
            imageBack.setOnClickListener {
                findNavController().navigateUp()
            }
            val currentDateTime = LocalDateTime.now()
            var time=currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy  |  HH:mm a"))
            tvTimeNdate.setText(time)
            /**buy airtime*/
            when (args.fragmentType){
                0->{
                    viewModel.accountNumber.observe(viewLifecycleOwner) { acNo ->
                        accNumber = acNo.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                    }
                    viewModel.accountName.observe(viewLifecycleOwner) { acName ->
                        tvNameValue.text = "$acName - A/C $accNumber"
                    }
                    tvName.text=getString(R.string.buy_from)
                    viewModel.phone.observe(viewLifecycleOwner) { phone ->
                        tvBankValue.text = phone
                    }
                    viewModel.amount.observe(viewLifecycleOwner) { amount ->
                        val finalAmount = FormatDigit.formatDigits(amount)
                        tvACNOValue.text = String.format(getString(R.string.kesh), finalAmount)
                    }
                    viewModel.charges.observe(viewLifecycleOwner) { charges ->
                        tvAmountValue.text = String.format(getString(R.string.kesh), charges)
                    }
                    viewModel.refId.observe(viewLifecycleOwner) { refId ->
                        tvREFIDValue.text = refId
                    }
                    tvBank.text=getString(R.string.buy_for)
                    tvACNO.text=getString(R.string.amoun)
                    checkTitle.text=getString(R.string.airtime_purchase_sucess)
                    tvAmount.text=getString(R.string.charges)
                    tvFromValur.visibility=View.GONE
                    tvFrom.visibility=View.GONE
                    tvDuty.visibility=View.GONE
                    tvDutyValue.visibility=View.GONE
                }
               /** makeDeposit*/
                1->{
                    viewModel.accountNumber.observe(viewLifecycleOwner,{acNo->
                         accNumber=acNo.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                    })
                    viewModel.accountName.observe(viewLifecycleOwner,{acName->
                        tvNameValue.text="$acName - A/C $accNumber"
                    })

                    tvName.text=getString(R.string.deposit_to)
                    tvBank.text=getString(R.string.deposit_from)
                    tvACNO.text=getString(R.string.amoun)
                    checkTitle.text=getString(R.string.deposit_successful)
                    tv1.text=getString(R.string.request_receive_enter_pin)
                    tvAmount.text=getString(R.string.charges)
                    viewModel.refId.observe(viewLifecycleOwner) { refId ->
                        tvREFIDValue.text = refId
                    }
                    viewModel.phone.observe(viewLifecycleOwner) { phone ->
                        tvBankValue.text = phone
                    }
                    viewModel.amount.observe(viewLifecycleOwner) { amount ->
                        tvACNOValue.text = String.format(getString(R.string.kesh), amount)
                    }
                    viewModel.charges.observe(viewLifecycleOwner) { charges ->
                        tvAmountValue.text = String.format(getString(R.string.kesh), charges)
                    }
                    viewModel.duty.observe(viewLifecycleOwner) { duty ->
                        tvFromValur.text = String.format(getString(R.string.kesh), duty)
                    }
                    tvDuty.visibility=View.GONE
                    tvDutyValue.visibility=View.GONE
                    tvFrom.text=getString(R.string.excise_duty)
                }
            }
            if (Constants.isFROMPESALINKPHONE==7){
                val accNumber=Constants.SAVINGACCOUNTNO.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                tvNameValue.text="${Constants.SAVINGACCOUNTNAME} - A/C $accNumber"
                tvName.text=getString(R.string.account)
                tvBank.text=getString(R.string.start_date)
                tvBankValue.text=Constants.STARTDATE
                tvACNO.text=getString(R.string.end_date)
                tvACNOValue.text=Constants.ENDDATE
                checkTitle.text=getString(R.string.fullstatement_request_successful)
                tv1.text=getString(R.string.statement_received)
                tvAmount.text=getString(R.string.charges)
                tvAmountValue.text="KES ${Constants.FULLSCHARGES}"
                tvREFIDValue.text= Constants.FULLSREFCODE
                tvFromValur.visibility=View.GONE
                tvDuty.visibility=View.GONE
                tvDutyValue.visibility=View.GONE
                tvFrom.visibility=View.GONE

            }


            doneBtn.setOnClickListener {
                /*tvNameValue.text=Constants.SAVINGACCOUNTNAME
                tvBankValue.text=Constants.AIRTIMEPHON
                tvACNOValue.text ="KSH ${Constants.AIRTIMEAMOUNT}"
                tvAmountValue.text ="KSH 00.00"*/
                Constants.DIALOGSELETED=-1
               findNavController().navigateUp()
            }
        }
        return binding.root
    }

}