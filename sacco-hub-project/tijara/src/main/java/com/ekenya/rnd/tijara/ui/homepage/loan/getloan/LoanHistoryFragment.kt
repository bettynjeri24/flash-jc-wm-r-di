package com.ekenya.rnd.tijara.ui.homepage.loan.getloan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ekenya.rnd.tijara.adapters.LoanProductHistoryAdapter
import com.ekenya.rnd.tijara.databinding.FragmentLoanHistoryBottomSheetBinding
import com.ekenya.rnd.tijara.requestDTO.LoanDetailsDTO
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.white_bg_spinkit.view.*

class LoanHistoryFragment : Fragment() {
    private lateinit var loanHistoryBinding: FragmentLoanHistoryBottomSheetBinding
    private lateinit var loanHistoryAdapter:LoanProductHistoryAdapter


    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(LoanProductViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loanHistoryBinding= FragmentLoanHistoryBottomSheetBinding.inflate(layoutInflater)
       // loanHistoryBinding.Spinkit.visibility=View.VISIBLE
       /* viewModel.status.observe(viewLifecycleOwner, Observer {
            if (null!=it){
                loanHistoryBinding.Spinkit.visibility=View.GONE
                when(it){
                    1->{
                        loanHistoryBinding.Spinkit.visibility=View.GONE
                    }
                    0->{
                        loanHistoryBinding.Spinkit.visibility=View.GONE

                    }
                }
            }
        })*/
        loanHistoryBinding.lifecycleOwner=this
        loanHistoryBinding.loanDetailsViewmodel=viewModel
        return loanHistoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val threeBounce: Sprite = ThreeBounce()
        loanHistoryBinding.apply {
       loanHistoryAdapter = LoanProductHistoryAdapter(requireContext(),LoanProductHistoryAdapter.OnClickListener{
           loanHistoryAdapter.notifyDataSetChanged()
        })
        rvHistory.adapter=loanHistoryAdapter
        rvHistory.layoutManager = GridLayoutManager(activity, 1)
    }
        val loanDetailsDTO=LoanDetailsDTO()
        loanDetailsDTO.productId=Constants.PRODUCTID
        viewModel.getLoanHistory(loanDetailsDTO)
        setupNavUp()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        loanHistoryBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        loanHistoryBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.loan_history)
    }


}