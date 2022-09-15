package com.ekenya.rnd.tijara.ui.auth.onboarding

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.layoutAdapter.AboutUsAdapter
import com.ekenya.rnd.tijara.databinding.FragmentAboutusDialogBinding
import com.ekenya.rnd.tijara.network.model.AboutUsItem
import com.ekenya.rnd.tijara.utils.CustomRoundBottomSheet


class AboutUsDialogFragment : CustomRoundBottomSheet() {
    private lateinit var binding: FragmentAboutusDialogBinding
    lateinit var aboutUsAdapter: AboutUsAdapter
    private var categoryList: ArrayList<AboutUsItem> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutusDialogBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initViews()

        return binding.root
    }

    private fun initViews() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        initialiseRv()
        populateAboutUsRV()

    }

    private fun populateAboutUsRV() {

        categoryList.add(AboutUsItem(getString(R.string.about),getString(R.string.aboutus),true))
      //  categoryList.add(AboutUsItem(getString(R.string.ppolicy),getString(R.string.terms_and_condition),false))
        categoryList.add(AboutUsItem(getString(R.string.ppolicy),getString(R.string.privacy_policy),false))
        aboutUsAdapter.notifyDataSetChanged()

    }


    private fun initialiseRv() {

        aboutUsAdapter = AboutUsAdapter(categoryList)
        binding.rvAboutUs.adapter = aboutUsAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvAboutUs.layoutManager=layoutManager
    }

    companion object {
        fun instance(): AboutUsDialogFragment = AboutUsDialogFragment()
    }
}