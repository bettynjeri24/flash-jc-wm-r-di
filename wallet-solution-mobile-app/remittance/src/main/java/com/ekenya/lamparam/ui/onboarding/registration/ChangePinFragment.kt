package com.ekenya.lamparam.ui.onboarding.registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.databinding.FragmentChangePinBinding
import com.ekenya.lamparam.network.DefaultResponse
import com.ekenya.lamparam.network.Status
import com.ekenya.lamparam.utilities.GlobalMethods
import kotlinx.android.synthetic.main.header_layout.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePinFragment] factory method to
 * create an instance of this fragment.
 */
class ChangePinFragment : Fragment() {

    private var _binding: FragmentChangePinBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //initialized for all fragments
    private val onboardingViewModel: OnBoardingViewModel by activityViewModels() {viewModelFactory}

    @Inject
    lateinit var globalMethods: GlobalMethods

    private lateinit var otp: String
    private lateinit var code: String
    private lateinit var phone: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePinBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = getString(R.string.create_pin)

        onboardingViewModel.userData.observe(viewLifecycleOwner, {
            it?.let {
                otp = it.otp
                phone = it.phoneNumber
            }
        })

        binding.btnVerification.setOnClickListener {
            if (valid()) {
                sendRequest()
            }
        }

    }

    private fun sendRequest() {
        onboardingViewModel.changePIN(otp, code, phone).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        globalMethods.loader(requireActivity(), 1)
                        resource.data?.let { data -> parseData(data.body()!!) }
                    }
                    Status.ERROR -> {
                        globalMethods.loader(requireActivity(), 1)
                        globalMethods.transactionError(requireActivity(), it.message!!)
                    }
                    Status.LOADING -> {
                        globalMethods.loader(requireActivity(), 0)
                    }
                }
            }
        })
    }


    private fun parseData(result: DefaultResponse) {
        when(result.responseCode){
            "00"->{
               globalMethods.confirmTransactionEnd(requireActivity(), result.responseMessage){
                   it.dismiss()
                   it.dismissWithAnimation()
                   findNavController().navigate(R.id.nav_pin)
               }
            }else->{
            globalMethods.transactionError(requireActivity(), result.responseMessage)
        }
        }
    }

    private fun valid(): Boolean {
        code = binding.etPin.text.toString()
        val code2 = binding.etConfirmPin.text.toString()

        if (code.isEmpty()) {
            binding.tlPin.error = getString(R.string.input_required)
            return false
        }

        if (code2.isEmpty()) {
            binding.tlConfirmPin.error = getString(R.string.input_required)
            return false
        }

        if (code == "0000" || code == "1111"){
            Toast.makeText(requireContext(), "Please select a stronger pin", Toast.LENGTH_SHORT).show()
            return false
        }

        if (code != code2) {
            Toast.makeText(requireContext(), "PIN mismatch", Toast.LENGTH_SHORT).show()
            return false
        }

        if(code == otp){
            Toast.makeText(requireContext(), "Please create a new PIN", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}