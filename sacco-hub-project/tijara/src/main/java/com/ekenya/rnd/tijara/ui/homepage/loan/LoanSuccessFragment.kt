package com.ekenya.rnd.tijara.ui.homepage.loan

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentSendMoneySuccessBinding
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.DashboardViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.PesalinkPhoneCheckViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.SendToPhoneViewmodel
import com.ekenya.rnd.tijara.ui.homepage.loan.getloan.LoanRequestViewModel
import com.ekenya.rnd.tijara.ui.homepage.loan.payloan.LoanRepaymentViewModel
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.camelCase
import com.ekenya.rnd.tijara.utils.makeGone
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class LoanSuccessFragment : Fragment() {
    private lateinit var binding:FragmentSendMoneySuccessBinding
    private val loanRequestVM :LoanRequestViewModel by activityViewModels()
    private val loanRePayVM : LoanRepaymentViewModel by activityViewModels()
    private var accNumber=""
    val args: LoanSuccessFragmentArgs by  navArgs()
    /*private val loanRequestVM by lazy {
        ViewModelProvider(requireActivity()).get(LoanRequestViewModel::class.java)
    }*/

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
            val time = currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy  |  HH:mm a"))
            tvTimeNdate.setText(time)
            when (args.fragmentType){
                /**loan request fragment*/
               0->{
                   tvName.text=getString(R.string.type)
                   tvNameValue.text =Constants.PRODUCTNAME
                   tvBank.text =getString(R.string.amoun)
                   tvACNO.text =  getString(R.string.deposit_to)
                   loanRequestVM.accountNumber.observe(viewLifecycleOwner) { acNo ->
                       accNumber = acNo.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                   }
                   loanRequestVM.accountName.observe(viewLifecycleOwner) { acName ->
                       tvACNOValue.text = "$acName - A/C $accNumber"
                   }
                   checkTitle.text=String.format(getString(R.string.get),camelCase(Constants.PRODUCTNAME))
                   tvAmount.text = getString(R.string.charges)
                   loanRequestVM.amount.observe(viewLifecycleOwner) { amount ->
                       val finalAmount = FormatDigit.formatDigits(amount)
                       tvBankValue.text = String.format(getString(R.string.kesh), finalAmount)
                   }
                   loanRequestVM.charges.observe(viewLifecycleOwner) { charges ->
                       val formatCharge=FormatDigit.formatDigits(charges)
                       tvAmountValue.text = String.format(getString(R.string.kesh), formatCharge)
                   }
                   loanRequestVM.refCode.observe(viewLifecycleOwner) { refId ->
                       tvREFIDValue.text = refId
                   }
                   tvFromValur.makeGone()
                   tvFrom.makeGone()
                   tvDuty.makeGone()
                   tvDutyValue.makeGone()
               }
                /**loan payment fragment*/
                1->{
                    tvName.text=getString(R.string.type)
                    tvNameValue.text =Constants.PRODUCTNAME
                    tvBank.text =getString(R.string.amoun)
                    tvACNO.text =  getString(R.string.pay_from)
                    if (Constants.SELECTED_TYPE==1){
                        loanRePayVM.phone.observe(viewLifecycleOwner) { phoneNu ->
                            tvACNOValue.text=phoneNu
                        }
                    }else {
                        loanRePayVM.accountNumber.observe(viewLifecycleOwner) { acNo ->
                            accNumber = acNo.replace("(?<=.{3}).(?=.{3})".toRegex(), "*")
                        }
                        loanRePayVM.accountName.observe(viewLifecycleOwner) { acName ->
                            tvACNOValue.text = "$acName - A/C $accNumber"
                        }
                    }
                    checkTitle.text=String.format(getString(R.string.payl),camelCase(Constants.PRODUCTNAME))
                    tvAmount.text = getString(R.string.charges)
                    loanRePayVM.amountValue.observe(viewLifecycleOwner) { amount ->
                        val finalAmount = FormatDigit.formatDigits(amount)
                        tvBankValue.text = String.format(getString(R.string.kesh), finalAmount)
                    }
                    loanRePayVM.charges.observe(viewLifecycleOwner) { charges ->
                        val formatCharge=FormatDigit.formatDigits(charges)
                        tvAmountValue.text = String.format(getString(R.string.kesh), formatCharge)
                    }
                    loanRePayVM.refCode.observe(viewLifecycleOwner) { refId ->
                        tvREFIDValue.text = refId
                    }
                    tvFromValur.makeGone()
                    tvFrom.makeGone()
                    tvDuty.makeGone()
                    tvDutyValue.makeGone()
                }
                /**self transfer*/

            }
        }
    }


}