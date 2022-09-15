package com.ekenya.rnd.tijara.ui.homepage.statement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.SavingAccountAdapter
import com.ekenya.rnd.tijara.databinding.FragmentStatementAccountBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.white_bg_spinkit.view.*
import javax.inject.Inject

class StatementAccountFragment : BaseDaggerFragment() {
    private lateinit var savingAdapter:SavingAccountAdapter
    private lateinit var binding:FragmentStatementAccountBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val fullViewmodel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(FullStatementViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentStatementAccountBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.statViewmodel=fullViewmodel
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
        fullViewmodel.savingAccountProperties.observe(viewLifecycleOwner, Observer {
            savingAdapter= SavingAccountAdapter(requireContext(),it!!)
            binding.rvAccounts.adapter=savingAdapter
            binding.rvAccounts.adapter?.notifyDataSetChanged()
            binding.rvAccounts.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            binding.rvAccounts.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.HORIZONTAL))

        })
        fullViewmodel.status.observe(viewLifecycleOwner, Observer {
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
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.select_account)
    }

}