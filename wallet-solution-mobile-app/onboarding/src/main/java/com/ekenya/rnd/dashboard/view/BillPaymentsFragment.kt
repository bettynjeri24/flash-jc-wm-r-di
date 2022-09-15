package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.dashboard.adapters.BillPaymentsRecyclerviewAdapter
import com.ekenya.rnd.dashboard.adapters.FrequentBillersRvAdapter
import com.ekenya.rnd.dashboard.datadashboard.model.BillItem
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeActiveBillOption
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.BillPaymentsBinding

class BillPaymentsFragment : BaseDaggerFragment() {
    private lateinit var binding: BillPaymentsBinding
    private lateinit var billPaymentsRecyclerviewAdapter: BillPaymentsRecyclerviewAdapter
    private lateinit var frequentBillersRvAdapter: FrequentBillersRvAdapter
    private lateinit var billItemsRv: RecyclerView
    private lateinit var frequentItemsRv: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BillPaymentsBinding.inflate(inflater, container, false)
        setClickListeners()
        initUI()
        return binding.root
    }

    private fun initUI() {
        makeActiveBillOption(
            binding.allTv,
            binding.tvElectricity,
            binding.tvWater,
            binding.tvAirtime
        )

        initBIllRecyclerView()
        initFrequentBillRv()
    }

    private fun initFrequentBillRv() {
        frequentBillersRvAdapter = FrequentBillersRvAdapter(requireContext())
        frequentBillersRvAdapter.setDashBoardItems(getBillers().subList(0,1))
        frequentItemsRv = binding.billFrequentoptionsRecyclerView
        frequentItemsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        frequentItemsRv.adapter = frequentBillersRvAdapter

    }

    private fun initBIllRecyclerView() {
        billPaymentsRecyclerviewAdapter = BillPaymentsRecyclerviewAdapter(requireContext())
        billPaymentsRecyclerviewAdapter.setDashBoardItems(getBillers())
        billItemsRv = binding.billOptionsRecyclerView

        billItemsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        billItemsRv.adapter = billPaymentsRecyclerviewAdapter


    }

    fun getBillers(): List<BillItem> {

        val itemlist = ArrayList<BillItem>()

        itemlist.add(
            BillItem(
                R.drawable.ic_dstv_big,
                getString(R.string.dstv)
            )
        )


        itemlist.add(
            BillItem(
                R.drawable.airplane,
                getString(R.string.airplane)
            )
        )




        itemlist.add(
            BillItem(
                R.drawable.school,
                getString(R.string.school)
            )
        )





        return itemlist


    }

    private fun setClickListeners() {


        binding.allTv.setOnClickListener {
            makeActiveBillOption(
                binding.allTv,
                binding.tvElectricity,
                binding.tvWater,
                binding.tvAirtime
            )
            billPaymentsRecyclerviewAdapter.setDashBoardItems(getBillers())

        }
        binding.tvElectricity.setOnClickListener {

            val itemlist = ArrayList<BillItem>()
            itemlist.add(
                BillItem(
                    R.drawable.ic_botspower,
                    getString(R.string.botswana_power)
                )
            )

            billPaymentsRecyclerviewAdapter.setDashBoardItems(itemlist)
            makeActiveBillOption(
                binding.tvElectricity,
                binding.allTv,
                binding.tvWater,
                binding.tvAirtime
            )
        }
        binding.tvWater.setOnClickListener {
            val itemlist = ArrayList<BillItem>()

            billPaymentsRecyclerviewAdapter.setDashBoardItems(itemlist)
            makeActiveBillOption(
                binding.tvWater,
                binding.tvElectricity,
                binding.allTv,
                binding.tvAirtime
            )
        }
        binding.tvAirtime.setOnClickListener {

            val itemlist = ArrayList<BillItem>()
            itemlist.add(
                BillItem(
                    R.drawable.safaricom,
                    getString(R.string.safaricom)
                )
            )
            itemlist.add(
                BillItem(
                    R.drawable.ethiopia_tel,
                    getString(R.string.ethiopia_tel)
                )
            )

            billPaymentsRecyclerviewAdapter.setDashBoardItems(itemlist)
            makeActiveBillOption(
                binding.tvAirtime,
                binding.tvWater,
                binding.tvElectricity,
                binding.allTv,
            )
        }
    }


}