package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
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
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.SendToPhoneViewmodel
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.camelCase
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BillSuccessFragment : Fragment() {
    private var accNumber=""
    private lateinit var binding:FragmentSendMoneySuccessBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(BillersViewModel::class.java)
    }
    val args: BillSuccessFragmentArgs by navArgs()

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
                /**pay bill fragment*/
               0->{
                   viewModel.accountName.observe(viewLifecycleOwner) { meName ->
                       tvNameValue.text = meName
                   }
                   tvBank.text=getString(R.string.account_number)
                   viewModel.accountNumber.observe(viewLifecycleOwner) { acNo ->
                       tvBankValue.text = acNo
                   }
                   tvACNO.text=getString(R.string.amoun)
                   viewModel.amount.observe(viewLifecycleOwner) { amount ->
                       val finalAmount = FormatDigit.formatDigits(amount)
                       tvACNOValue.text = String.format(getString(R.string.kesh), finalAmount)
                   }
                   viewModel.billerName.observe(viewLifecycleOwner) { billerName ->
                       checkTitle.text = String.format(billerName, "Bill Payment successful")
                   }
                   tvAmount.text=getString(R.string.pay_from)
                   viewModel.primeAccountNumber.observe(viewLifecycleOwner) { acNo ->
                       accNumber = acNo.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                   }
                   viewModel.primeAccountName.observe(viewLifecycleOwner) { acName ->
                       tvAmountValue.text = "$acName - A/C $accNumber"
                   }
                   viewModel.refId.observe(viewLifecycleOwner) { refId ->
                       tvREFIDValue.text = refId
                   }
                   viewModel.charges.observe(viewLifecycleOwner) { charges ->
                       val finalCharges = FormatDigit.formatDigits(charges)
                       tvFromValur.text = String.format(getString(R.string.kesh), finalCharges)
                   }
                   tvFrom.text=getString(R.string.charges)
                   tvDuty.visibility=View.GONE
                   tvDutyValue.visibility=View.GONE
               }
                /**count PayBill fragment*/
                1->{
                    tvName.text=getString(R.string.amoun)
                    tvNameValue.text = " KES "
                    tvBank.text =getString(R.string.biller_number)
                    tvBankValue.text = Constants.GENERAL_NUMBER
                    tvACNO.text =  getString(R.string.member_name)
                    tvACNOValue.text = Constants.GENERAL_NAME
                    checkTitle.text="${Constants.BILLER_NAME} Bill Payment successful"
                    tvAmount.text = getString(R.string.county)
                    tvAmountValue.text=Constants.CountyName
                    tvREFIDValue.text=Constants.AIRTIMEREFID
                    val accNumber = Constants.SAVINGACCOUNTNO.replace(
                        "(?<=.{3}).(?=.{3})".toRegex(),
                        "*"
                    )
                    tvFromValur.text =
                        "${Constants.SAVINGACCOUNTNAME} - A/C $accNumber"
                    tvFrom.text =  getString(R.string.pay_from)
                    tvDuty.text=getString(R.string.charges)
                    tvDutyValue.text="KES ${Constants.CHARGES}"
                }
                /**self transfer*/

            }
        }
    }


}