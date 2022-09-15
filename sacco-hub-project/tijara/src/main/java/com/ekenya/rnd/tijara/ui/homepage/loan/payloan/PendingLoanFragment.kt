package com.ekenya.rnd.tijara.ui.homepage.loan.payloan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.ActiveLoanAdapter
import com.ekenya.rnd.tijara.adapters.PendingLoanAdapter
import com.ekenya.rnd.tijara.databinding.FragmentActiveLoanBinding
import com.ekenya.rnd.tijara.databinding.PendingLoanFragmentBinding
import com.ekenya.rnd.tijara.utils.FormatDigit
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class PendingLoanFragment : Fragment() {
   private lateinit var activeLoanBinding: PendingLoanFragmentBinding
   private lateinit var viewModel: ActiveLoanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activeLoanBinding= PendingLoanFragmentBinding.inflate(layoutInflater)
        viewModel= ViewModelProviders.of(requireActivity()).get(ActiveLoanViewModel::class.java)
        activeLoanBinding.lifecycleOwner=this
        activeLoanBinding.loanProductViewModel=viewModel
        activeLoanBinding.apply {
            activeLoanBinding.rvLoanAdapter.adapter= PendingLoanAdapter(PendingLoanAdapter.OnClickListener {
                Constants.GENERAL_AMOUNT= FormatDigit.splitTwoString(it.loanBalance.toString())
            },requireContext())
            activeLoanBinding.rvLoanAdapter.layoutManager = GridLayoutManager(requireContext(),1)

        }
        return activeLoanBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupNavUp()
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        activeLoanBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        activeLoanBinding.toolbar.custom_toolbar.custom_toolbar_title.text  ="Pending Disbursement"
    }


}