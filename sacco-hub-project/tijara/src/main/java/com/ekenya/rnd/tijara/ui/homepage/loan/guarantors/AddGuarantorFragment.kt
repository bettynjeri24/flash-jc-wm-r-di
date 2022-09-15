package com.ekenya.rnd.tijara.ui.homepage.loan.guarantors

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.LoanGuarantedAdapter
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentAddGuarantorBinding
import com.ekenya.rnd.tijara.requestDTO.AddGuarantorDTO
import com.ekenya.rnd.tijara.requestDTO.LoanGuarantedDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*

class  AddGuarantorFragment : Fragment() {
private lateinit var binding:FragmentAddGuarantorBinding
private lateinit var viewModel: MyGuarantorsViewModel
    private lateinit var cardBinding: DepositDialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(MyGuarantorsViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddGuarantorBinding.inflate(layoutInflater)
        val loanGuarantedDTO=LoanGuarantedDTO()
        loanGuarantedDTO.mustBeGuaranteed=1
        viewModel.getLoanGuaranted(loanGuarantedDTO)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loans.observe(viewLifecycleOwner, Observer {
            val adapter= LoanGuarantedAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.accountSpinner.adapter=adapter
            binding.accountSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.LOANID=it[position].productId
                    Constants.PRODUCTNAME=it[position].name
                }
            }
        })
        binding.apply {
            BtnAdd.setOnClickListener {
                val memberNumber=etMemberNo.text.toString()
                val amount=etLoanAmount.text.toString()
                if (memberNumber.isEmpty()){
                    tlMemberNo.error=getString(R.string.enter_memebr_number_to_continue)
                }else if (amount.isEmpty()){
                    tlMemberNo.error=""
                    tlEnterAmount.error=getString(R.string.amount_cannot_be_empty)
                }else if(Constants.PRODUCTNAME.isEmpty()){
                    toastyError(requireContext(),"Select the Loan Type")
                }else{
                    tlEnterAmount.error=""
                    binding.progressr.makeVisible()
                    binding.progressr.tv_pbTitle.makeGone()
                    binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                    val addGuarantedDTO=AddGuarantorDTO()
                    addGuarantedDTO.loanId=Constants.LOANID
                    addGuarantedDTO.amount=amount.toInt()
                    Constants.GENERAL_AMOUNT=addGuarantedDTO.amount.toString()
                    addGuarantedDTO.memberNumber=memberNumber
                    Constants.GENERAL_NUMBER=addGuarantedDTO.memberNumber
                    viewModel.addGuarantors(addGuarantedDTO)
                }
            }


        }
        viewModel.status.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.makeGone()
                when (it) {
                    1 -> {

                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardBinding= DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            tvHeading.text=getString(R.string.confirm_add_guarontors)
                            tvName.text=getString(R.string.type)
                            tvNameValue.text=Constants.PRODUCTNAME
                            tvBank.text=getString(R.string.amoun)
                            tvBankValue.text= String.format(getString(R.string.kesh,Constants.GENERAL_AMOUNT))
                            tvACNOValue.text=Constants.GENERAL_NUMBER
                            tvACNO.text=getString(R.string.member_number)
                            tvAmount.text=getString(R.string.member_name)
                            tvFrom.makeGone()
                            tvAmountValue.text=Constants.GENERAL_NAME
                            tvFromValur.makeGone()
                        }

                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            binding.progressr.makeVisible()
                            binding.progressr.tv_pbTitle.makeGone()
                            binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                            val statementDTO=StatementDTO()
                            statementDTO.formId=Constants.FORMID
                            viewModel.addGuarantorsCommit(statementDTO)
                            viewModel.stopObserving()
                        }

                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)



                    }
                    0 -> {
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                    }
                    else -> {
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                    }
                }
            }
        })
        viewModel.statusCommit.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.makeGone()
                when (it) {
                    1 -> {
                        toastySuccess(requireContext(),"Guarantor added successfully")
                        findNavController().navigateUp()
                        viewModel.stopObserving()
                    }
                    0 -> {
                        onInfoDialog(requireContext(),viewModel.commitMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        viewModel.stopObserving()
                    }
                }
            }
        })
        setupNavUp()
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.add_guarontors)
    }
}