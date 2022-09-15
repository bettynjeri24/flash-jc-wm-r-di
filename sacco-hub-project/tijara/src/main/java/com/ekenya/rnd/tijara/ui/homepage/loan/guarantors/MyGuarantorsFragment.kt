package com.ekenya.rnd.tijara.ui.homepage.loan.guarantors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.MyGuarontorsAdapter
import com.ekenya.rnd.tijara.databinding.MyGuarantorsFragmentBinding
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class MyGuarantorsFragment : Fragment() {
private lateinit var binding:MyGuarantorsFragmentBinding
    private lateinit var viewModel: MyGuarantorsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MyGuarantorsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= MyGuarantorsFragmentBinding.inflate(layoutInflater)
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_myGuarantorsFragment_to_addGuarantorFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner=this
        binding.guarontorsvModel=viewModel
        viewModel.getMyGuarantors()
        setupNavUp()
        binding.apply {
            val guarontorsAdapter= MyGuarontorsAdapter(MyGuarontorsAdapter.OnClickListener {

            })
            binding.rvGuarantors.layoutManager = GridLayoutManager(requireActivity(), 1)
            binding.rvGuarantors.adapter=guarontorsAdapter
        }
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.my_guarontors)
    }

}