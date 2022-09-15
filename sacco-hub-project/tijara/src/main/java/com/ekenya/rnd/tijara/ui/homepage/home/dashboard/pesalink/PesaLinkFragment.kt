package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentPesaLinkBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant

import com.ekenya.rnd.tijara.adapters.layoutAdapter.PesaLinkAdapter

import kotlinx.android.synthetic.main.custom_toolbar.view.*

class PesaLinkFragment : Fragment() {
    private lateinit var binding: FragmentPesaLinkBinding
    private val items:ArrayList<BillPaymentMerchant> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPesaLinkBinding.inflate(layoutInflater)
        setupNavUp()
        addPesaLinkItems()
        binding.apply {
            rvPesaLink.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            val pesaLinkAdapter= PesaLinkAdapter(items)
            rvPesaLink.adapter=pesaLinkAdapter
        }
        return binding.root
    }

    private fun addPesaLinkItems() {
        items.add(BillPaymentMerchant(R.drawable.ic_phone,"Send to Phone", R.drawable.arrow_right))
        items.add(BillPaymentMerchant(R.drawable.ic_bank_account,"Send to Account", R.drawable.arrow_right))
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.pesa_link)
    }


}