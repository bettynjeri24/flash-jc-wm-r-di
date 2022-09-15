package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.SavingAccountAdapter
import com.ekenya.rnd.tijara.adapters.SavingsAdapter
import com.ekenya.rnd.tijara.databinding.FragmentSavingsBinding
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.ui.homepage.home.SavingViewModel
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.white_bg_spinkit.view.*

class SavingsFragment : Fragment() {
  private lateinit var binding:FragmentSavingsBinding
  private lateinit var viewModel:SavingViewModel
  private lateinit var savingsAdapter: SavingsAdapter
  private val items:ArrayList<SavingAccountData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentSavingsBinding.inflate(layoutInflater)
        viewModel=ViewModelProvider(requireActivity()).get(SavingViewModel::class.java)
        savingsAdapter= SavingsAdapter(requireContext(),items)
        binding.lifecycleOwner=this
        binding.savingViewModel=viewModel
        binding.rvAccounts.adapter=savingsAdapter
        binding.rvAccounts.layoutManager=
            GridLayoutManager(requireContext(), 1)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        binding.apply {
            val threeBounce: Sprite = ThreeBounce()
            progressSpinkit.SKView.setIndeterminateDrawable(threeBounce)
            progressSpinkit.visibility=View.VISIBLE

        }
        viewModel.savingAccountProperties.observe(viewLifecycleOwner,  {
           items.clear()
            items.addAll(it)
            binding.rvAccounts.adapter?.notifyDataSetChanged()
        })
        viewModel.status.observe(viewLifecycleOwner,  {
            if (null!=it){
                binding.progressSpinkit.visibility=View.GONE
                when(it){
                    1->{
                        binding.progressSpinkit.visibility=View.GONE
                    }
                    0->{
                        binding.progressSpinkit.visibility=View.GONE
                    }
                    else->{
                        binding.progressSpinkit.visibility=View.GONE
                    }
                }
            }
        })
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.saving_accounts)
    }

}