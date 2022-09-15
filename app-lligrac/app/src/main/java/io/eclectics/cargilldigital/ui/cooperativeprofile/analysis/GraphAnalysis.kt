package io.eclectics.cargilldigital.ui.cooperativeprofile.analysis

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentGraphAnalysisBinding
import io.eclectics.cargilldigital.utils.ToolBarMgmt


@AndroidEntryPoint
class GraphAnalysis : Fragment() {
    private var _binding: FragmentGraphAnalysisBinding? = null
    private val binding get() = _binding!!
    lateinit var pieChart: PieChart
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphAnalysisBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(
            resources.getString(R.string.evalue_analysis),
            resources.getString(R.string.evalue_analysis),
            binding.mainLayoutToolbar,
            requireActivity()
        )
        return binding.root
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_graph_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pieChart = binding.pieChartView
        initPieChart()
        showPieChart()
    }
    private fun showPieChart() {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = "type"

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["Buyers"] = 200
        typeAmountMap["Suppliers"] = 230
        typeAmountMap["Third party"] = 100
        typeAmountMap["Salaries"] = 500
        typeAmountMap["Others"] = 50

        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#304567"))
        colors.add(Color.parseColor("#309967"))
        colors.add(Color.parseColor("#476567"))
        colors.add(Color.parseColor("#890567"))
        colors.add(Color.parseColor("#a35567"))
        colors.add(Color.parseColor("#ff5f67"))
        colors.add(Color.parseColor("#3ca567"))

        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        pieData.setValueFormatter(PercentFormatter())
        pieChart.data = pieData
        pieChart.invalidate()
    }
    private fun initPieChart() {
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(true)

        //remove the description label on the lower left corner, default true if not set
        pieChart.description.isEnabled = false

        //enabling the user to rotate the chart, default true
        pieChart.isRotationEnabled = true
        //adding friction when rotating the pie chart
        pieChart.dragDecelerationFrictionCoef = 0.9f
        //setting the first entry start from right hand side, default starting from top
        pieChart.rotationAngle = 0f

        //highlight the entry when it is tapped, default true if not set
        pieChart.isHighlightPerTapEnabled = true
        //adding animation so the entries pop up from 0 degree  .EasingOption.EaseInOutQuad
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        //setting the color of the hole in the middle, default white
        pieChart.setHoleColor(Color.parseColor("#000000"))
        pieChart.holeRadius = 0f;
        pieChart.transparentCircleRadius = 0f;

    }
}