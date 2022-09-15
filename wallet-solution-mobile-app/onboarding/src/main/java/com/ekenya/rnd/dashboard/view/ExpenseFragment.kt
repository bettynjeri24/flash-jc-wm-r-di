package com.ekenya.rnd.dashboard.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.MonthData
import com.ekenya.rnd.dashboard.datadashboard.model.StatisticPayload
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.onboarding.databinding.ExpenseFragmentBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.math.RoundingMode
import java.text.DecimalFormat

class ExpenseFragment : Fragment() {

    private lateinit var binding: ExpenseFragmentBinding
    private lateinit var viewmodel: LoginViewModel
    private var topUpList = ArrayList<MonthData>()
    private var withdrawList = ArrayList<MonthData>()
    private  var monthlyWithdrawalAverage = 0.0
    private var monthlyBillsAverage = 0.0
    private var monthlyFundTransfersAverage = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpenseFragmentBinding.inflate(inflater, container, false)
        initUI()
        initViewModel()
        setupObservers()
        setOnclickListener()

        return binding.root
    }

    private fun initViewModel() {
        viewmodel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireActivity())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)
    }

    private fun setOnclickListener() {
    }

    private fun initUI() {
        binding.userName.text =
            SharedPreferencesManager.getFirstName(requireContext()) + " " + SharedPreferencesManager.getLastName(
                requireContext()
            )
    }

    private fun setupObservers() {

        getCashFlow()

        getMonthlyAverage()


    }

    private fun getMonthlyAverage() {
        val data = StatisticPayload(
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            SharedPreferencesManager.getAccountNumber(requireContext())!!,

            "monthly_average"
        )

        viewmodel.getStatistics(data)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {

                            when (it.data?.status) {
                                0 -> {
                                    monthlyBillsAverage = roundOffDecimal(it.data!!.data.bills[0].amount.toDouble())
                                    monthlyFundTransfersAverage = it.data!!.data.fund_transfer[0].amount.toDouble()
                                    monthlyWithdrawalAverage = it.data!!.data.withdrawal[0].amount.toDouble()
                                    Log.d("TAG", "getMonthlyAverage: bills $monthlyBillsAverage")
                                    Log.d("TAG", "getMonthlyAverage: funds $monthlyFundTransfersAverage")
                                    Log.d("TAG", "getMonthlyAverage: withdrawal $monthlyWithdrawalAverage")


                                    PopulateProgressBars()

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

    private fun PopulateProgressBars() {
        
        
        val billsProgress = monthlyBillsAverage/10000*100
        val withdrawalProgress = monthlyWithdrawalAverage/10000*100
        val sendMoneyProgress = monthlyFundTransfersAverage/10000*100

        binding.progressbarbills.progress = billsProgress.toInt()
        binding.progressbarsavings.progress = 0
        binding.progressbarsendmoney.progress = sendMoneyProgress.toInt()
        binding.progressbar.progress=withdrawalProgress.toInt()

        binding.monthlyBillsAverage.text = "GHS $monthlyBillsAverage"
        binding.monthlyWithdrawalAverage.text = "GHS $monthlyWithdrawalAverage"
        binding.monthlySavingsAverage.text = "GHS 0.0"
        binding.monthlySendMoneyAverage.text = "GHS $monthlyFundTransfersAverage"
    }

    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }

    private fun getCashFlow() {
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


        val lineDataSetFirstLine = LineDataSet(entries, "Top Up Data")
        lineDataSetFirstLine.apply {
            color = Color.RED
            //circleColors = listOf(Color.RED)
            setCircleColor(Color.RED)
            setDrawCircleHole(false)
        }
        val lineDataSetSecondLine = LineDataSet(entries2, "WithDraw Data")
        lineDataSetSecondLine.apply {
            //ContextCompat.getColor(requireView().context, R.color.colorOnSurface)
            color = Color.BLUE
            circleColors = listOf(Color.BLUE)
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
        binding.graph.axisRight.setDrawGridLines(false)

        binding.graph.axisRight.setDrawGridLines(false)
        binding.graph.axisRight.setDrawLabels(false)
        binding.graph.axisRight.setDrawAxisLine(false)


        val xAxis: XAxis = binding.graph.xAxis
        //val leftAxis: YAxis = binding.reportingChart.axisLeft
        val xPosition = XAxis.XAxisPosition.BOTTOM
        xAxis.position = xPosition
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.isGranularityEnabled = true


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