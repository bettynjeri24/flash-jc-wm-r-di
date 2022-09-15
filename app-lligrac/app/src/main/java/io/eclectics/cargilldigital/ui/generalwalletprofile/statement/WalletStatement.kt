package io.eclectics.cargilldigital.ui.generalwalletprofile.statement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentWalletStatementBinding
import io.eclectics.cargilldigital.utils.ToolBarMgmt

@AndroidEntryPoint
class WalletStatement : Fragment() {
    private var _binding: FragmentWalletStatementBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = WalletStatement()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletStatementBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.transaction_statement),resources.getString(R.string.view_mini_full_statement),binding.mainLayoutToolbar,requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // this.view = view
        val fragmentAdapter = AccountsTabsAdapter(childFragmentManager, lifecycle)
        binding.viewpagerMain.adapter = fragmentAdapter
        TabLayoutMediator(binding.tabsMain, binding.viewpagerMain) { tab, position ->
            tab.text = fragmentAdapter.getPageTitle(position)
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}