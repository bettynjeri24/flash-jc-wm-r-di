package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.layoutAdapter.DashboardItemsAdapter
import com.ekenya.rnd.tijara.adapters.pagersadapter.CardHomeAdapter
import com.ekenya.rnd.tijara.adapters.pagersadapter.DashboardAdsAdapter
import com.ekenya.rnd.tijara.databinding.FragmentHomeMainBinding
import com.ekenya.rnd.tijara.network.model.Accounts
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.changepassword.PinViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.AccountsCallBack
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import javax.inject.Inject

class HomeMainFragment : BaseDaggerFragment(),AccountsCallBack {
    private lateinit var binding:FragmentHomeMainBinding
    private var isAuthenticated:Boolean=false
    private var position=-1
    private lateinit var cardHomeAdapter: CardHomeAdapter
    private lateinit var dItemAdapter: DashboardItemsAdapter
    private  var itemLists:ArrayList<UserProfileList> = ArrayList()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val pinViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(PinViewModel::class.java)
    }
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)    }
    val allAccount:ArrayList<Accounts> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeMainBinding.inflate(layoutInflater)
        cardHomeAdapter= CardHomeAdapter(requireContext(),allAccount,this)
        binding.pagerDashboard.adapter=cardHomeAdapter

        loadAdvs()
        binding.apply{
            itemLists.clear()
            dItemAdapter= DashboardItemsAdapter(itemLists,requireContext())
            rvData.adapter=dItemAdapter
            rvData.layoutManager = GridLayoutManager(requireContext(), 4)
        }
        addDashBoardMenus()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG","${Constants.isSacco}")
        pinViewModel.authSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                pinViewModel.unsetAuthSuccess()
                isAuthenticated = true
                showBalance()
                pinViewModel.stopObserving()
            }
        }
        val username= PrefUtils.getPreferences(requireContext(), "firstName")
        val time= PrefUtils.getPreferences(requireContext(), "logintime")!!
        binding.apply {
            tvSacco.text= String.format(getString(R.string.welcome_user),username)
            loginTime.text= String.format(getString(R.string.last_logged),time)
            tvWelcome.text= camelCase(Constants.SaccoName)
            pagerDashboard.setPadding(dpToPx(10, requireContext()), 0, dpToPx(20, requireContext()), 0)
            pagerDashboard.clipToPadding = false
            pagerDashboard.pageMargin = dpToPx(2, requireContext())

        }
        viewModel.refresh.observe(viewLifecycleOwner,object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t==true){
                    viewModel.getAccountsData()
                }
            }

        })
        loadSavingSlider()
        binding.cardLoader.makeVisible()
        viewModel.statusCode.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    1 -> {
                        binding.cardLoader.makeGone()
                    }
                    2 -> {
                        binding.cardLoader.makeGone()
                    }
                    else -> {
                        binding.cardLoader.makeGone()
                    }
                }
            }
        }

    }
    private fun loadSavingSlider(){
        viewModel.accList.observe(viewLifecycleOwner, Observer {
            allAccount.clear()
            allAccount.addAll(it)
            cardHomeAdapter.notifyDataSetChanged()
        })
        /**observe status code to know when to display error and empty page*/
        binding.tabDotsDash.setupWithViewPager(binding.pagerDashboard, true)

        binding.pagerDashboard.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }
    private fun addDashBoardMenus() {
        if (Constants.isSacco==true){
            itemLists.add(UserProfileList(getString(R.string.buy_airtime), R.drawable.h_airtime))
            itemLists.add(UserProfileList(getString(R.string.send_money), R.drawable.h_send))
            itemLists.add(UserProfileList(getString(R.string.bill_payment), R.drawable.h_bill))
            itemLists.add(UserProfileList(getString(R.string.make_deposit), R.drawable.h_deposit))
            itemLists.add(UserProfileList(getString(R.string.my_loans), R.drawable.h_loan))
            itemLists.add(UserProfileList(getString(R.string.saving), R.drawable.h_saving))
            itemLists.add(UserProfileList(getString(R.string.my_shares), R.drawable.h_shares))
            itemLists.add(UserProfileList(getString(R.string.other_services), R.drawable.h_others))

        }else {
            itemLists.add(UserProfileList(getString(R.string.buy_airtime), R.drawable.h_airtime))
            itemLists.add(UserProfileList(getString(R.string.send_money), R.drawable.h_send))
            itemLists.add(UserProfileList(getString(R.string.bill_payment), R.drawable.h_bill))
            itemLists.add(UserProfileList(getString(R.string.make_deposit), R.drawable.h_deposit))
            itemLists.add(UserProfileList(getString(R.string.my_loans), R.drawable.h_loan))
            itemLists.add(UserProfileList(getString(R.string.saving), R.drawable.h_saving))
            itemLists.add(UserProfileList(getString(R.string.other_services), R.drawable.h_others))
            //   itemLists.add(UserProfileList(getString(R.string.my_shares), R.drawable.h_shares))
        }
    }
    fun dpToPx(dp: Int, context: Context): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    private fun loadAdvs(){
        val fragAd =
            DashboardAdsAdapter(
                requireContext()
            )
        binding.pager.adapter =fragAd
        fragAd.notifyDataSetChanged()
        autoPlayAdvertisement(binding.pager)

    }

    override fun onItemSelected(accounts: Accounts, pos: Int) {
        position=pos
        if (Constants.isMobileMClicked==true){
            if (accounts.showBalance){
                hideBalance()
            }else{
                if (isAuthenticated){
                    showBalance()
                }else{
                    findNavController().navigate(R.id.action_homeMainFragment_to_pinFragment)
                }
            }
        }else{
            Constants.SAVINGAID= accounts.accountId
            Constants.SAVINGPID= accounts.productId.toString()
            Constants.SAVINGACCOUNTNAME= accounts.accountName
            // Constants.SAVINGACCOUNTNO= accounts.accountNo
            findNavController().navigate(R.id.action_homeMainFragment_to_selectStatementFragment)
        }



    }
    fun showBalance(){
        val item=allAccount.removeAt(position)
        item.showBalance=true
        allAccount.add(position,item)
        cardHomeAdapter.notifyDataSetChanged()
    }
    fun hideBalance(){
        val item=allAccount.removeAt(position)
        item.showBalance=false
        allAccount.add(position,item)
        cardHomeAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
    }


}