package io.eclectics.cargilldigital.ui.buyerprofile.dashboard

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.ui.bottomsheet.BottomSheetFragment
import io.eclectics.cargilldigital.ui.bottomsheet.OnActionTaken
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.CollectionsAdapter
import io.eclectics.cargilldigital.adapter.CollectionsListener
import io.eclectics.cargilldigital.databinding.FragmentCheckFloatBinding
import io.eclectics.cargill.model.FarmerTransaction
import io.eclectics.cargill.model.FarmersModel
import io.eclectics.cargill.network.networkCallback.responseCallback.FloatCallBack
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargill.viewmodel.AgentViewModel
import io.eclectics.cargill.viewmodel.FarmViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [CheckFloatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CheckFloatFragment : Fragment(), OnActionTaken {

    private var _binding: FragmentCheckFloatBinding? = null
    private val binding get() = _binding!!
    lateinit var farmViewModel: FarmViewModel
    lateinit var pDialog: SweetAlertDialog
    lateinit var  farmersList:List<FarmerTransaction>
    private val MAX_X_VALUE = 5
    private val MAX_Y_VALUE = 50
    private val MIN_Y_VALUE = 5
    private val SET_LABEL = "Recent Cash Disbursements 2021 (1,000 CFA)"
    private val DAYS = arrayOf("JAN", "FEB", "MAR", "APR", "MAY")

    private var show = true
    private var floatBalance = 0.0
    private lateinit var bottomSheetFragment: BottomSheetFragment

    private val globalMethods = GlobalMethods()
    lateinit var agentViewModel: AgentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckFloatBinding.inflate(inflater, container, false)
        farmViewModel = ViewModelProvider(requireActivity()).get(FarmViewModel::class.java)
        agentViewModel = ViewModelProvider(this).get(AgentViewModel::class.java)
        pDialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        binding.btnHideBalance.setOnClickListener {
            show = false
            animate(container!!, binding)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //UserRepository.getUser()
        floatBalance = 52.30//UserRepository.theUser.value!!.floatBalance
         lifecycleScope.launch {
            NetworkUtility().sendRequest(pDialog)
            getAgentFloat()
            sendFarmregRequest()
        }
       /* val adapter = CollectionsAdapter(CollectionsListener { collection ->
            navigateToCollection(collection)
        })

        binding.rvCollectionHistory.adapter = adapter*/

        val data: BarData = createChartData()
        configureChartAppearance()
        prepareChartData(data)

        binding.tvBalanceInstr.text = getString(R.string.your_float_balance_is, "")

        //binding.tvFloatAmount.text = Html.fromHtml(getString(R.string.raw_amount, globalMethods.getBigSizeSpanned(floatBalance.toString())))

        var balance = UtilPreference().getFloatBalance(requireContext())
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = balance.toInt()
        val formattedNumber = formatter.format(myNumber)

        /*  var sample = "35,600"
          var inttxt = sample.toInt() - 1600*/
        //binding.tvFloatAmount.text =NetworkUtility().cashFormatter(balance)//"CFA ${formattedNumber.toString()}"

        binding.btnRequestFloat.setOnClickListener {
            showDropDown(null, "request_float")
        }
        //show floa{
        binding.tvFloatAmount.setOnClickListener{
            showDropDown(null, "showFloat")
        }
    }

    private fun showDropDown(farmer: FarmersModel?, action: String) {
        // using BottomSheetDialogFragment
        bottomSheetFragment = BottomSheetFragment(this, farmer, action)

        bottomSheetFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetFragment.tag
        )
    }

    private fun animate(parent: ViewGroup, binding: FragmentCheckFloatBinding) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 1500
        transition.addTarget(binding.floatHolder)

        TransitionManager.beginDelayedTransition(parent, transition)
        binding.floatHolder.visibility = if (show) View.VISIBLE else View.GONE
    }

      suspend fun getAgentFloat() {
//getAgentFloat
        agentViewModel.getAgentFloat().observe(requireActivity(),androidx.lifecycle.Observer{
          //  pDialog.dismiss()
            when(it){
                is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                is ViewModelWrapper.response -> processFloatBalance(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
            }
        })
    }

    private fun processFloatBalance(floatResp: String) {
        //FloatCallBack
        //var balance = UtillPreference().getWalletBalance(requireContext())
        LoggerHelper.loggerError("respobnse", "resp $floatResp")
        var floatCallBackList: List<FloatCallBack> = NetworkUtility.jsonResponse(floatResp)
        var floatCallBack = floatCallBackList[0]
        binding.tvFloatAmount.text =NetworkUtility().cashFormatter(floatCallBack.balance)//"CFA ${formattedNumber.toString()}"
        UtilPreference().saveFloatBalance(requireActivity(),floatCallBack.balance)
        //
        UtilPreference().saveCashBalance(requireActivity(),"10000")
        try{
            var walletbalance = floatCallBack.balance.toInt() - 10000
            UtilPreference().saveWalletBalance(requireActivity(),walletbalance.toString())
            LoggerHelper.loggerError("walletBalance","walletBalance-$walletbalance-floatttot-${floatCallBack.balance}")

        }catch (ex:Exception){}
    }
    /*private fun navigateToCollection(collection: CollectionsModel) {

    }*/

    private fun configureChartAppearance() {
        binding.barChart.getDescription().setEnabled(false)
        binding.barChart.setDrawValueAboveBar(false)
        val xAxis: XAxis = binding.barChart.getXAxis()
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return DAYS[value.toInt()]
            }
        }
        val axisLeft: YAxis = binding.barChart.getAxisLeft()
        axisLeft.granularity = 10f
        axisLeft.axisMinimum = 0f
        val axisRight: YAxis = binding.barChart.getAxisRight()
        axisRight.granularity = 10f
        axisRight.axisMinimum = 0f
    }

    private fun createChartData(): BarData {
        val values: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until MAX_X_VALUE) {
            val x = i.toFloat()
            val rand = Random()
            val y: Float = rand.nextFloat() * (MAX_Y_VALUE - MIN_Y_VALUE) + MIN_Y_VALUE
            values.add(BarEntry(x, y))
        }

        val set1 = BarDataSet(values, SET_LABEL)
        val MATERIAL_COLORS = intArrayOf(
          R.color.reddish, R.color.greenish, R.color.bluish, R.color.orangish, R.color.accent3
        )
        set1.setColors(MATERIAL_COLORS, requireContext())

        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        return BarData(dataSets)
    }

    private fun prepareChartData(data: BarData) {
        data.setValueTextSize(12f)
        binding.barChart.data = data
        binding.barChart.animateXY(3000, 5000)
        binding.barChart.invalidate()
    }

    private fun createSimpleBarGraph() {
        val data = BarData(getDataSet())

        binding.barChart.data = data
        val description = Description()
        description.text = getString(R.string.statistics)
        description.textAlign = Paint.Align.CENTER
        binding.barChart.description = description

        binding.barChart.invalidate()
    }

    private fun getDataSet(): ArrayList<IBarDataSet> {
        val valueSet1:ArrayList<BarEntry> = ArrayList()
        val v1e1 = BarEntry(110.000f, 0f) // Jan
        valueSet1.add(v1e1)
        val v1e2 = BarEntry(40.000f, 1f) // Feb
        valueSet1.add(v1e2)
        val v1e3 = BarEntry(60.000f, 2f) // Mar
        valueSet1.add(v1e3)
        val v1e4 = BarEntry(30.000f, 3f) // Apr
        valueSet1.add(v1e4)
        val v1e5 = BarEntry(90.000f, 4f) // May
        valueSet1.add(v1e5)

        val valueSet2:ArrayList<BarEntry> = ArrayList()
        val v2e1 = BarEntry(150.000f, 0f) // Jan
        valueSet2.add(v2e1)
        val v2e2 = BarEntry(90.000f, 1f) // Feb
        valueSet2.add(v2e2)
        val v2e3 = BarEntry(120.000f, 2f) // Mar
        valueSet2.add(v2e3)
        val v2e4 = BarEntry(60.000f, 3f) // Apr
        valueSet2.add(v2e4)
        val v2e5 = BarEntry(20.000f, 4f) // May
        valueSet2.add(v2e5)

        val barDataSet1 = BarDataSet(valueSet1, "2020")
        barDataSet1.color = Color.rgb(0, 155, 0)
        val barDataSet2 = BarDataSet(valueSet2, "2021")
        barDataSet2.setColors(*ColorTemplate.COLORFUL_COLORS)

        val dataSets: ArrayList<IBarDataSet> = ArrayList()

        dataSets.add(barDataSet1)
        dataSets.add(barDataSet2)
        return dataSets
    }

    private fun getXAxisValues(): ArrayList<String> {
        val xAxis = ArrayList<String>()
        xAxis.add("JAN")
        xAxis.add("FEB")
        xAxis.add("MAR")
        xAxis.add("APR")
        xAxis.add("MAY")

        return xAxis
    }

    override fun onActionChosen(value: String?) {
        value?.let {
            if (value == "request_float"){
                globalMethods.confirmTransactionEnd(
                    "Request sent Successfully",
                    requireActivity()
                )
                bottomSheetFragment.dismiss()
                 lifecycleScope.launch {
                    getAgentFloat()
                }
            }else{
                bottomSheetFragment.dismiss()
            }
        }
    }

    private suspend fun sendFarmregRequest() {
        farmViewModel.getFarmerTransaction().observe(requireActivity(),androidx.lifecycle.Observer{
            pDialog.dismiss()
            when(it){
                is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
            }
        })

    }

    private fun processRequest(response: String) {
        LoggerHelper.loggerError("processTrans", "trans $response")
//FarmerTransaction
        farmersList = NetworkUtility.jsonResponse(response)
        var productFilterSort = farmersList.toMutableList()
            .sortedByDescending { vanOrderList -> vanOrderList.datetime }
        val adapter = CollectionsAdapter(CollectionsListener { collection ->
           // navigateToCollection(collection)
        },productFilterSort)//

        binding.rvCollectionHistory.adapter = adapter
    }
}