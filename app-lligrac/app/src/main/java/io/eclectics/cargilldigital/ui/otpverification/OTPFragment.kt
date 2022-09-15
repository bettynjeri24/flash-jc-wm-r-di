package io.eclectics.cargilldigital.ui.otpverification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentOtpBinding
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class OTPFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    lateinit var request:String
    @Inject
    lateinit var navOptions:NavOptions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.verification_code),resources.getString(R.string.add_beneficiary_subbtle),binding.mainLayoutToolbar,requireActivity())
        return binding.root
        // return inflater.inflate(R.layout.fragment_transfer_tobank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         request = requireArguments().getString("requestJson")!!
        var recipientNumber = requireArguments().getString("recipientNumber")
        binding.tvPhoneNo.text = recipientNumber
        binding.btnVerifyOtp.setOnClickListener {

            submitRequest()
        }
    }

    private fun submitRequest() {

        var jsonRequest = JSONObject(request)
        jsonRequest.put("otp",binding.edtOtp.text.toString())
        var bundle = Bundle()//requestJson
        bundle.putString("requestJson",jsonRequest.toString())
        findNavController().navigate(R.id.nav_pinFragment,bundle,navOptions)

    }

}