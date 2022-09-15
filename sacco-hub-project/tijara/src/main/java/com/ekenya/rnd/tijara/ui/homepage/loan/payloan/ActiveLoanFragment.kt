package com.ekenya.rnd.tijara.ui.homepage.loan.payloan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.ActiveLoanAdapter
import com.ekenya.rnd.tijara.databinding.FragmentActiveLoanBinding
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.ui.homepage.loan.getloan.LoanProductViewModel
import com.ekenya.rnd.tijara.utils.FormatDigit
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ActiveLoanFragment : Fragment() {
   private lateinit var activeLoanBinding: FragmentActiveLoanBinding
   private lateinit var viewModel: ActiveLoanViewModel
    private val loanPViewModel: LoanProductViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activeLoanBinding= FragmentActiveLoanBinding.inflate(layoutInflater)
        viewModel= ViewModelProvider(requireActivity()).get(ActiveLoanViewModel::class.java)
        activeLoanBinding.lifecycleOwner=this
        activeLoanBinding.loanProductViewModel=viewModel
        activeLoanBinding.apply {
            activeLoanBinding.rvLoanAdapter.adapter= ActiveLoanAdapter(ActiveLoanAdapter.OnClickListener {
                loanPViewModel.amountBorrowed.value= FormatDigit.splitTwoString(it.loanBalance.toString())
            },requireContext())
            activeLoanBinding.rvLoanAdapter.layoutManager = GridLayoutManager(requireContext(),1)

        }
        return activeLoanBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        viewModel.responseStatus.observe(viewLifecycleOwner) {
            if (null != it) {
                when (it) {
                    GeneralResponseStatus.LOADING -> {
                        activeLoanBinding.progressSpinkit.visibility = View.VISIBLE
                    }
                    GeneralResponseStatus.DONE -> {
                        activeLoanBinding.progressSpinkit.visibility = View.GONE
                    }
                    else -> {
                        activeLoanBinding.progressSpinkit.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        activeLoanBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        activeLoanBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.active_loans)
    }


}