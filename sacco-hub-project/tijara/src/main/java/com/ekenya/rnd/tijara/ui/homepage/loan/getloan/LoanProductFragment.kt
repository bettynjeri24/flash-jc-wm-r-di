package com.ekenya.rnd.tijara.ui.homepage.loan.getloan

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.LoanProductAdapter
import com.ekenya.rnd.tijara.databinding.LoanProductFragmentBinding
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.utils.FormatDigit.Companion.formatDigits
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import javax.inject.Inject

class LoanProductFragment : BaseDaggerFragment() {
    private lateinit var binding: LoanProductFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(LoanProductViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity?.window?.statusBarColor = Color.parseColor("#D7355E")
        }
        binding = LoanProductFragmentBinding.inflate(layoutInflater)
        binding.progressSpinkit.visibility=View.VISIBLE

        viewModel.responseStatus.observe(viewLifecycleOwner) {
            if (null != it) {
                binding.progressSpinkit.visibility = View.GONE
                when (it) {
                    GeneralResponseStatus.LOADING -> {
                        binding.progressSpinkit.visibility = View.VISIBLE
                    }
                    GeneralResponseStatus.DONE -> {
                        binding.progressSpinkit.visibility = View.GONE
                    }
                    GeneralResponseStatus.ERROR -> {
                        binding.progressSpinkit.visibility = View.GONE
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner=this
        binding.loanProductViewModel=viewModel
        setupNavUp()
        handleBackButton()
        binding.apply {
            val loanProductAdapter=LoanProductAdapter(LoanProductAdapter.OnClickListener {
                val directions=
                    LoanProductFragmentDirections.actionLoanProductFragmentToLoanOptionFragment(it.activeLoan)
                findNavController().navigate(directions)
                viewModel.loanLimit.value=formatDigits(it.limit)
                viewModel.productId.value=formatDigits(it.approvalThreshhold)
                viewModel.productName.value=formatDigits(it.approvalThreshhold)
                Constants.PRODUCTID = it.productId.toString()
                Constants.PRODUCTNAME = it.name
                viewModel.canApply.postValue(it.canApply)
                viewModel.canPay.postValue(it.canRepay)
                viewModel.hasActiveLoan.postValue(it.hasActiveLoan)
                viewModel.mustGuaranteed.postValue(it.mustBeGuarateed)
            })
            binding.rvLoanAdapter.layoutManager = GridLayoutManager(requireActivity(), 1)
            binding.rvLoanAdapter.adapter=loanProductAdapter
        }
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.loan_products)
    }

    private fun handleBackButton() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                    //findNavController().navigate(R.id.action_serviceFragment_to_dashboardFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }


}