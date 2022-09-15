package com.ekenya.lamparam.ui.schoolfees

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.StaticData
import kotlinx.android.synthetic.main.fragment_fee_menu.view.*

class FeeMenu : Fragment() {

    lateinit var rcvReceiveMoneyMenu: RecyclerView
    lateinit var FeeAdapter: FeeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_fee_menu, container, false)
       // ((activity as LampMainActivity).showMenuDashboard("School fee payment"))
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcvReceiveMoneyMenu = view.rcvScholFeeMenu
        rcvReceiveMoneyMenu.setHasFixedSize(true)
        rcvReceiveMoneyMenu.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        var dashmenuList = StaticData().schoolType()
        FeeAdapter = FeeAdapter(requireActivity(), dashmenuList)
        rcvReceiveMoneyMenu.adapter = FeeAdapter

    }
}