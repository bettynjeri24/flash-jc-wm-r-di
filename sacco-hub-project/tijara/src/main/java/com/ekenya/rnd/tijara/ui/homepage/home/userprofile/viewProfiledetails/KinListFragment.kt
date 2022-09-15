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
import com.ekenya.rnd.tijara.adapters.KinsListAdapter
import com.ekenya.rnd.tijara.databinding.KinListFragmentBinding
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup.KinBottomSheetFragment
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup.NextKinViewModel
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import javax.inject.Inject

class KinListFragment : BaseDaggerFragment() {
    private lateinit var binding: KinListFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(KinListViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= KinListFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.viewmodel=viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            fabBtn.setOnClickListener {
                KinBottomSheetFragment().show(requireActivity().supportFragmentManager,"kin")
            }
            addFab.setOnClickListener {
                KinBottomSheetFragment().show(requireActivity().supportFragmentManager,"kin")
            }
            rvKin.adapter= KinsListAdapter(KinsListAdapter.OnClickListener{
                val directions=
                    KinListFragmentDirections.actionKinListFragmentToViewNextKinFragment(it)
                findNavController().navigate(directions)
            })
         rvKin.layoutManager= GridLayoutManager(requireActivity(),1)

        }
        setupNavUp()
        handleFab()
    }
    private fun handleFab() {
        binding.svScrool.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

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
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.next_of_kin)
    }



}