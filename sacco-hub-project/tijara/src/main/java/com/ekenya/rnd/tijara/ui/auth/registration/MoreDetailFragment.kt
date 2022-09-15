package com.ekenya.rnd.tijara.ui.auth.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentMoreDetailBinding
import com.ekenya.rnd.tijara.utils.FieldValidators
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*

class MoreDetailFragment : Fragment() {
    private lateinit var viewModel:RegistrationViewModel
    private lateinit var binding:FragmentMoreDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentMoreDetailBinding.inflate(layoutInflater)
        viewModel= ViewModelProvider(requireActivity()).get(RegistrationViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnContinue.setOnClickListener {
                val validMsg = FieldValidators.VALIDINPUT
                val mail=etEmail.text.toString().trim()
                val kra=etKra.text.toString().trim()
                val validEmail= FieldValidators().isEmailValid(mail)
                when{
                    kra.isEmpty() -> {
                        tlKra.error=getString(R.string.required)
                    }
                    !validEmail.contentEquals(validMsg)-> {
                        tlKra.error=""
                        etEmail.isFocusable=true
                        tlEmail.error=validEmail
                      }
                    else->{
                      tlEmail.error=""
                      tlKra.error=""
                        binding.btnContinue.isEnabled=false
                        requireActivity().window.statusBarColor = resources.getColor(R.color.spinkit_color)
                        binding.progressr.visibility=View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility=View.GONE
                        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                        if (Constants.FromID==0){
                            viewModel.uploadPhotos(kra,mail)
                        }else{
                            viewModel.upPassPortPhotos(kra,mail)
                        }
                      }

                }
            }
            viewModel.statusCode.observe(viewLifecycleOwner) {
                if (null != it) {
                    viewModel.stopObserving()
                    binding.btnContinue.isEnabled = true
                    requireActivity().window.statusBarColor = resources.getColor(R.color.white)
                    binding.progressr.makeGone()

                    when (it) {
                        1 -> {
                            viewModel.stopObserving()
                            binding.progressr.makeGone()
                            binding.btnContinue.isEnabled = true
                            findNavController().navigate(R.id.action_moreDetailFragment_to_newPin)
                        }
                        0 -> {
                            viewModel.stopObserving()
                            binding.btnContinue.isEnabled = true
                            onInfoDialog(requireContext(), viewModel.photoMessage.value)
                            binding.progressr.makeGone()

                        }

                        else -> {
                            viewModel.stopObserving()
                            binding.btnContinue.isEnabled = true
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))
                            binding.progressr.makeGone()

                        }
                    }
                }
            }
        }
    }


}