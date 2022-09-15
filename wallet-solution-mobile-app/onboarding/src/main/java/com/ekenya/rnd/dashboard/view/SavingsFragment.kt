package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.adapters.SavingsAccountAdapter
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsAccountData
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsItem
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.changeBackgroundColor
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.dashboard.viewmodels.RoomDBViewModel
import com.ekenya.rnd.dashboard.viewmodels.SavingsViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.SavingsBottomsheetBinding
import com.ekenya.rnd.onboarding.databinding.SavingsFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SavingsFragment : BaseDaggerFragment() {


    private lateinit var roomDBViewModel: RoomDBViewModel
    private lateinit var savingsViewModel: SavingsViewModel
    private lateinit var mobileWalletViewModel: MobileWalletViewModel
    private lateinit var binding: SavingsFragmentBinding
    private lateinit var adapter: SavingsAccountAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SavingsFragmentBinding.inflate(inflater, container, false)


        initViewModel()
        setClickListeners()
        savingsViewModel.setAccountType("Personal Savings")
        setObservers()
        initUi()

        return binding.root
    }

    private fun setObservers() {


      val  token = SharedPreferencesManager.getnewToken(requireContext())!!
        val savingsPayload = SavingsAccountData(
            phone_number = SharedPreferencesManager.getPhoneNumber(requireContext())!!
        )


        mobileWalletViewModel.getSavingsAccounts(
            token,
            MainDataObject(savingsPayload)
        )
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            val responseCode = it.data?.data?.response?.response_code
                            toastMessage("$responseCode")
                            when (responseCode) {
                                "401" -> {
                                    toastMessage("Session Expired Login to Continue")
                                    findNavController().navigate(R.id.loginFragment2)

                                }
                                "00" -> {
                                    toastMessage("${it.data!!.data.savings}")
                                    if (it.data!!.data.savings.isNotEmpty()) {
                                        SharedPreferencesManager.setNumberOfSavingsAccount(
                                            requireContext(),
                                            it.data!!.data.savings.size.toString()

                                        )
                                        binding.tvNotransactions.visibility = View.GONE
                                        renderList(it.data!!.data.savings)
                                        toastMessage("${it.data!!.data.savings.get(0).target_amount}")

                                        /*it.data?d.let { users ->
                                            renderList(users)
                                        }*/
                                        binding.rvSavingsAccounts.visibility = View.VISIBLE

                                    }


                                }
                                "000" -> {
                                }
                                else -> {


                                }
                            }


                            //findNavController().popBackStack()
                        }
                        Status.ERROR -> {

                        }
                        Status.LOADING -> {


                        }
                    }
                }
            })



        /*roomDBViewModel.getSavingsAccounts().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.isNotEmpty()) {
                        SharedPreferencesManager.setNumberOfSavingsAccount(
                            requireContext(),
                            it.data!!.size.toString()

                        )
                        binding.tvNotransactions.visibility = View.GONE
                        it.data?.let { users ->
                            renderList(users)
                        }
                        binding.rvSavingsAccounts.visibility = View.VISIBLE

                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //Handle Error
                    it.message?.let { it1 -> toastMessage(it1) }
                    Log.d("TAG", "setObservers: " + it.message)
                }
            }
        })*/

    }

    private fun renderList(savingsAccounts: List<SavingsItem>) {
        adapter.addData(savingsAccounts)
        adapter.notifyDataSetChanged()
    }

    private fun initViewModel() {
        savingsViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(SavingsViewModel::class.java)

        mobileWalletViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(MobileWalletViewModel::class.java)

        roomDBViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(RoomDBViewModel::class.java)
    }

    private fun setClickListeners() {

        binding.addSavingsAccount.setOnClickListener {
            //display bottomsheet
            showDialog()
        }
    }

    private fun initUi() {


        binding.addSavingsAccount.changeBackgroundColor(com.ekenya.rnd.walletbaseapp.R.color.app_mustard)

        binding.rvSavingsAccounts.layoutManager = LinearLayoutManager(requireContext())
        adapter =
            SavingsAccountAdapter(
                arrayListOf()
            )

        binding.rvSavingsAccounts.adapter = adapter


    }

    private fun showDialog() {
        val bottomsheetlayoutBinding = SavingsBottomsheetBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        dialog.setContentView(bottomsheetlayoutBinding.root)
        dialog.show()


        bottomsheetlayoutBinding.cardViewPersonalSavingsAccount.setOnClickListener {
            savingsViewModel.setAccountType(getString(R.string.personal_savings_text))
            findNavController().navigate(R.id.action_savings_addsavingsAccount)

            dialog.dismiss()

        }
        bottomsheetlayoutBinding.cardviewGroupSavings.setOnClickListener {

            savingsViewModel.setAccountType(getString(R.string.group_savings_txt))
            toastMessage("Feature Coming Soon")
            //findNavController().navigate(R.id.action_savings_addsavingsAccount)

            //dialog.dismiss()
        }


    }

    override fun onResume() {
        super.onResume()
        showSupportActionBar()
        setObservers()
    }


}