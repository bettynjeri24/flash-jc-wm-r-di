package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentSendMoneySuccessBinding
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.PesalinkPhoneCheckViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.SendToPhoneViewmodel
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class StandingOrderSuccessFragment : Fragment() {
    private lateinit var binding:FragmentSendMoneySuccessBinding
    var id=""
    private val viewmodel by lazy {
        ViewModelProviders.of(requireActivity()).get(StandindOrderViewModel::class.java)
    }

    val args:StandingOrderSuccessFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentSendMoneySuccessBinding.inflate(layoutInflater)
        binding.apply {
            imageBack.setOnClickListener {
                findNavController().navigateUp()
            }
            doneBtn.setOnClickListener {
                findNavController().navigateUp()
              //  findNavController().navigate(R.id.action_sendMoneySuccessFragment_to_sendMoneyOptionFragment)
            }
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val currentDateTime = LocalDateTime.now()
            var time = currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy  |  HH:mm a"))
            tvTimeNdate.setText(time)
            when (args.fragmentType){
               1->{
                   checkTitle.text=getString(R.string.confirm_create_standing)
                   tv1.text="Your request has been received and is being processed"
                       tvName.text=getString(R.string.standing_order_account)
                   viewmodel._standingOrderName.observe(viewLifecycleOwner) {
                       tvNameValue.text = it
                   }
                   viewmodel._payFromId.observe(viewLifecycleOwner) {
                       id = it
                   }
                   tvBank.text=getString(R.string.pay_from)
                   viewmodel._payFromName.observe(viewLifecycleOwner) {
                       val accNumber = id.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                       tvBankValue.text = "${it} - A/C $accNumber"
                   }
                   viewmodel.amount.observe(viewLifecycleOwner) {
                       val amount = FormatDigit.formatDigits(it)
                       tvACNOValue.text = String.format(getString(R.string.kesh), amount)
                   }
                   tvACNO.text=getString(R.string.amoun)
                   viewmodel._charges.observe(viewLifecycleOwner) { char ->
                       val charges = FormatDigit.formatDigits(char)
                       tvFromValur.text = String.format(getString(R.string.kesh), charges)
                   }
                   viewmodel.frequencyName.observe(viewLifecycleOwner) { fre ->
                       tvAmountValue.text = fre
                   }
                   tvAmount.text="Frequency"
                   tvFrom.text=getString(R.string.charges)


                   viewmodel.refId.observe(viewLifecycleOwner) {
                       tvREFIDValue.text = it
                   }

                   tvDuty.visibility=View.GONE
                   tvDutyValue.visibility=View.GONE
               }
                2->{
                    checkTitle.text="Create Cheque Request successful"
                    tv1.text="Your request has been received and is being processed"
                    tvName.text="Payable To:"
                    viewmodel._payee.observe(viewLifecycleOwner) {
                        tvNameValue.text = it
                    }
                    viewmodel.amount.observe(viewLifecycleOwner) {
                        val amount = FormatDigit.formatDigits(it)
                        tvBankValue.text = String.format(getString(R.string.kesh), amount)

                    }
                    tvBank.text=getString(R.string.amoun)
                    viewmodel.amount.observe(viewLifecycleOwner) {

                    }
                    tvACNO.text="Bank Branch"
                    viewmodel.bankName.observe(viewLifecycleOwner) {
                        tvACNOValue.text = it
                    }

                    val charges= FormatDigit.formatDigits(Constants.CHARGES.toString())
                        tvFromValur.text=String.format(getString(R.string.kesh),charges)

                    tvAmount.text="Collection Date"
                    viewmodel._date.observe(viewLifecycleOwner) {

                        tvAmountValue.text = it
                    }
                    tvFrom.text=getString(R.string.charges)
                    viewmodel.refId.observe(viewLifecycleOwner) {
                        tvREFIDValue.text = it
                    }

                    tvDuty.visibility=View.GONE
                    tvDutyValue.visibility=View.GONE
                }
                /**self transfer*/
                2->{
                    tvName.text=getString(R.string.amoun)
                    val amount=FormatDigit.formatDigits(Constants.SELFAMOUNT)
                    tvNameValue.text=String.format(getString(R.string.kesh),amount)
                    tvBank.text=getString(R.string.transfer_from)
                    val fromId=Constants.SaveACNOFROM.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                    tvBankValue.text="${Constants.SaveNameFrom} - A/C $fromId"
                    tvACNO.text=getString(R.string.transfer_to)
                    val fromIdTo=Constants.SaveACNOTo.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                    tvACNOValue.text="${Constants.SaveNameTo} - A/C $fromIdTo"
                    checkTitle.text=getString(R.string.self_transfer_transaction_sucess)
                    tvAmount.text=getString(R.string.charges)
                    tvAmountValue.text="ZWL ${Constants.EDUTY}"
                    tvREFIDValue.text=Constants.SELFREFID
                    tvFromValur.text="ZWL ${Constants.CHARGES}"
                    tvDuty.visibility=View.GONE
                    tvDutyValue.visibility=View.GONE
                    tvFrom.text=getString(R.string.excise_duty)
                }
                /**member transfer*/
                3->{
                    tvName.text=getString(R.string.amoun)
                    val amount=FormatDigit.formatDigits(Constants.SELFAMOUNT)
                    tvNameValue.text=String.format(getString(R.string.kesh),amount)
                    tvBank.text=getString(R.string.transfer_from)
                    val fromId=Constants.SaveACNOFROM.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                    tvBankValue.text="${Constants.SaveNameFrom} - A/C $fromId"
                    tvACNO.text=getString(R.string.recipient_account_number)
                    val accountNo=
                        Constants.MEMBERACCNO.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                    tvACNOValue.text=accountNo
                    checkTitle.text=getString(R.string.other_transfer_transaction_sucess)
                    tvAmount.text=getString(R.string.recipient_name)
                    tvAmountValue.text=Constants.MEMBERRECIPNAME
                    tvREFIDValue.text=Constants.SELFREFID
                    tvFromValur.text="ZWL ${Constants.CHARGES}"
                    tvDuty.text=getString(R.string.excise_duty)
                    tvDutyValue.text="ZWL ${Constants.EDUTY}"
                    tvFrom.text=getString(R.string.charges)
                }
                /**mobile money transfer*/

                4->{
                    if (Constants.SPROVIDERNAME=="EcoCash"){
                        tvName.text=getString(R.string.send_to_mpesa)
                    }else if (Constants.SPROVIDERNAME=="One Wallet"){
                        tvName.text=getString(R.string.send_to_airtel)
                    }else if (Constants.SPROVIDERNAME=="Telecash"){
                        tvName.text=getString(R.string.send_to_tcash)
                    }
                    val amount=FormatDigit.formatDigits(Constants.AIRTIMEAMOUNT)
                    tvBankValue.text=String.format(getString(R.string.kesh),amount)
                    tvNameValue.text=Constants.AIRTIMEPHON
                    tvBank.text=getString(R.string.amoun)
                   // tvBankValue.text=Constants.AIRTIMEAMOUNT
                    tvACNO.text=getString(R.string.charges)
                    tvACNOValue.text="ZWL ${Constants.AIRTIMECHARGES}"
                    checkTitle.text=getString(R.string.mobile_money_transfer_sucess)
                    tvAmount.text=getString(R.string.excise_duty)
                    tvAmountValue.text="ZWL ${Constants.AIRTIMEDUTY}"
                    tvREFIDValue.text=Constants.AIRTIMEREFID
                    tvFromValur.visibility=View.GONE
                    tvDuty.visibility=View.GONE
                    tvDutyValue.visibility=View.GONE
                    tvFrom.visibility=View.GONE
                }
            }
        }
    }


}