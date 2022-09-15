package com.ekenya.lamparam.ui.paybill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.StaticData
import kotlinx.android.synthetic.main.fragment_paybill.*


class Paybill : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootiew= inflater.inflate(R.layout.fragment_paybill, container, false)
        return rootiew
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcvBillList.setHasFixedSize(true)
        rcvBillList.layoutManager = GridLayoutManager(context, 3)
        var dashmenuList = StaticData().payBillsList()
        var dashboardAdapter = PaybillAdapter(requireActivity(), dashmenuList)
        rcvBillList.adapter = dashboardAdapter
    }

}