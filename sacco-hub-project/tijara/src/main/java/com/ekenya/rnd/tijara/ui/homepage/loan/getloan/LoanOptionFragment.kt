package com.ekenya.rnd.tijara.ui.homepage.loan.getloan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ekenya.rnd.tijara.adapters.layoutAdapter.HasLoanAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.NoLoanAdapter
import com.ekenya.rnd.tijara.databinding.FragmentLoanOptionBinding
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import com.ekenya.rnd.tijara.utils.toastySuccess
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.white_bg_spinkit.view.*
import javax.inject.Inject

class LoanOptionFragment : BaseDaggerFragment() {
    private lateinit var loanOptionBinding: FragmentLoanOptionBinding
    private var canPayLoan:Boolean=false
       @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(LoanProductViewModel::class.java)
    }
    private val items:ArrayList<UserProfileList> = ArrayList()
    private val noLoanitems:ArrayList<UserProfileList> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loanOptionBinding= FragmentLoanOptionBinding.inflate(layoutInflater)
        loanOptionBinding.apply {
            viewModel.canPay.observe(viewLifecycleOwner){
                canPayLoan=it
                if (it==true){
                    textPlaceholder.text=getString(R.string.your_active_loan_summary_details)
                    loanOptionBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(
                        R.string.loan_options)
                    layoutNoLoan.makeGone()
                    noLoanShows.makeGone()
                    LayoutCantTopUp.makeGone()
                    LayoutHasLoan.makeVisible()
                    clActiveLoan.makeVisible()
                }
            }
            viewModel.canApply.observe(viewLifecycleOwner){
                if (it==true){
                    layoutNoLoan.makeVisible()
                    noLoanShows.makeVisible()
                    LayoutCantTopUp.makeGone()
                    clActiveLoan.makeGone()
                    LayoutHasLoan.makeGone()
                    loanTitleText.text =
                        "YOU DO NOT HAVE ACTIVE ${Constants.PRODUCTNAME}\n IN YOUR PROFILE"
                    loanOptionBinding.toolbar.custom_toolbar.custom_toolbar_title.text =
                        getString(R.string.loan_request)
                }
            }
            viewModel.hasActiveLoan.observe(viewLifecycleOwner){
                if (it==true && canPayLoan){
                    layoutNoLoan.makeGone()
                    noLoanShows.makeGone()
                    LayoutCantTopUp.makeGone()
                    LayoutHasLoan.makeVisible()
                    clActiveLoan.makeVisible()

                }else if (it==true && !canPayLoan){
                    noLoanShows.makeGone()
                    clActiveLoan.makeVisible()
                    LayoutCantTopUp.makeVisible()
                    LayoutHasLoan.makeGone()
                    layoutNoLoan.makeGone()
                }else{
                    noLoanShows.makeVisible()
                    clActiveLoan.makeGone()
                    LayoutCantTopUp.makeGone()
                    layoutNoLoan.makeVisible()
                    LayoutHasLoan.makeGone()
                }
            }
        }
        loanOptionBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(
            R.string.loan_options)
       loanOptionBinding.apply {
           CVLoanHistoryLoan.setOnClickListener {
               findNavController().navigate(R.id.action_loanOptionFragment_to_loanHistoryBottomSheetFragment)
           }
           val args= LoanOptionFragmentArgs.fromBundle(requireArguments()).activeLoansResponse
           if (args!=null){
               tvTotalAmountApplied.text=args.amountApplied
               tvAmountDisbursed.text=args?.amountDisbursed
               tvPaidAmount.text=args?.amountRepaid
               tvLoanBal.text=args?.loanBalance
               tvIAmount.text=args?.interestAmount
               Constants.LOANID=args.loanId
               viewModel.amountBorrowed.value= FormatDigit.splitTwoString(args?.loanBalance.toString())
           }
       }
        loanOptionBinding.loanDetailsViewmodel=viewModel
        loanOptionBinding.lifecycleOwner=this
        return loanOptionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loanOptionBinding.apply {

            rvHasLoan.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            val hasLoanAdapter= HasLoanAdapter(items)
            rvHasLoan.adapter=hasLoanAdapter
            rvNoLoan.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            val noLoanAdapter= NoLoanAdapter(noLoanitems)
            rvNoLoan.adapter=noLoanAdapter
        }
        addHasLoanItems()
        addNoLoanItems()
        setupNavUp()
    }
    private fun addHasLoanItems() {
        items.add(UserProfileList(getString(R.string.pay_loan), R.drawable.arrow_right))
        items.add(UserProfileList(getString(R.string.loan_history), R.drawable.arrow_right))
    }
    private fun addNoLoanItems() {
        noLoanitems.add(UserProfileList(getString(R.string.request_loan), R.drawable.arrow_right))
        noLoanitems.add(UserProfileList(getString(R.string.loan_history), R.drawable.arrow_right))
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        loanOptionBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)

    }


}