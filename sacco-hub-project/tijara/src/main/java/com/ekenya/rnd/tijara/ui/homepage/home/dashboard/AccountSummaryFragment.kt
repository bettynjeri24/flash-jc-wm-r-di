package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.AccountSummaryAdapter
import com.ekenya.rnd.tijara.databinding.AccountSummaryFragmentBinding
import com.ekenya.rnd.tijara.network.model.TransactionDetails
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import javax.inject.Inject

class AccountSummaryFragment : BaseDaggerFragment() {
    lateinit var accountBinding: AccountSummaryFragmentBinding
    private lateinit var accountSummaryAdapter:AccountSummaryAdapter
    private val transList:ArrayList<TransactionDetails> = arrayListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountBinding= AccountSummaryFragmentBinding.inflate(layoutInflater)
        accountSummaryAdapter = AccountSummaryAdapter(transList)
        accountBinding.rvAccSummary.adapter =accountSummaryAdapter
        setupNavUp()
        return accountBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountBinding.lifecycleOwner=this
        accountBinding.accountSummaryViewModel=viewModel
        accountBinding.apply {

            viewModel.transactionDetails.observe(viewLifecycleOwner, Observer {
                transList.clear()
              transList.addAll(it)
              accountSummaryAdapter.notifyDataSetChanged()
            })

            viewModel.getTransactionSummary()
        }
        viewModel.accountName.observe(viewLifecycleOwner, Observer {
            accountBinding.toolbar.custom_toolbar.custom_toolbar_title.text  =getString(R.string.acc_summary,it)
        })
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        accountBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)

    }

}