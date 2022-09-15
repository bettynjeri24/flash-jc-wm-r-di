package com.ekenya.rnd.tmd.ui.fragments.activate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentActivateBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivateFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentActivateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentActivateBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        binding.apply {
            button8.setOnClickListener {
                lifecycleScope.launch {
                    showHideProgress("We are activating your account")
                    delay(3000)
                    showHideProgress(null)
                    findNavController().navigate(R.id.action_activateFragment_to_verificationCodeFragment)
                }
            }
        }
    }
}
