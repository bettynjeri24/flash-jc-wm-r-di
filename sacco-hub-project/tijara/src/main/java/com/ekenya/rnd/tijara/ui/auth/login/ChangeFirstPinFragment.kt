package com.ekenya.rnd.tijara.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.ChangeFirstPinFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.ChangeFirstPinDTO
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import javax.inject.Inject

class ChangeFirstPinFragment : BaseDaggerFragment() {
    private lateinit var binding: ChangeFirstPinFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ChangeFirstPinViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ChangeFirstPinFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.firstPassViewModel =viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.makeGone()
                when (it) {
                    1 -> {
                        binding.progressr.makeGone()
                       findNavController().navigate(R.id.action_changeFirstPinFragment_to_loginPinFragment)
                        viewModel.stopObserving()
                    }
                    0-> {
                        binding.progressr.makeGone()
                        onInfoDialog(context,   viewModel.statusMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {

                    }
                }
            }
        })


        binding.apply {
            ivBack.setOnClickListener {findNavController().navigateUp() }
            binding.btnChangePass.setOnClickListener {
                val oldPass: String = binding.etOldPass.text.toString()
                val password: String = binding.etPassword.text.toString()
                val passwordConfirm: String = binding.etPasswordConfirm.text!!.toString()

               if (oldPass.isEmpty()) {
                    tlOldPass.error = getString(R.string.please_enter_the_old_password)
                } else if (password.isEmpty()) {

                    tlOldPass.error=""
                    etPasswordTi.error = getString(R.string.please_enter_the_new_password)
                    etPassword.isFocusable
                }else if (passwordConfirm.isEmpty()) {
                    etPasswordTi.error = ""
                    etPasswordConfirmTi.error = getString(R.string.please_confirm_password)
                    etPasswordConfirm.isFocusable
                } else if (password != passwordConfirm){
                    etPasswordConfirmTi.error =""
                        onInfoDialog2(context,   getString(R.string.password_do_not_match))
                    etPasswordConfirm.isFocusable
                }
                else {
                    etPasswordTi.clearFocus()
                    etPasswordConfirmTi.clearFocus()
                    val changeFirstPinDTO= ChangeFirstPinDTO()
                    changeFirstPinDTO.old_password=oldPass
                    changeFirstPinDTO.new_password=password
                   binding.progressr.makeVisible()
                   binding.progressr.tv_pbTitle.makeGone()
                   binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                 viewModel!!.setFirstPin(changeFirstPinDTO)
                }

            }
        }
        handleBackButton()
    }
    private fun handleBackButton() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }



}