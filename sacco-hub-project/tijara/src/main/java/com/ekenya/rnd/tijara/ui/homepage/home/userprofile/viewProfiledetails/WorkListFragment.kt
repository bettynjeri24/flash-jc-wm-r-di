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
import com.ekenya.rnd.tijara.adapters.WorkListAdapter
import com.ekenya.rnd.tijara.databinding.WorkListFragmentBinding
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup.WorkBottomSheetFragment
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import javax.inject.Inject

class WorkListFragment : BaseDaggerFragment() {
    private lateinit var binding: WorkListFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(WorkListViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= WorkListFragmentBinding.inflate(layoutInflater)

        binding.lifecycleOwner=this
        binding.viewmodel=viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleFab()
        setupNavUp()
        binding.apply {
            binding.fabFullBtn.setOnClickListener {
                WorkBottomSheetFragment().show(requireActivity().supportFragmentManager, "work")
            }
            binding.addFab.setOnClickListener {
                WorkBottomSheetFragment().show(requireActivity().supportFragmentManager, "work")
            }
            rvWork.adapter= WorkListAdapter(WorkListAdapter.OnClickListener{
                val directions=
                    WorkListFragmentDirections.actionWorkListFragmentToViewWorkInfoFragment(it)
                findNavController().navigate(directions)
            })
            rvWork.layoutManager= GridLayoutManager(requireActivity(),1)

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
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.work_details)
    }




}