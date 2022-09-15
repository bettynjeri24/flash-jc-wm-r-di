package io.eclectics.cargilldigital.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentProfileContactUsBinding
import io.eclectics.cargilldigital.utils.ToolBarMgmt

@AndroidEntryPoint
class ProfileContactUs : Fragment() {
    private var _binding: FragmentProfileContactUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileContactUsBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.contact_us),resources.getString(R.string.contact_us),binding.mainLayoutToolbar,requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //show contacts nd social media for

    }
}