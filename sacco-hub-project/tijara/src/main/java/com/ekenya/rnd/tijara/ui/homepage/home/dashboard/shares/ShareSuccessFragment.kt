package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

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
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentSendMoneySuccessBinding
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.PesalinkPhoneCheckViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.SendToPhoneViewmodel
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.camelCase
import com.ekenya.rnd.tijara.utils.makeGone
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ShareSuccessFragment : Fragment() {
    private lateinit var binding:FragmentSendMoneySuccessBinding

    val args: ShareSuccessFragmentArgs by  navArgs()

    private lateinit var commitViewModel:SharePhoneLookupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commitViewModel=ViewModelProvider(requireActivity())[SharePhoneLookupViewModel::class.java]
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
                /**loan request fragment*/
               0->{
                   commitViewModel.allCommitShareResponse.observe(viewLifecycleOwner,{
                       tvNameValue.text=it.memberName
                       tvACNOValue.text= "${it.numberOfShares} Shares"
                       tvAmountValue.text=it.currency +" " +it.charges
                       tvFromValur.text=it.currency +" " +it.exciseDuty
                   })
                   tvREFIDValue.text=Constants.SHAREREFCODE
                   tvBankValue.text=Constants.SHAREMEMBERLOOKUP
                   tvBank.text="MEMBER NUMBER:"
                   tvACNO.text="NUMBER OF SHARES:"
                   checkTitle.text=getString(R.string.transfer_share_request_successful)
                   tv1.text=getString(R.string.transfer_share_request_received)
                   tvAmount.text="CHARGES"
                   tvFrom.text="EXCISE DUTY:"
                   tvDuty.visibility=View.GONE
                   tvDutyValue.visibility=View.GONE
               }
                /**loan payment fragment*/
                1->{
                    commitViewModel.allCommitShareResponse.observe(viewLifecycleOwner, Observer {
                        tvNameValue.text=it.memberName
                        tvACNOValue.text= "${it.numberOfShares} Shares"
                        tvAmountValue.text=it.currency +" " + Constants.TOTALSVALUE
                        tvFromValur.text=it.currency +" " +it.charges
                        tvDutyValue.text=it.currency +" " +it.exciseDuty
                    })
                    tvREFIDValue.text=Constants.SHAREREFCODE
                    tvBankValue.text=Constants.SHAREMEMBERLOOKUP
                    tvBank.text="MEMBER NUMBER:"
                    tvACNO.text="NUMBER OF SHARES:"
                    checkTitle.text=getString(R.string.sell_share_request_successful)
                    tv1.text=getString(R.string.transfer_share_request_received)
                    tvAmount.text="VALUE OF SHARES"
                    tvFrom.text="CHARGES:"
                    tvDuty.text="EXCISE DUTY:"
                }
                /**self transfer*/

            }
        }
    }


}