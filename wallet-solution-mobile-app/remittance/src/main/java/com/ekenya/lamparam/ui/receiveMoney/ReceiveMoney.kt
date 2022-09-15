package com.ekenya.lamparam.ui.receiveMoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.databinding.FragmentReceiveMoneyBinding
import com.ekenya.lamparam.utilities.StaticData
import kotlinx.android.synthetic.main.fragment_receive_money.view.*

class ReceiveMoney : Fragment() {

    private lateinit var binding: FragmentReceiveMoneyBinding
    lateinit var rcvReceiveMoneyMenu: RecyclerView
    lateinit var ReceiveMoneyAdapter: ReceiveMoneyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReceiveMoneyBinding.inflate(inflater)
        binding.appBar.tvTitle.text = "Cash Pickup"
        binding.appBar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcvReceiveMoneyMenu = view.rcvReceiveMoneyMenu
        rcvReceiveMoneyMenu.setHasFixedSize(true)
        rcvReceiveMoneyMenu.layoutManager = GridLayoutManager(context, 2)
        var dashmenuList = StaticData().sendMoneyList()
        ReceiveMoneyAdapter = ReceiveMoneyAdapter(requireActivity(), dashmenuList)
        rcvReceiveMoneyMenu.adapter = ReceiveMoneyAdapter

    }
}