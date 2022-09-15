package com.ekenya.rnd.tijara.ui.auth.onboarding

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.tijara.adapters.FAQsAdapter
import com.ekenya.rnd.tijara.databinding.FragmentHelpDialogBinding
import com.ekenya.rnd.tijara.network.model.AboutUsItem
import com.ekenya.rnd.tijara.utils.CustomRoundBottomSheet


class HelpDialogFragment : CustomRoundBottomSheet()
{
    private lateinit var binding: FragmentHelpDialogBinding
    lateinit var aboutUsAdapter: FAQsAdapter
    private var categoryList: ArrayList<AboutUsItem> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpDialogBinding.inflate(layoutInflater)
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

        categoryList.add(AboutUsItem("What are the accepted payment methods for loan repayments?","Currently, members can make payments via Mobile money e.g. M-PESA and or pay directly from their prime account.",true))
        categoryList.add(AboutUsItem("How is a loan disbursed to a member?","Upon successful approval by the admin, the loan is instantly disbursed to the member's prime account.",false))
        categoryList.add(AboutUsItem("Can i join more than one sacco?","Yes, upon account lookup, member are presented with a list of sacco that they wish to join,select one and continue with self registration.",false))
        aboutUsAdapter.notifyDataSetChanged()

    }


    private fun initialiseRv() {

        aboutUsAdapter = FAQsAdapter(categoryList)
        binding.rvFAQs.adapter = aboutUsAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvFAQs.layoutManager=layoutManager
    }


    companion object {
        fun instance(): HelpDialogFragment = HelpDialogFragment()
    }
}