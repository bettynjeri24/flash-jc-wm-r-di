package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentAirlineTicketingBinding


class BusTicketingFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentAirlineTicketingBinding
    private lateinit var viewModel: MobileWalletViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel
    private var adults: Int = 0
    private var seats = 0
    private var children: Int = 0
    private var luggage: String = "No"
    private lateinit var date: String
    private var buyforSelf = true
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var selectedValue: String
    private var bustDestinations = listOf("Ethiopian Airline", "Kenya Airways")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAirlineTicketingBinding.inflate(inflater, container, false)
        initUi()

        initViewModel()
        setOnspinnerchnageListerners()

        return binding.root
    }

    private fun setOnspinnerchnageListerners() {


        binding.btnContinue.setOnClickListener {
            if (!selectedValue.isNullOrBlank()) {


            }else{
                toastMessage("Select One")
            }
        }
        (binding.regionspinner).onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedValue = adapter.getItem(position).toString()


            }

    }


    private fun initViewModel() {
        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.DSTV_PAYMENTS_FRAGMENT)
    }

    private fun initUi() {

        adapter = ArrayAdapter(requireContext(), R.layout.list_item, bustDestinations)
        selectedValue = binding.regionspinner.text.toString()
        (binding.regionspinner as? AutoCompleteTextView)?.setAdapter(adapter)


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