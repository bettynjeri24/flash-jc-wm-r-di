package com.ekenya.lamparam.ui.secretPin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.main.LampMainViewModel
import com.ekenya.lamparam.databinding.FragmentSecretPinBinding
import com.ekenya.lamparam.model.CompleteTransactionReq
import com.ekenya.lamparam.model.FundsTransferReceiveMoneyReq
import com.ekenya.lamparam.model.response.ConfirmTransactionResponse
import com.ekenya.lamparam.utilities.UtilityClass
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Constants


class SecretPin : Fragment() {

    private lateinit var binding: FragmentSecretPinBinding
    private var digit1 = ""
    private var digit2 = ""
    private var digit3 = ""
    private var digit4 = ""
    private var digit5 = ""
    private var inputtedOTP = ""
    lateinit var lampMainViewModel: LampMainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var confBundle = requireArguments()
        var otp = confBundle.getString("otp").toString()
        Toast.makeText(requireContext(),otp,Toast.LENGTH_LONG).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSecretPinBinding.inflate(inflater)
        lampMainViewModel = ViewModelProvider((activity as LampMainActivity)).get(LampMainViewModel::class.java)


        return binding.root
    }

    private fun getInputedOTP()
    {
        digit1 = binding.etc1.text.toString()
        digit2 = binding.etc2.text.toString()
        digit3 = binding.etc3.text.toString()
        digit4 = binding.etc4.text.toString()
        digit5 = binding.etc5.text.toString()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTimeout.text = getString(R.string.resend_code_timeout)

        binding.btnSecretKey.setOnClickListener {
            getInputedOTP()
            inputtedOTP = digit1+digit2+digit3+digit4+digit5
            Toast.makeText(requireContext(),inputtedOTP,Toast.LENGTH_LONG).show()
            completeTransaction(
                CompleteTransactionReq(
                    inputtedOTP,
                    SharedPreferencesManager.getPhoneNumber(requireContext()).toString()
                )
            )

        }
    }

    private fun completeTransaction(req:CompleteTransactionReq)
    {
        Constants.callDialog2("Please wait...", requireContext())
        lampMainViewModel.doCompleteTransReq(req).observe(viewLifecycleOwner) { myAPIResponse ->
            if(myAPIResponse.requestName == "CompleteTransactionReq") {
                if (myAPIResponse.code == 200)
                {
                    val resp = myAPIResponse.responseObj as ConfirmTransactionResponse
                    if(resp.status == 200){

                        doFundsTransfer(FundsTransferReceiveMoneyReq(
                            inputtedOTP,
                            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
                            "WITHDRAW2WALLET",
                            "",
                            SharedPreferencesManager.getWalletAccNumber(requireContext())!!
                        ))
                    }

                }

            }
        }
    }

    private fun doFundsTransfer(req: FundsTransferReceiveMoneyReq)
    {
        lampMainViewModel.doFundsTransferReq(req).observe(viewLifecycleOwner) { myAPIResponse ->
            if(myAPIResponse.requestName == "mFundsTransferReceiveMoneyReq") {
                Constants.cancelDialog()
                if (myAPIResponse.code == 200)
                {
                    val resp = myAPIResponse.responseObj as ConfirmTransactionResponse
                    if(resp.status == 200){

                        UtilityClass().confirmTransactionEnd(
                           binding.root,
                            "Info",
                            resp.message,
                            requireActivity(),
                            ::toHomeScreen
                        )
                    }

                }

            }
        }
    }

    fun toHomeScreen()
    {
        requireActivity().finish()

    }

}