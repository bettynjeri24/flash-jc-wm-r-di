package com.ekenya.rnd.dashboard.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.dashboard.adapters.TransactionItemsAdapter
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.MonthData
import com.ekenya.rnd.dashboard.datadashboard.model.StatisticPayload
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.dashboard.viewmodels.RoomDBViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.MobileWalletFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class MobileWalletFragment : BaseDaggerFragment() {
    private lateinit var roomDBViewModel: RoomDBViewModel
    private lateinit var binding: MobileWalletFragmentBinding
    private lateinit var viewmodel: LoginViewModel
    private lateinit var lineChart: LineChart
    private var topUpList = ArrayList<MonthData>()
    private var withdrawList = ArrayList<MonthData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = MobileWalletFragmentBinding.inflate(inflater, container, false)
        lineChart = binding.graph
        setupViewModel()
        initUi()
        setupObservers()
        setclicklisteners()


        val adapter = TransactionItemsAdapter()
        // adapter.sendTransactions(AppData.gettransactionItems())


        val rv = binding.rvTransactions
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter






        return binding.root
    }

    private fun setclicklisteners() {
        binding.closeIcon.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setupObservers() {
        binding.tvAmount.text = SharedPreferencesManager.getAccountBalance(requireContext())
        val data = StatisticPayload(
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            SharedPreferencesManager.getAccountNumber(requireContext())!!,

            "cash_flow"
        )
        viewmodel.getStatistics(data)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {

                            when (it.data?.status) {
                                0 -> {
                                    topUpList =
                                        it.data!!.data.money_in.reversed() as ArrayList<MonthData>
                                    withdrawList =
                                        it.data!!.data.money_out.reversed() as ArrayList<MonthData>
                                    loadLineGraph()

                                }
                            }


                        }
                        Status.ERROR -> {

                        }
                        Status.LOADING -> {


                        }
                    }
                }
            })


    }

    private fun initUi() {


        binding.btnTopup.setOnClickListener {
            findNavController().navigate(R.id.action_mobileWalletFragment_to_topUpWalletFragment2)
        }
        binding.btnTransact.setOnClickListener {
            findNavController().navigate(R.id.action_mobileWalletFragment_to_withdrawFragment2)
        }
    }

    private fun setupViewModel() {
        viewmodel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireActivity())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)


        roomDBViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )

        ).get(RoomDBViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ChangeActionBarandBackArrowColor("#f76710")
    }

    override fun onStop() {
        super.onStop()
        makeStatusBarTransparent()
        showSupportActionBar()
    }

    override fun onResume() {
        super.onResume()
        hideSupportActionBar()
        changeStatusBarColor(Color.parseColor("#ffee1a23"))
        //extendStatusBarBackground()

    }


    private fun loadLineGraph() {
        val entries: ArrayList<Entry> = ArrayList()
        val entries2: ArrayList<Entry> = ArrayList()

        /* topUpList = dataValueFirstLine()
         withdrawList = dataValueSecondLine()*/

        //you can replace this data object with  your custom object
        for (i in topUpList.indices) {
            val score = topUpList[i]
            val score2 = withdrawList[i]
            entries.add(Entry(i.toFloat(), score.amount.toFloat()))
            entries2.add(Entry(i.toFloat(), score2.amount.toFloat()))
        }


        val lineDataSetFirstLine = LineDataSet(entries, "Money In")
        lineDataSetFirstLine.apply {
            color = Color.parseColor("#ffee1a23")
            //circleColors = listOf(Color.RED)
            setCircleColor(Color.parseColor("#ffee1a23"))
            setDrawCircleHole(false)
        }
        val lineDataSetSecondLine = LineDataSet(entries2, "Money Out")
        lineDataSetSecondLine.apply {
            //ContextCompat.getColor(requireView().context, R.color.colorOnSurface)
            color = Color.parseColor("#CD0031")
            circleColors = listOf(Color.parseColor("#CD0031"))
            setDrawCircleHole(false)
        }
        val dataSet: ArrayList<ILineDataSet> = ArrayList()
        dataSet.add(lineDataSetFirstLine)
        dataSet.add(lineDataSetSecondLine)

        val lineData = LineData(dataSet)
        binding.graph.data = lineData
        binding.graph.invalidate()

        binding.graph.xAxis.setDrawGridLines(false)
        binding.graph.axisLeft.setDrawGridLines(false)
        binding.graph.axisLeft.axisMinimum = 0f

        binding.graph.axisRight.setDrawGridLines(false)
        binding.graph.axisRight.setDrawLabels(false)
        binding.graph.axisRight.setDrawAxisLine(false)


        val xAxis: XAxis = binding.graph.xAxis
        //val leftAxis: YAxis = binding.reportingChart.axisLeft
        val xPosition = XAxis.XAxisPosition.BOTTOM
        xAxis.position = xPosition
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.isGranularityEnabled = true




        binding.graph.description.isEnabled = true
        val description = Description()
        description.text = "Month"
        description.textSize = 15f


    }




    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < topUpList.size) {
                topUpList[index].month
            } else {
                ""
            }
        }
    }

}