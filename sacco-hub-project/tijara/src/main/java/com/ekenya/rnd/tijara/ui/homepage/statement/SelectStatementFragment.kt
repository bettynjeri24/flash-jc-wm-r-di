package com.ekenya.rnd.tijara.ui.homepage.statement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentSelectStatementBinding
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class SelectStatementFragment : Fragment() {
    private lateinit var binding:FragmentSelectStatementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSelectStatementBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        binding.apply {
            CLGetMinistat.setOnClickListener {
                findNavController().navigate(R.id.action_selectStatementFragment_to_miniStatementFragment)
            }
            CLGetFullStat.setOnClickListener {
                findNavController().navigate(R.id.action_selectStatementFragment_to_statementFragment)

            }
        }
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.statements)
    }
}