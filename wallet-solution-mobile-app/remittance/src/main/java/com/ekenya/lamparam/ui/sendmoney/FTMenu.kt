package com.ekenya.lamparam.ui.sendmoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.databinding.FragmentFTMenuBinding
import com.ekenya.lamparam.utilities.StaticData
import kotlinx.android.synthetic.main.fragment_f_t_menu.view.*


class FTMenu : Fragment() {
    lateinit var rcvFTMenu: RecyclerView
    lateinit var FTAdapter: FTAdapter
    private var _binding: FragmentFTMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFTMenuBinding.inflate(inflater)
        binding.appBar.tvTitle.text = "Card Payments"
        binding.appBar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcvFTMenu = view.rcvFTMenu
        rcvFTMenu.setHasFixedSize(true)
        rcvFTMenu.layoutManager = GridLayoutManager(context, 2)
        var dashmenuList = StaticData().sendMoneyList()
        FTAdapter = FTAdapter(requireActivity(), dashmenuList)
        rcvFTMenu.adapter = FTAdapter

    }
}