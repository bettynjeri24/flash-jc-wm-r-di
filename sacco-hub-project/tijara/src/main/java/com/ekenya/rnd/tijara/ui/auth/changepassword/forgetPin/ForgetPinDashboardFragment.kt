package com.ekenya.rnd.tijara.ui.auth.changepassword.forgetPin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentForgetPinDashboardBinding
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ForgetPinDashboardFragment : Fragment() {
    private lateinit var binding:FragmentForgetPinDashboardBinding
    private lateinit var viewModel: SelectIDTypeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentForgetPinDashboardBinding.inflate(layoutInflater)
        viewModel=ViewModelProvider(requireActivity()).get(SelectIDTypeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            CLSecQuiz.setOnClickListener {
                viewModel.setResetOption("securityQuiz")
                findNavController().navigate(R.id.action_forgetPinDashboardFragment_to_securityQuizFragment)
            }
            CLIdentity.setOnClickListener {
                viewModel.setResetOption("verifyIdentity")
                findNavController().navigate(R.id.action_forgetPinDashboardFragment_to_selectIDTypeFragment)
            }
            CLLocation.setOnClickListener {
                toastyInfos("Coming Soon")
                viewModel.setResetOption("visitBranch")
            }
        }
    }



}
