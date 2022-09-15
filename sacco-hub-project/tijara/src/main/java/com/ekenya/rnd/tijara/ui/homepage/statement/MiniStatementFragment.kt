package com.ekenya.rnd.tijara.ui.homepage.statement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.MinistatementAdapter
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.AllAccountAdapter
import com.ekenya.rnd.tijara.databinding.MiniStatementFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.MiniStatementDTO
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup.BankBottomSheetFragment
import com.ekenya.rnd.tijara.utils.showToast

import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.white_bg_spinkit.view.*
import javax.inject.Inject

class MiniStatementFragment : BaseDaggerFragment() {
    private lateinit var binding: MiniStatementFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(MiniStatementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= MiniStatementFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel
            val miniStatementDTO= MiniStatementDTO()
            miniStatementDTO.accountId=Constants.SAVINGAID
            miniStatementDTO.productId=Constants.SAVINGPID
            viewModel.getMiniStatement(miniStatementDTO)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()

        binding.apply {
          //  progressSpinkit.visibility=View.VISIBLE
            val miniStatAdapter= MinistatementAdapter()
            rvMiniStat.adapter=miniStatAdapter
            rvMiniStat.layoutManager = GridLayoutManager(activity, 1)
            handleFab()
            fabFullBtn.setOnClickListener {
                showToast(requireContext(),"CLick")
            }
            addFab.setOnClickListener {
                showToast(requireContext(),"CLick")
            }
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
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.ministatement)
    }

    override fun onResume() {
        super.onResume()
        /*binding.apply {
            val threeBounce: Sprite = ThreeBounce()
            progressSpinkit.SKView.setIndeterminateDrawable(threeBounce)
            progressSpinkit.visibility = View.VISIBLE
        }*/
    }
}