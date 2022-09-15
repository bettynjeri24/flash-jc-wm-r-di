package com.ekenya.rnd.tijara.ui.homepage.loan.loancalculator

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.BankAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.LoanSpinnerAdapter
import com.ekenya.rnd.tijara.databinding.LoanCalculatorFragmentBinding
import com.ekenya.rnd.tijara.network.model.LoanProduct
import com.ekenya.rnd.tijara.requestDTO.AddGuarantorDTO
import com.ekenya.rnd.tijara.requestDTO.LoanCalDTO
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import java.util.ArrayList

class LoanCalculatorFragment : Fragment() {
private lateinit var binding: LoanCalculatorFragmentBinding
private lateinit var viewModel: LoanCalculatorViewModel
private lateinit var  adapter:LoanSpinnerAdapter
private val loanList:ArrayList<LoanProduct> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= LoanCalculatorFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(LoanCalculatorViewModel::class.java)
         adapter= LoanSpinnerAdapter(requireContext(),loanList)
        binding.loanSpinner.adapter=adapter
        binding.layouData.visibility=View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadLoanProducts()
        viewModel.loanProduct.observe(viewLifecycleOwner, Observer {
           loanList.clear()
            loanList.addAll(it)
            adapter.notifyDataSetChanged()
            binding.loanSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.setRate(it[position].interestRate)

                    Constants.PRODUCTNAME=it[position].name
                    Constants.PRODUCTID=it[position].productId.toString()
                }
            }

        })
        viewModel.rate.observe(viewLifecycleOwner, Observer {
            binding.etRate.setText(it)
        })
        binding.apply {
            btnCal.setOnClickListener {
                val amount=etAmount.text.toString()
                val rate=etRate.text.toString()
                val period=etPeriod.text.toString()
                if (amount.isEmpty()){
                    tlEnterAmount.error=getString(R.string.amount_cannot_be_empty)
                }else if(Constants.PRODUCTNAME.isEmpty()){
                    toastyError(requireContext(),"Select the Loan Type")
                }
                else if (period.isEmpty()){
                    tlEnterAmount.error=""
                    tlPeriod.error=getString(R.string.required)
                }else{
                    binding.btnCal.isEnabled=false
                    tlPeriod.error=""
                    binding.progressr.makeVisible()
                    binding.progressr.tv_pbTitle.makeGone()
                    binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                    val loanCalDTO= LoanCalDTO()
                    loanCalDTO.amount=amount.toString().toInt()
                    loanCalDTO.productId=Constants.PRODUCTID.toInt()
                    loanCalDTO.repaymentPeriod=period.toInt()
                    viewModel.calculateLoan(loanCalDTO)
                }
            }
            viewModel.statusCode.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    binding.progressr.makeGone()
                    binding.btnCal.isEnabled

                    when (it) {
                        1 -> {
                            binding.btnCal.isEnabled=true
                            binding.layouData.makeVisible()
                            viewModel.calData.observe(viewLifecycleOwner,{loanData->
                                tvIAmount.text= String.format(getString(R.string.kesh),FormatDigit.formatDigits(loanData.interestAmount))
                                tvPayableAmount.text= String.format(getString(R.string.total_amount_payable_is),FormatDigit.formatDigits(loanData.totalLoanAmount))
                            })

                            viewModel.stopObserving()
                        }
                        0 -> {
                            binding.btnCal.isEnabled=true
                            binding.layouData.makeGone()
                            onInfoDialog(requireContext(),viewModel.statusMessage.value)
                            viewModel.stopObserving()
                        }
                        else -> {
                            binding.btnCal.isEnabled=true
                            binding.layouData.makeGone()
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))
                            viewModel.stopObserving()
                        }
                    }
                }
            })
        }
        setupNavUp()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.loans_calculator)
    }

}