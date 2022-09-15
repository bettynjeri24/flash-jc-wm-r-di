package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime.BuyAirtimeViewmodel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.PesalinkPhoneCheckViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.SendToAccNumberViewmodel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.SendToPhoneViewmodel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney.internaltransfer.SelfTransferViewModel
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SendMoneySuccessFragment : BaseDaggerFragment() {
    private lateinit var binding:FragmentSendMoneySuccessBinding
    var accNumber=""
    var accNumberTo=""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val buyAirtimeViewmodel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(BuyAirtimeViewmodel::class.java)
    }

    private val viewmodel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SendToPhoneViewmodel::class.java)
    }
    private val selfViewmodel by lazy {
        ViewModelProvider(requireActivity()).get(SelfTransferViewModel::class.java)
    }
    private val accountviewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SendToAccNumberViewmodel::class.java)
    }
    private val pesalinkPhoneCheckViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PesalinkPhoneCheckViewModel::class.java)
    }
    val args:SendMoneySuccessFragmentArgs by navArgs()

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
                /**pesa link to phone*/
               0->{
                   tvName.text=getString(R.string.recipient_name)
                   tvBank.text=getString(R.string.recipient_bank)
                   tvACNO.text=getString(R.string.recipient_phone)
                   tvFrom.text=getString(R.string.transfer_from)
                   checkTitle.text=getString(R.string.pesa_link_transaction_sucess)
                   pesalinkPhoneCheckViewModel.phone.observe(viewLifecycleOwner) {
                       tvACNOValue.text = it
                   }
                   pesalinkPhoneCheckViewModel.name.observe(viewLifecycleOwner) {
                       tvNameValue.text = (it)
                   }
                   viewmodel.amount.observe(viewLifecycleOwner) {
                       tvAmountValue.text = FormatDigit.formatDigits(it)
                   }
                   viewmodel.bankName.observe(viewLifecycleOwner) {
                       tvBankValue.text = (it)
                   }
                   viewmodel.accountNumber.observe(viewLifecycleOwner) { number ->
                       accNumber = number.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                   }
                   viewmodel.accountName.observe(viewLifecycleOwner) { accName ->
                       tvFromValur.text = "$accName- A/C $accNumber"
                   }

                   tvDuty.text=getString(R.string.charges)
                   viewmodel.charges.observe(viewLifecycleOwner) { charges ->
                       tvDutyValue.text = String.format(getString(R.string.charges), charges)
                   }
                   viewmodel.refId.observe(viewLifecycleOwner) { ref ->
                       tvREFIDValue.text = ref
                   }
               }
                /**pesa link to account*/
                1->{
                    tvName.text=getString(R.string.recipient_name)
                    tvBank.text=getString(R.string.recipient_bank)
                    tvACNO.text=getString(R.string.recipient_acc_no)
                    tvFrom.text=getString(R.string.transfer_from)
                    accountviewModel.recName.observe(viewLifecycleOwner) { recName ->
                        tvNameValue.text = recName
                    }
                    accountviewModel.bankName.observe(viewLifecycleOwner) { bankName ->
                        tvBankValue.text = bankName
                    }
                    accountviewModel.resAccNo.observe(viewLifecycleOwner) { resAccNo ->
                        val recAcc = resAccNo.replace("(?<=.{5}).(?=.{5})".toRegex(), "*")
                        tvACNOValue.text = recAcc
                    }
                    accountviewModel.amount.observe(viewLifecycleOwner) { amount ->
                        val amoun = FormatDigit.formatDigits(amount)
                        tvAmountValue.text = String.format(getString(R.string.kesh), amoun)
                    }
                    checkTitle.text=getString(R.string.pesa_link_transaction_sucess)
                    accountviewModel.accountNumber.observe(viewLifecycleOwner) { number ->
                        accNumber = number.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                    }
                    accountviewModel.accountName.observe(viewLifecycleOwner) { accName ->
                        tvFromValur.text = "$accName- A/C $accNumber"
                    }

                    tvDuty.text=getString(R.string.charges)
                    accountviewModel.charges.observe(viewLifecycleOwner) { charges ->
                        tvDutyValue.text = String.format(getString(R.string.kesh), charges)
                    }
                    accountviewModel.refId.observe(viewLifecycleOwner) { ref ->
                        tvREFIDValue.text = ref
                    }
                }
                /**self transfer*/
                2->{
                    tvName.text=getString(R.string.amoun)
                    selfViewmodel.amount.observe(viewLifecycleOwner) { amount ->
                        Log.d("TAG", "AMOU$amount")
                        val amoun = FormatDigit.formatDigits(amount)
                        tvNameValue.text = String.format(getString(R.string.kesh), amoun)
                    }
                    tvBank.text=getString(R.string.transfer_from)
                    selfViewmodel.accountNumber.observe(viewLifecycleOwner) { number ->
                        accNumber = number.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                    }
                    selfViewmodel.accountName.observe(viewLifecycleOwner) { accName ->
                        tvBankValue.text = "$accName- A/C $accNumber"
                    }
                    tvACNO.text=getString(R.string.transfer_to)
                    selfViewmodel.accountNumberTo.observe(viewLifecycleOwner) { number ->
                        accNumberTo = number.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                    }
                    selfViewmodel.accountNameTo.observe(viewLifecycleOwner) { accName ->
                        tvACNOValue.text = "$accName- A/C $accNumberTo"
                    }
                    checkTitle.text=getString(R.string.self_transfer_transaction_sucess)

                    tvAmount.text=getString(R.string.charges)
                    selfViewmodel.charges.observe(viewLifecycleOwner) { charges ->
                        val finalCharges = FormatDigit.formatDigits(charges)
                        tvAmountValue.text = String.format(getString(R.string.kesh), finalCharges)
                    }
                    selfViewmodel.refId.observe(viewLifecycleOwner) { ref ->
                        tvREFIDValue.text = ref
                    }

                    tvFromValur.makeGone()
                    tvDuty.visibility=View.GONE
                    tvDutyValue.visibility=View.GONE
                    tvFrom.makeGone()
                }
                /**member transfer*/
                3->{
                    tvName.text=getString(R.string.amoun)
                    selfViewmodel.amount.observe(viewLifecycleOwner) { amount ->
                        val amoun = FormatDigit.formatDigits(amount)
                        tvNameValue.text = String.format(getString(R.string.kesh), amoun)
                    }
                    tvBank.text=getString(R.string.transfer_from)
                    selfViewmodel.accountNumber.observe(viewLifecycleOwner) { number ->
                        accNumber = number.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                    }
                    selfViewmodel.accountName.observe(viewLifecycleOwner) { accName ->
                        tvBankValue.text = "$accName- A/C $accNumber"
                    }
                    tvACNO.text=getString(R.string.recipient_acc_no)
                    selfViewmodel.resAccNo.observe(viewLifecycleOwner) { number ->
                        val accountNo =
                            number.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                        tvACNOValue.text = accountNo
                    }
                    selfViewmodel.recName.observe(viewLifecycleOwner) { namer ->
                        val name =
                            namer.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                        tvAmountValue.text = name
                    }

                    checkTitle.text=getString(R.string.other_transfer_transaction_sucess)
                    tvAmount.text=getString(R.string.recipient_name)
                    selfViewmodel.charges.observe(viewLifecycleOwner) { charges ->
                        val finalCharges = FormatDigit.formatDigits(charges)
                        tvFromValur.text = String.format(getString(R.string.kesh), finalCharges)
                    }
                    selfViewmodel.refId.observe(viewLifecycleOwner) { ref ->
                        tvREFIDValue.text = ref
                    }
                    tvDuty.makeGone()
                    tvDutyValue.makeGone()
                    tvFrom.text=getString(R.string.charges)
                }
                /**mobile money transfer*/
                4->{
                    buyAirtimeViewmodel.accountNumber.observe(viewLifecycleOwner) { acNo ->
                        accNumber = acNo.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                    }
                    buyAirtimeViewmodel.accountName.observe(viewLifecycleOwner) { acName ->
                        tvBankValue.text = "$acName - A/C $accNumber"
                    }
                    buyAirtimeViewmodel.phone.observe(viewLifecycleOwner) { phone ->
                        tvNameValue.text = phone
                    }
                    buyAirtimeViewmodel.amount.observe(viewLifecycleOwner) { amount ->
                        tvACNOValue.text = String.format(getString(R.string.kesh), amount)
                    }
                    buyAirtimeViewmodel.charges.observe(viewLifecycleOwner) { charges ->
                        tvAmountValue.text = String.format(getString(R.string.kesh), charges)
                    }
                    buyAirtimeViewmodel.refId.observe(viewLifecycleOwner) { refId ->
                        tvREFIDValue.text = refId
                    }
                    if (Constants.SPROVIDERNAME=="MPESA"){
                        tvName.text=getString(R.string.send_to_mpesa)
                    }else if (Constants.SPROVIDERNAME=="AIRTEL MONEY"){
                        tvName.text=getString(R.string.send_to_airtel)
                    }else if (Constants.SPROVIDERNAME=="T-KASH"){
                        tvName.text=getString(R.string.send_to_tcash)
                    }
                    tvBank.text=getString(R.string.transfer_from)
                    tvACNO.text=getString(R.string.amoun)
                    checkTitle.text=getString(R.string.mobile_money_transfer_sucess)
                    tvAmount.text=getString(R.string.charges)
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