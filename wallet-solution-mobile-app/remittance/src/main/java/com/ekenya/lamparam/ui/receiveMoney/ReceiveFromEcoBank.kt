package com.ekenya.lamparam.ui.receiveMoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.UtilityClass
import com.ekenya.lamparam.activities.main.LampMainViewModel
import kotlinx.android.synthetic.main.fragment_receive_from_ecobank.*
import kotlinx.android.synthetic.main.header_layout.*


class ReceiveFromEcoBank : Fragment() {

    lateinit var navOptions: NavOptions

    lateinit var lampMainViewModel: LampMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lampMainViewModel = ViewModelProvider((activity as LampMainActivity)).get(LampMainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_receive_from_ecobank, container, false)
        //((activity as MainActivity).showToolbar("",""))
        navOptions = UtilityClass().getNavoptions()
        //((activity as LampMainActivity).hideActionBar())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var title = requireArguments().getString("title")
        var slogan  =requireArguments().getString("slogan")//slogan
        tvActionTitle.text = "$title$slogan"
        var confBundle = requireArguments()
       // confBundle.putString("")
        //bsck button
        //btn_back
        ((activity as LampMainActivity). onBackClick(btn_back,view))
        //nav_setAmount
        btnReceiveEcobnk.setOnClickListener {
            lampMainViewModel.senderPhoneNumber.postValue(etYourMobileNumber.text.toString())
            lampMainViewModel.senderName.postValue(etYourFullName.text.toString())
            lampMainViewModel.senderIdNumber.postValue(etIdNo.text.toString())

            findNavController().navigate(R.id.nav_setAmount,confBundle, navOptions)
        }
    }

}