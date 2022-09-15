package com.ekenya.rnd.tmd.ui.fragments.lookup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.dialogs.dialog_confirm.showAlertDialog
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentLookUpBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LookUpFragment : BaseDaggerFragment() {

    lateinit var binding: FragmentLookUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentLookUpBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            buttonContinue.setOnClickListener {
                lifecycleScope.launch {
                    showHideProgress("Hold on, we are looking for your account")
                    delay(3000)
                    showHideProgress(null)
                    showAlertDialog {
                        setDialogTitle("Account Successfully Found!")
                        setDialogSubtitle("We found an account with the details you provided. Would you like to activate Mobile Banking for your account?")
                        setCallbacks(object : ConfirmDialogCallBacks {
                            override fun confirm() {
                                findNavController().navigate(R.id.action_lookUpFragment_to_activateFragment)
                            }

                            override fun cancel() {
                            }
                        })
                    }.show()
                }
            }
        }
    }
}
