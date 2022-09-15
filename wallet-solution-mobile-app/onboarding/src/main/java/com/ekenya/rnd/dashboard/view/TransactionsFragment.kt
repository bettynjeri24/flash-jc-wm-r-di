package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.dashboard.adapters.TransactionItemsAdapter
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentTransactionsBinding
import java.util.*


class TransactionsFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private lateinit var transactionsRv: RecyclerView
    private lateinit var viewModel: MobileWalletViewModel
    private val transactionItemsAdapter = TransactionItemsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)

        initUI()
        setupViewModel()
        setObservers()
        setonclicklisteners()

        return binding.root
    }

    private fun setonclicklisteners() {
        binding.imgTopup.setOnClickListener {
            findNavController().navigate(R.id.action_transactionsfragments_totopWallet)
        }
        binding.imgWithdraw.setOnClickListener {
            findNavController().navigate(R.id.action_transactionsfragments_toWithdrawFragment)
        }
        binding.imgBillpayment.setOnClickListener {
            findNavController().navigate(R.id.action_transactionsfragments_toBillpayments)
        }
        binding.imgSendmoney.setOnClickListener {
            findNavController().navigate(R.id.action_transactionsfragments_toSendMoney)
        }
        binding.imgPaymerchant.setOnClickListener {
            findNavController().navigate(R.id.action_transactionsfragments_toPayMerchant)
        }
        binding.exportStatement.setOnClickListener {
            findNavController().navigate(R.id.exportStatementsFragment)
        }

    }

    private fun initUI() {
        binding.exportStatement.changeBackgroundColor(com.ekenya.rnd.walletbaseapp.R.color.app_mustard)

        val indexOfMonth = Calendar.getInstance().get(Calendar.MONTH)

        val currencyAdapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.month, R.layout.list_item
        )
        currencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        binding.monthSpinner.setSelection(indexOfMonth);
        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position != indexOfMonth) {
                    binding.tvNotransactions.makeVisible()
                    binding.rvTransactions.makeInvisible()
                } else {
                    binding.tvNotransactions.makeInvisible()
                    binding.rvTransactions.makeVisible()

                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                binding.tvNotransactions.makeInvisible()
                binding.rvTransactions.makeVisible()
            }
        }



        transactionsRv = binding.rvTransactions
        transactionsRv.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        transactionsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(MobileWalletViewModel::class.java)
    }


    private fun setObservers() {
        val data = MainDataObject(DashBoardUtils.getMinistatementRequestData(context))
        val token = context?.let { SharedPreferencesManager.getnewToken(it) }
        viewModel.getMiniStatement(token!!, data).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        if (it.data?.data?.response?.response_code.equals("00")) {
                            if (it.data?.data?.response?.mini_statement?.size!! > 0) {
                                transactionsRv.visibility = View.VISIBLE
                                binding.exportStatement.visibility= View.VISIBLE

                                binding.tvNotransactions.visibility = View.INVISIBLE
                                transactionItemsAdapter.sendTransactions(it.data!!.data.response.mini_statement)
                                transactionsRv.adapter = transactionItemsAdapter

                            } else {
                                binding.tvNotransactions.visibility = View.VISIBLE
                                binding.exportStatement.visibility= View.INVISIBLE

                            }
                        } else {
                            binding.tvNotransactions.visibility = View.VISIBLE

                            Toast.makeText(
                                context,
                                it.data!!.data.response.response_message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    Status.ERROR -> {

                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                    }
                    Status.LOADING -> {

                        // Toast.makeText(context, "loading", Toast.LENGTH_LONG).show()

                    }
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        showSupportActionBar()
    }

    override fun onResume() {
        super.onResume()
        showSupportActionBar()
    }


}