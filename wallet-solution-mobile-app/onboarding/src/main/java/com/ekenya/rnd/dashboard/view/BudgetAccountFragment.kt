package com.ekenya.rnd.dashboard.view

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.dashboard.adapters.BudgetBreakdownAdapter
import com.ekenya.rnd.dashboard.database.AppData
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.BudgetAccountViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.BudgetAccountFragmentBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class BudgetAccountFragment : Fragment() {

    private lateinit var binding: BudgetAccountFragmentBinding
    private lateinit var budgetBudgetBreaddownRv: RecyclerView
    private lateinit var budgetBreakdownAdapter: BudgetBreakdownAdapter

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
        binding = BudgetAccountFragmentBinding.inflate(layoutInflater,container,false)

       initAdapter()


        loadTransactionGraph()
        return binding.root

    }

    private fun initAdapter() {
        budgetBreakdownAdapter = BudgetBreakdownAdapter(requireContext())
        initBudgetBreakdownRv()

    }
    private fun initBudgetBreakdownRv() {
        budgetBudgetBreaddownRv = binding.budgetRv
        budgetBudgetBreaddownRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        budgetBreakdownAdapter.setDataItems(AppData.getBudgetBreadownData(requireContext()))
        budgetBudgetBreaddownRv.adapter = budgetBreakdownAdapter
    }

    private fun loadTransactionGraph() {

        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.description.isEnabled = true
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.pieChart.dragDecelerationFrictionCoef = 0.99f
        binding.pieChart.setDrawSlicesUnderHole(true)
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setEntryLabelTextSize(8f)
        binding.pieChart.holeRadius = 50f
        binding.pieChart.setEntryLabelColor(Color.BLACK)

        val l: Legend = binding.pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.CIRCLE
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        l.calculatedLineSizes
        val visitors: ArrayList<PieEntry> = ArrayList()

        visitors.add(PieEntry(500.toFloat(), "Bills"))
        visitors.add(PieEntry(300.toFloat(), "Send Money"))
       // visitors.add(PieEntry(120.toFloat(), "Savings"))
        visitors.add(PieEntry(450.toFloat(), "Withdraw"))
        //visitors.add(PieEntry(250.toFloat(), "Others"))

        binding.pieChart.centerText = "GHS 10000.00"
        binding.pieChart.setCenterTextSize(10f)
        binding.pieChart.animateX(3000, Easing.EaseInOutQuad)

        val description = Description()
        description.text = ""
        binding.pieChart.description=description

        val dataSet = PieDataSet(visitors, "")
        /* dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
         dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE*/
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 20f
        dataSet.setColors(
            Color.rgb(255, 0, 0),
            Color.rgb(255, 140, 0),
            Color.rgb(255, 255, 0),
            Color.rgb(0, 128, 0),
            Color.rgb(0, 0, 255),
            Color.rgb(128, 0, 128)
        )
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(binding.pieChart))
        data.setValueTextSize(10f)

        data.setValueTextColor(Color.BLACK)
        binding.pieChart.data = data


    }

}