package com.ekenya.rnd.tijara.ui.homepage.statement

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentDepositSuccessBinding
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares.SharePhoneLookupViewModel
import com.ekenya.rnd.tijara.utils.camelCase
import com.ekenya.rnd.tijara.utils.makeGone
import kotlinx.android.synthetic.main.custom_toolbar.view.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StatementSuccessFragment:Fragment() {
    private lateinit var binding: FragmentDepositSuccessBinding
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
            /**full statement*/
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