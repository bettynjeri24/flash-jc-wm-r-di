package io.eclectics.cargilldigital.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.databinding.FragmentNotificationBinding

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_notification, container, false)
         _binding = FragmentNotificationBinding.inflate(inflater, container, false)
         //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        // ToolBarMgmt.setToolbarTitle(resources.getString(R.string.top_up_title),resources.getString(R.string.allocate_money_sbtt),binding.mainLayoutToolbar,requireActivity())
         (activity as MainActivity).hideToolbar()
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}