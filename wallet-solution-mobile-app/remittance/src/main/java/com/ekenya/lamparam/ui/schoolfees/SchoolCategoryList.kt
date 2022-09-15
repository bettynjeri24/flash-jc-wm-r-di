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
import kotlinx.android.synthetic.main.fragment_school_category_list.view.*
import kotlinx.android.synthetic.main.header_layout.*


class SchoolCategoryList : Fragment() {

    lateinit var rcvSelectSchool: RecyclerView
    lateinit var FeeAdapter: BankListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_school_category_list, container, false)
       // ((activity as LampMainActivity).hideActionBar())
       /// navOptions = UtilityClass().getNavoptions()
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var confBundle = requireArguments()
       // tvSendToDesc.text ="Primary School"// ${confBundle.getString("title")}
        tvActionTitle.text = "Primary School"
        //btn click
        ((activity as LampMainActivity). onBackClick(btn_back,view))
        //nav_confirmPin
       // btnConfirmTrans.setOnClickListener { findNavController().navigate(R.id.nav_confirmPin) }
        rcvSelectSchool = view.rcvSelectSchool
        rcvSelectSchool.setHasFixedSize(true)
        rcvSelectSchool.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        var dashmenuList = StaticData().schoolList()
        FeeAdapter = BankListAdapter(requireActivity(), dashmenuList)
        rcvSelectSchool.adapter = FeeAdapter
    }
}