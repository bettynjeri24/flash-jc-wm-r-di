package com.ekenya.rnd.tijara.ui.homepage.loan.loanstatement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.LoanSummaryAdapter
import com.ekenya.rnd.tijara.databinding.FragmentLoanProductStatementBinding
import com.ekenya.rnd.tijara.network.model.LoanStatement
import com.ekenya.rnd.tijara.utils.camelCase
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class LoanProductStatementFragment : Fragment() {
    private lateinit var binding: FragmentLoanProductStatementBinding
    private lateinit var loanSummaryAdapter:LoanSummaryAdapter
    private  var loanList:ArrayList<LoanStatement> = arrayListOf()


    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoanProductStatementViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoanProductStatementBinding.inflate(layoutInflater)
        binding.apply {
            loanSummaryAdapter = LoanSummaryAdapter(loanList,requireContext())
            rvAccSummary.adapter =loanSummaryAdapter
            rvAccSummary.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.summaryViewmodel=viewModel
        binding.lifecycleOwner=this
        viewModel.transactionLoanDetails.observe(viewLifecycleOwner,{
            loanList.clear()
            loanList.addAll(it)
            loanSummaryAdapter.notifyDataSetChanged()
        })
        viewModel.getLoanStatement()
        // binding.whiteBgLoading.makeVisible()

        /*viewModel.status.observe(viewLifecycleOwner,{
            if (it!==null){
                when(it){
                    1->{
                        binding.whiteBgLoading.makeGone()
                    }
                    0->{
                        binding.whiteBgLoading.makeGone()

                    }
                    else->{
                        binding.whiteBgLoading.makeGone()

                    }
                }
            }
        })*/
        setupNavUp()
        viewModel.accountName.observe(viewLifecycleOwner,{
            binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.loan_ministatement,
                camelCase(it))
        })

    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}