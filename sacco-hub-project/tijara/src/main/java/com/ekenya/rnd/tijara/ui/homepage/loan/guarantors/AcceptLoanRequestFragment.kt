package com.ekenya.rnd.tijara.ui.homepage.loan.guarantors

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.AcceptLoanRequestFragmentBinding
import com.ekenya.rnd.tijara.utils.FormatDigit
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class AcceptLoanRequestFragment : Fragment() {
    private lateinit var viewModel: LoanGuarantedViewModel
    private lateinit var binding:AcceptLoanRequestFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= AcceptLoanRequestFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(LoanGuarantedViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args= AcceptLoanRequestFragmentArgs.fromBundle(requireArguments()).newLoanRequest
        binding.apply {
            tvTransType.text=args.loanName
            textShareTT.text=args.name
            textName.text=args.memberNumber
            val amouyt= FormatDigit.formatDigits(args.amountRequested)
            binding.textMNo.text= String.format(getString(R.string.kesh),amouyt)
            val texte = args.percentageRequested
            textM0bileN.text="%.2f".format(texte)+"%"
            textStatus.text=args.status
            btnAccept.setOnClickListener {
                Constants.ISFROMBLOOKUP=1
                findNavController().navigate(R.id.action_acceptLoanRequestFragment_to_acceptLoanFragment)
            }
            btnReject.setOnClickListener {
                Constants.ISFROMBLOOKUP=2
                findNavController().navigate(R.id.action_acceptLoanRequestFragment_to_acceptLoanFragment)
            }
        }
        setupNavUp()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.loans_requestd)
    }

}