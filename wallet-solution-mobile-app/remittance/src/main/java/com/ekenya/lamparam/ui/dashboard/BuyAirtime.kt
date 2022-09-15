package com.ekenya.lamparam.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.fragment_buyairtime.*
import kotlinx.android.synthetic.main.header_layout.*

class BuyAirtime : Fragment() {

    lateinit var spnProvider : Spinner
    lateinit var spnAccount : Spinner
    lateinit var providerList:ArrayList<String>
    lateinit var  accountList: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootiew = inflater.inflate(R.layout.fragment_buyairtime, container, false)
        //((activity as MainActivity).showToolbar("Buy Airtime","How much do you wish to spend"))
       // ((activity as LampMainActivity).hideActionBar())
        return rootiew
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = getString(R.string.str_buy_airtime)
        providerList= ArrayList()
        providerList.add("Select Network provider")
        providerList.add("Orange")
        providerList.add("MTN")

        accountList = ArrayList()
        accountList.add("Lamparam account 2727827827")
        accountList.add("Orange Money")
        accountList.add("MTN Money")
        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, providerList) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_provider.adapter = adapter
        //back button
        ((activity as LampMainActivity). onBackClick(btn_back,view))
        //spn_payfrom
//        var list = StaticData().accountsType()
//        var  accTypeAdapter = SpinnerAccountAdapter(requireActivity(),list)
        // acctFrmspnAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val adapter2 = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, accountList) }
        adapter2?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_payfrom.adapter = adapter2
        //nav_Confirmation
        btnBuyAirtime.setOnClickListener { findNavController().navigate(R.id.nav_Confirmation) }
    }
}