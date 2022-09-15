package com.ekenya.rnd.tmd.ui.fragments.get_started

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentGetStartedBinding
import com.ekenya.rnd.tmd.utils.extendStatusBarBackground
import com.ekenya.rnd.tmd.utils.getIsLoggedIn
import com.ekenya.rnd.tmd.utils.unExtendStatusBarBackground
import javax.inject.Inject

class GetStartedFragment : BaseDaggerFragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: FragmentGetStartedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentGetStartedBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            binding.button2.setOnClickListener {
                findNavController().navigate(R.id.action_getStartedFragment_to_welcomeFragment2)
            }
            binding.textViewLogin.setOnClickListener {
                if (sharedPreferences.getIsLoggedIn()){
                    findNavController().navigate(R.id.action_getStartedFragment_to_authFragment2)
                }else{
                    findNavController().navigate(R.id.action_getStartedFragment_to_lookUpFragment)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        extendStatusBarBackground()
    }

    override fun onPause() {
        super.onPause()
        unExtendStatusBarBackground()
    }
}
