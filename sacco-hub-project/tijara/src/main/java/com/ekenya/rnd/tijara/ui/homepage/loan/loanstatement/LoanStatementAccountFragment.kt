package com.ekenya.rnd.tijara.ui.homepage.loan.loanstatement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.SelectLoanAccountAdapter
import com.ekenya.rnd.tijara.databinding.FragmentLoanStatementAccountBinding
import com.ekenya.rnd.tijara.network.model.ActivesLoan
import com.ekenya.rnd.tijara.utils.callbacks.SelectLoanCallBack
import com.ekenya.rnd.tijara.utils.makeVisible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.white_bg_spinkit.view.*

class LoanStatementAccountFragment : Fragment(),SelectLoanCallBack {
    private lateinit var adapter:SelectLoanAccountAdapter
    private val accList:ArrayList<ActivesLoan> = arrayListOf()
    private lateinit var binding:FragmentLoanStatementAccountBinding
    private lateinit var viewModel: LoanProductStatementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoanStatementAccountBinding.inflate(layoutInflater)
        adapter= SelectLoanAccountAdapter(requireContext(),accList,this)
        binding.rvAccounts.adapter=adapter
        binding.rvAccounts.layoutManager=GridLayoutManager(requireContext(),1)
        viewModel= ViewModelProvider(requireActivity()).get(LoanProductStatementViewModel::class.java)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //  progressSpinkit.visibility=View.VISIBLE

        }
        viewModel.activeLoan.observe(viewLifecycleOwner,  {
            accList.clear()
            accList.addAll(it)
            binding.rvAccounts.adapter?.notifyDataSetChanged()

            /* if (it.isEmpty()){
                 binding.noShows.makeVisible()
             }*/

        })

        /* viewModel.statusCode.observe(viewLifecycleOwner,  {
             if (null!=it){
                 binding.progressSpinkit.visibility=View.GONE
                 when(it){
                     1->{
                         binding.progressSpinkit.visibility=View.GONE
                     }
                 }
             }
         })*/
        setupNavUp()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.loan_statements)
    }

    override fun onItemSelected(item: ActivesLoan) {
        viewModel.setProductName(item.name)
        viewModel.setAccountId(item.loanId)
        findNavController().navigate(R.id.action_loanStatementAccountFragment_to_LoanProductStatementFragment)
    }


}