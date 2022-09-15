package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.dashboard.adapters.TicketingAdapter
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.onboarding.databinding.FragmentAvailableTicketsBinding


class AvailableTicketsFragment : Fragment() {
    private lateinit var binding:FragmentAvailableTicketsBinding
    private lateinit var availableTicketsRv: RecyclerView
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var ticketingAdapter:TicketingAdapter
    private lateinit var destination1:String
    private lateinit var destination2:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAvailableTicketsBinding.inflate(inflater,container,false)

        initUi()
        initViewModel()
        initObservers()
        initAdapter()
        return binding.root
    }

    private fun initObservers() {
        authorizeTransactionViewModel.spinnerOption1.observe(viewLifecycleOwner,{
            destination1=it

        })
        authorizeTransactionViewModel.spinnerOption2.observe(viewLifecycleOwner,{
            destination2=it
        })
    }

    private fun initAdapter() {
       ticketingAdapter = TicketingAdapter(this,"Addis Ababa","Mekelle")

        availableTicketsRv.adapter =ticketingAdapter
    }

    private fun initViewModel() {
        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)
        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)

        confirmSendingMoneyViewModel.setRequestingFragment(Constants.TICKETING_FRAGMENT)
        authorizeTransactionViewModel.setRequestingFragment(Constants.TICKETING_FRAGMENT)
    }

    private fun initUi() {
        availableTicketsRv = binding.availableTickets
        availableTicketsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


    }


}