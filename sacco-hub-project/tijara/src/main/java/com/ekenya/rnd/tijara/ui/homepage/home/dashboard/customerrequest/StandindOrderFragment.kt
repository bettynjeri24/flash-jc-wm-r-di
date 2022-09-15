package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.SavingsAdapter
import com.ekenya.rnd.tijara.adapters.StandingOrderAdapter
import com.ekenya.rnd.tijara.databinding.StandindOrderFragmentBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.StandingOrderData
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.StandingOrderCallBack
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class StandindOrderFragment : Fragment(),StandingOrderCallBack {
private lateinit var binding:StandindOrderFragmentBinding
    private lateinit var viewModel: StandindOrderViewModel
    private lateinit var standingOrderAdapter: StandingOrderAdapter
    private val items:ArrayList<StandingOrderData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= StandindOrderFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(StandindOrderViewModel::class.java)
        standingOrderAdapter= StandingOrderAdapter(requireContext(),items,this)
        binding.lifecycleOwner=this
        binding.standingViewModel=viewModel
        binding.rvAccounts.adapter=standingOrderAdapter
        binding.rvAccounts.layoutManager=
            GridLayoutManager(requireContext(), 1)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        viewModel.standingOrderProperties.observe(viewLifecycleOwner,  {
            items.clear()
            items.addAll(it)
            binding.rvAccounts.adapter?.notifyDataSetChanged()
        })
        binding.apply {
            fabFullBtn.setOnClickListener {
                findNavController().navigate(R.id.action_standindOrderFragment_to_createStandingOrderFragment)
            }
            chatFabText.setOnClickListener {
                findNavController().navigate(R.id.action_standindOrderFragment_to_createStandingOrderFragment)
            }
        }
        handleFab()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.active_staning_order)
    }
    private fun handleFab() {
        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (scrollY > oldScrollY) {
                binding.chatFabText!!.visibility = View.GONE

            } else if (scrollX == scrollY) {
                binding.chatFabText.visibility = View.VISIBLE

            } else {
                binding.chatFabText.visibility = View.VISIBLE

            }

        })

    }

    override fun onItemSelected(stData: StandingOrderData) {
       val direction=StandindOrderFragmentDirections.actionStandindOrderFragmentToViewStandingOrderFragment(stData)
        findNavController().navigate(direction)
    }

}