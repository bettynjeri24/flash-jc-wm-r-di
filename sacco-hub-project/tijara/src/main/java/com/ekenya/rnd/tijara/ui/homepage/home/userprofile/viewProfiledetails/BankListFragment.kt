package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.BankListAdapter
import com.ekenya.rnd.tijara.databinding.BankListFragmentBinding
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup.BankBottomSheetFragment
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup.NextKinViewModel
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import javax.inject.Inject

class BankListFragment : BaseDaggerFragment() {
    private lateinit var binding: BankListFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(BankListViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= BankListFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.viewmodel=viewModel
        setupNavUp()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleFab()
        binding.apply {
            fabFullBtn.setOnClickListener {
                BankBottomSheetFragment().show(requireActivity().supportFragmentManager,"bank")
            }
            addFab.setOnClickListener {
                BankBottomSheetFragment().show(requireActivity().supportFragmentManager,"bank")
            }
            rvBank.adapter= BankListAdapter(BankListAdapter.OnClickListener{
                val directions=
                    BankListFragmentDirections.actionBankListFragmentToViewBankInfoFragment(it)
                findNavController().navigate(directions)
            })
            rvBank.layoutManager=GridLayoutManager(requireActivity(),1)
        }
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
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.bank_details)
    }

}