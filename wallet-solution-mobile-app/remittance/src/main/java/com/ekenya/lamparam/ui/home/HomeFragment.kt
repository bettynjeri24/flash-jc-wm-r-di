package com.ekenya.lamparam.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.R

import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.databinding.FragmentHomeRemittanceBinding
import com.ekenya.lamparam.utilities.StaticData
import android.R.attr.defaultValue
import androidx.navigation.fragment.findNavController


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var rcvDashboard: RecyclerView
    lateinit var dashboardAdapter: DashboardAdapter
    private lateinit var binding: FragmentHomeRemittanceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeRemittanceBinding.inflate(inflater)
        binding.appBar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcvDashboard = binding.rcvDashmenu

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
      //  ((activity as LampMainActivity).hideBottonNav())

        rcvDashboard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val dashmenuList = context?.let { StaticData().mainDashBoard(it) }
        dashboardAdapter = dashmenuList?.let { DashboardAdapter(requireActivity(), it) }!!
        rcvDashboard.adapter = dashboardAdapter

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
    }


}