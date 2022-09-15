package io.eclectics.cargilldigital.ui.buyerprofile.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.databinding.FragmentDashboardTabsBinding

@AndroidEntryPoint
class DashboardTabs : Fragment() {
    private var _binding:FragmentDashboardTabsBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = DashboardTabs()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_dashboard_tabs, container, false)
        _binding = FragmentDashboardTabsBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // this.view = view
        val fragmentAdapter = BuyerHomeTabsAdapter(childFragmentManager, lifecycle,context)
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