package com.ekenya.rnd.tijara.ui.homepage.loan.guarantors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.LoanGuarantedDetailsFragmentBinding
import com.ekenya.rnd.tijara.utils.FormatDigit
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class LoanGuarantedDetailsFragment : Fragment() {
    private lateinit var binding:LoanGuarantedDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= LoanGuarantedDetailsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args= LoanGuarantedDetailsFragmentArgs.fromBundle(requireArguments()).loanGuaranteedDetails
        binding.apply {
            tvTransType.text=args.loanName
            textShareTT.text=args.name
            textName.text=args.memberNumber

            val amouyt= FormatDigit.formatDigits(args.amountRequested)
            binding.textMNo.text= String.format(getString(R.string.kesh),amouyt)
            val texte = args.percentageRequested
            textM0bileN.text="%.2f".format(texte)+"%"
            textStatus.text=args.status
        }
        setupNavUp()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.loans_guaranteed)
    }

}