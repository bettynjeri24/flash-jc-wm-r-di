package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.DashBoardAccountAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.DashboardItemsAdapter
import com.ekenya.rnd.tijara.adapters.pagersadapter.DashboardAdsAdapter
import com.ekenya.rnd.tijara.databinding.FragmentDashboardBinding
import com.ekenya.rnd.tijara.network.model.Accounts
import com.ekenya.rnd.tijara.network.model.UserProfileList
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.ekenya.rnd.tijara.utils.autoPlayAdvertisement
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.toastySuccess
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.inject.Inject


@Suppress("DEPRECATION")
class DashboardFragment() : BaseDaggerFragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var dashboardAccountAdapter:DashBoardAccountAdapter

    private  var itemLists:ArrayList<UserProfileList> = ArrayList()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel:DashboardViewModel


    val allAccount:ArrayList<Accounts> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity?.window?.statusBarColor = Color.parseColor("#D7355E")
        }
        binding= FragmentDashboardBinding.inflate(layoutInflater)

        viewModel=ViewModelProvider(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)

        binding.lifecycleOwner=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserPhoto()
        addDashBoardItems()
        dashboardAccountAdapter= DashBoardAccountAdapter(requireContext(),allAccount)
        binding.pagerDashboard.adapter = dashboardAccountAdapter
        loadSliders()


        loadAdvs()
        //handleBackButton()

        binding.apply {
            val username= PrefUtils.getPreferences(requireContext(), "firstName")
            val time=PrefUtils.getPreferences(requireContext(), "logintime")!!
            loginTime.text = time
            tvUserName.setText(username)
            clMinistatement.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_statementAccountFragment)
            }
            btnDeposit.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_makeDepositFragment)
            }
            ivUser.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_userProfileFragment)
            }
            btnViewSummary.setOnClickListener {
                val myAccunt: Accounts = allAccount[binding.pagerDashboard.currentItem]
                viewModel.setAccountId(myAccunt.accountId)
                viewModel.setProductId(myAccunt.productId.toString())
                viewModel.setProductName(myAccunt.accountName)
                findNavController().navigate(R.id.action_dashboardFragment_to_accountSummaryFragment)
            }
            rvData.layoutManager = GridLayoutManager(requireContext(), 4)
            val dItemAdapter= DashboardItemsAdapter(itemLists,requireContext())
            rvData.adapter=dItemAdapter
        }
        viewModel.refresh.observe(viewLifecycleOwner,object : Observer<Boolean>{
            override fun onChanged(t: Boolean?) {
                if (t==true){
                    viewModel.getAccountsData()
                }
                //  toastySuccess(requireContext(),"TESTR TESTT $t")


            }

        })
    }
    private fun addDashBoardItems() {
        itemLists.add(UserProfileList(getString(R.string.my_shares), R.drawable.ic_buy_share))
        itemLists.add(UserProfileList(getString(R.string.send_money), R.drawable.ic_withdrawal))
        itemLists.add(UserProfileList(getString(R.string.bill_payment), R.drawable.ic_bill_payment))
        itemLists.add(UserProfileList(getString(R.string.buy_airtime), R.drawable.ic_but_airtime))
    }
    private fun loadAdvs(){
        val fragAd =
            DashboardAdsAdapter(
                requireContext()
            )
        binding.pager.adapter =fragAd
        fragAd.notifyDataSetChanged()
        autoPlayAdvertisement(binding.pager)

    }
    private fun loadSliders(){
        viewModel.accList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()){
                binding.view.makeGone()
            }
            allAccount.clear()
            allAccount.addAll(it)
            dashboardAccountAdapter.notifyDataSetChanged()
        })
        /**observe status code to know when to display error and empty page*/

        /*dashBoardAdapter.addFragment(BosaCardFragment())
        dashBoardAdapter.addFragment(CardLoanStatus())
        dashBoardAdapter.addFragment(CardSavingWallet())*/

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
                val myAccunt: Accounts = allAccount[position]
                /*Constants.ACCOUNTID = myAccunt.accountId
                Constants.SAVINGPRODUCTID = myAccunt.productId.toString()*/

            }
        })
    }

    private fun initUserPhoto(){
        val path = requireContext().getDir("imageDir", Context.MODE_PRIVATE)
        try {
            if (path==null){
                val drawable= AppCompatResources.getDrawable(requireContext(), R.drawable.user_pic)
                Glide.with(requireContext())
                    .load(drawable)
                    .into(binding.ivUser!!)
                binding.ivUser.setImageResource(R.drawable.user_pic)
            }else{
                loadImageFromStorage()
            }
        }catch (e: Exception){
            Timber.d("LOADING IMAGE ERROR ${e.message}")
        }
    }
    private fun loadImageFromStorage() {
        try {
            val path = requireContext().getDir("imageDir", Context.MODE_PRIVATE)

            val file = File(path, "profile.png")
            val bitmap = BitmapFactory.decodeStream(FileInputStream(file))

            binding.ivUser.setImageBitmap(bitmap)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            if (getFragmentManager() != null) {
                getFragmentManager()
                    ?.beginTransaction()
                    ?.detach(this)
                    ?.attach(this)
                    ?.commit();
            }
        }
    }




}