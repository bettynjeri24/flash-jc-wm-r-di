package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.adapters.BudgetBreakdownAdapter
import com.ekenya.rnd.dashboard.adapters.BudgetCategoriesAdapter
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.MonthData
import com.ekenya.rnd.dashboard.database.AppData.getBudgetBreadownData
import com.ekenya.rnd.dashboard.database.AppData.getBudgetCategories
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.darkStatusBar
import com.ekenya.rnd.dashboard.utils.hideSupportActionBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarTransparent2
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentGrowthBinding

class GrowthFragment : Fragment() {

    private lateinit var viewmodel: LoginViewModel

    //private var _binding: FragmentScanBinding? = null
    private var _binding: FragmentGrowthBinding? = null
    private var total_cashflow = 0.0
    private var topUpList = ArrayList<MonthData>()
    private lateinit var budgetCategoriesAdapter: BudgetCategoriesAdapter
    private lateinit var budgetBreakdownAdapter: BudgetBreakdownAdapter
    private lateinit var budgetCategoriesRv: RecyclerView
    private lateinit var budgetBudgetBreaddownRv: RecyclerView


    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSupportActionBar()
        darkStatusBar()
        makeStatusBarTransparent2()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentGrowthBinding.inflate(inflater, container, false)
        // = FragmentScanBinding.inflate(inflater, container, false)

        initAdapter()
        initRecyclerView()
        initViewModel()
        setClickListeners()
        setUpObservers()


        return binding.root
    }

    private fun initRecyclerView() {

        initBudgetCategoriesRv()
        initBudgetBreakdownRv()

    }

    private fun initBudgetCategoriesRv() {
        budgetCategoriesRv = binding.budgetCategoriesrv
        budgetCategoriesRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        budgetCategoriesAdapter.setDataItems(getBudgetCategories(requireContext()))
        budgetCategoriesRv.adapter = budgetCategoriesAdapter
    }

    private fun initBudgetBreakdownRv() {
        budgetBudgetBreaddownRv = binding.budgetRv
        budgetBudgetBreaddownRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        budgetBreakdownAdapter.setDataItems(getBudgetBreadownData(requireContext()))
        budgetBudgetBreaddownRv.adapter = budgetBreakdownAdapter
    }

    private fun initAdapter() {
        budgetCategoriesAdapter = BudgetCategoriesAdapter(requireContext())
        budgetBreakdownAdapter = BudgetBreakdownAdapter(requireContext())
    }

    private fun setUpObservers() {
        //  getCashFlow()
    }

    private fun setClickListeners() {

        binding.createbudget.setOnClickListener {
            findNavController().navigate(R.id.createBudgetFragment)
        }


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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
/*
    private fun getCashFlow() {
        val data = StatisticPayload(
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            SharedPreferencesManager.getAccountNumber(requireContext())!!,

            "year_stats"
        )

        viewmodel.getStatistics(data)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {

                            when (it.data?.status) {
                                0 -> {
                                    topUpList =
                                        it.data!!.data.year_cashflow_summary

                                    loadLineGraph()
                                    binding.tvAmountInfo.text = "${it.data!!.data.total_cashflow.total_deposits}.00 GHS"

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
*/

    /*private fun loadLineGraph() {
        val entries: ArrayList<Entry> = ArrayList()
        //val entries2: ArrayList<Entry> = ArrayList()

        *//* topUpList = dataValueFirstLine()
         withdrawList = dataValueSecondLine()*//*

        //you can replace this data object with  your custom object
        for (i in topUpList.indices) {
            val score = topUpList[i]
            //val score2 = withdrawList[i]
            entries.add(Entry(i.toFloat(), score.amount.toFloat()))
            //entries2.add(Entry(i.toFloat(), score2.amount.toFloat()))
        }


        val lineDataSetFirstLine = LineDataSet(entries, "CashFlow")
        lineDataSetFirstLine.apply {
            color = Color.RED
            setCircleColor(Color.RED)
            setDrawCircleHole(false)
        }
        *//*val lineDataSetSecondLine = LineDataSet(entries2, "x")
        lineDataSetSecondLine.apply {
            color = Color.BLUE
            circleColors = listOf(Color.BLUE)
            setDrawCircleHole(false)
        }*//*
        val dataSet: ArrayList<ILineDataSet> = ArrayList()
        dataSet.add(lineDataSetFirstLine)
        //dataSet.add(lineDataSetSecondLine)

        val lineData = LineData(dataSet)
        binding.graph.data = lineData
        binding.graph.invalidate()

        binding.graph.xAxis.setDrawGridLines(false)
        binding.graph.axisLeft.setDrawGridLines(false)
        binding.graph.axisLeft.textColor=Color.WHITE

        binding.graph.xAxis.textColor=Color.WHITE
        binding.graph.axisRight.setDrawGridLines(false)
        binding.graph.legend.textColor= Color.WHITE

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
    }*/


}