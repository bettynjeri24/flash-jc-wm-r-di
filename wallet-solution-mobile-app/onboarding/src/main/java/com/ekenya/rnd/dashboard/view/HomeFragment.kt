package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.dashboard.adapters.*
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.*
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.dashboard.viewmodels.RoomDBViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentHomeBinding
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager

class HomeFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dashboardItemsRV: RecyclerView
    private lateinit var dashboardBillItemsRV: RecyclerView
    private lateinit var dashboardSendReceive: RecyclerView
    private lateinit var dashboardRecentTrans: RecyclerView
    private lateinit var viewModel: MobileWalletViewModel
    private lateinit var roomDBViewModel: RoomDBViewModel
    private lateinit var dashboardAdapter: DashboardAdapter
    private lateinit var recentTransactionsAdapter: RecentTransactionsAdapter
    private lateinit var dashboardItemsAdapter: DashboardItemsAdapter
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initAdapter()
        setSharedPreferences()
        setupViewModel()
        setUpViewPager()

        try {
            setObservers()
        } catch (e: Exception) {
        }
        initUI()

        return binding.root
    }

    private fun initUI() {
        dashboardItemsRV = binding.rvCard
        dashboardBillItemsRV = binding.rvDashboardItems
        dashboardRecentTrans = binding.rvRecentTrans
        dashboardSendReceive = binding.rvSendReceive

        dashboardRecentTrans.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        dashboardItemsRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        dashboardBillItemsRV.layoutManager = GridLayoutManager(requireContext(), 4)
        dashboardSendReceive.layoutManager = GridLayoutManager(requireContext(), 1)

        dashboardRecentTrans.adapter = recentTransactionsAdapter
        dashboardItemsRV.adapter = dashboardAdapter
        dashboardBillItemsRV.adapter = dashboardItemsAdapter

        dashboardItemsAdapter.setDashBoardItems(getDashBoardBillItems())
        recentTransactionsAdapter.setDashBoardItems(getRecentTransactions())

//        mGetWalletBalance(
//            LoginUserReq(
//                SharedPreferencesManager.getPhoneNumber(requireContext()).toString(),
//                "6405"
//            )
//        )
    }

    private fun initAdapter() {
        dashboardAdapter = DashboardAdapter(requireContext())
        recentTransactionsAdapter = RecentTransactionsAdapter()
        dashboardItemsAdapter = DashboardItemsAdapter(requireContext())
    }

    private fun setUpViewPager() {
        // viewPager = binding.viewPager

        val images = intArrayOf(
            R.drawable.promotion_image,
            R.drawable.promotion_image,
            R.drawable.promotion_image

        )

        binding.viewPager.adapter = ImagesAdapter(requireContext(), images)
        binding.viewPager.setInterval(2000)
        binding.viewPager.setDirection(AutoScrollViewPager.Direction.RIGHT)
        binding.viewPager.setCycle(true)
        binding.viewPager.setBorderAnimation(true)
        binding.viewPager.setSlideBorderMode(AutoScrollViewPager.SlideBorderMode.TO_PARENT)
        binding.viewPager.startAutoScroll()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    private fun setSharedPreferences() {
        SharedPreferencesManager.setisFirstTimeAtLanding(requireContext(), false)
        SharedPreferencesManager.setIsRegistereduser(requireContext(), true)
        SharedPreferencesManager.setHasFinishedSliders(requireContext(), true)
        SharedPreferencesManager.setHasReachedHomepage(requireContext(), true)
        SharedPreferencesManager.setIsRegistereduser(requireContext(), true)
        SharedPreferencesManager.setHasSetPin(requireContext(), 1)

        context?.let { SharedPreferencesManager.getnewToken(it) }
        binding.profileicon.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.profileFragment)
        }

        val firstName = SharedPreferencesManager.getFirstName(requireContext())
            ?.replaceFirstChar { it.uppercase() }
        val lastName = SharedPreferencesManager.getLastName(requireContext())
            ?.replaceFirstChar { it.uppercase() }
        binding.registeredname.text = "$firstName $lastName"
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
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
        roomDBViewModel.insertUser()

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ),
                ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)
    }

    private fun setObservers() {
        val token2 = context?.let { SharedPreferencesManager.getnewToken(it) }

        // getMinistatement(token2)

        roomDBViewModel.getUsers().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    // it.data?.first_name?.let { it1 -> toastMessage(it1) }
                    Log.d("TAG", "setObservers: usenamedb ${it.data?.first_name}")
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    // Handle Error
                    // it.message?.let { it1 -> toastMessage(it1) }
                    Log.d("TAG", "setObservers: " + it.message)
                }
            }
        }
    }

    private fun mGetWalletBalance(loginUserReq: LoginUserReq) {
        loginViewModel.getWalletBalance(loginUserReq).observe(viewLifecycleOwner) { myAPIResponse ->
            if (myAPIResponse.requestName == "WalletBalanceReq") {
                if (myAPIResponse.code == 200) {
                    val resp = myAPIResponse.responseObj as WalletBalanceResp
                    if (resp.status == 200) {
                        dashboardAdapter.setDashBoardItems(getDashBoardItems(resp.balance.toString()))
                    }
                }
            } else {
                Log.d("tagged", "else")
            }
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            setObservers()
            initUI()
        } catch (e: Exception) {
        }
    }

    fun getDashBoardItems(amount: String): List<DashboardItem> {
        val itemlist = ArrayList<DashboardItem>()
        itemlist.add(
            DashboardItem(
                R.color.cd_yellow,
                R.drawable.ic_mobilewallet,
                "Mobile Wallet",
                // walletBalance
                "GHS $amount"

            )
        )
        dashboardAdapter.notifyDataSetChanged()
        return itemlist
    }

    fun getRecentTransactions(): List<Transaction> {
        val itemlist = ArrayList<Transaction>()

        itemlist.add(
            Transaction(
                "Deposit to wallet from Ria money",
                "GHS",
                "12",
                "10/02/2022",
                "Receive Money",
                "C"
            )
        )
        itemlist.add(
            Transaction(
                "Bill payment for DSTV",
                "GHS",
                "10",
                "11/02/2022",
                "Bill Payment",
                "D"
            )
        )
        itemlist.add(
            Transaction(
                "Send Money through Dahabhill",
                "GHS",
                "990",
                "11/02/2022",
                "Send Money",
                "D"
            )
        )
        itemlist.add(
            Transaction(
                "Deposit to wallet through mobile mney",
                "GHS",
                "4800",
                "12/02/2022",
                "Wallet Deposit",
                "C"
            )
        )
        itemlist.add(
            Transaction(
                "Funds transfer to wallet 010101",
                "GHS",
                "300",
                "12/02/2022",
                "Send Money",
                "D"
            )
        )
        itemlist.add(
            Transaction(
                "Buy airtime for MTN from Wallet",
                "GHS",
                "2190",
                "13/02/2022",
                "Buy airtime",
                "D"
            )
        )

        return itemlist
    }

    fun getDashBoardBillItems(): List<DashboardBillItem> {
        val itemlist = ArrayList<DashboardBillItem>()

        itemlist.add(
            DashboardBillItem(
                R.drawable.ic_remittance_icon,
                "Cash\nPickup"
            )
        )

        itemlist.add(
            DashboardBillItem(
                R.drawable.ic_send_money,
                "Send\nMoney"
            )
        )

        itemlist.add(
            DashboardBillItem(
                R.drawable.ic_buy_airtime_icon,
                "Buy\nAirtime"
            )
        )

        itemlist.add(
            DashboardBillItem(
                R.drawable.ic_bill_payment_icon,
                "Bill\nPayment"
            )
        )
        itemlist.add(
            DashboardBillItem(
                R.drawable.ic_loyalty_program_icon,
                "Loyalty\nProgram"
            )
        )

        itemlist.add(
            DashboardBillItem(
                R.drawable.ic_growthservices,
                "Budget\n"
            )
        )

        return itemlist
    }
}
