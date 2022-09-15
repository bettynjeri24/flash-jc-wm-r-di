package io.eclectics.cargilldigital.ui.confirmation

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
import io.eclectics.cargilldigital.databinding.FragmentCashoutConfirmationBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class CashoutConfirmation : Fragment() {
    private var _binding: FragmentCashoutConfirmationBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navOptions:NavOptions
    lateinit var  confObj:ConfirmationObj
    lateinit var transRequest:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCashoutConfirmationBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    var confirmJson = requireArguments().getString("confirm")//
    confObj = NetworkUtility.jsonResponse(confirmJson!!)
    transRequest = requireArguments().getString("requestJson")!!

    LoggerHelper.loggerError("confirm","${confObj.amount}-${confObj.recipient}")
    getTransType()
    binding.btnSubmit.setOnClickListener {
        var bundle = Bundle()
        bundle.putString("requestJson",transRequest)
        findNavController().navigate(R.id.nav_pinFragment,bundle,navOptions)
    }
}

private fun getTransType() {
    //"fttelco"->{confirmSe}
    when(confObj.transType){
        "ebooking"->{confirmEvalueBooking()}

        else ->{populateDefault()}
    }
}

private fun confirmEvalueBooking() {
    var requestJObejct = JSONObject(transRequest)
    binding.tvTitle.text = confObj.title
    binding.amountTextView.text  =confObj.amount
    binding.transactionCostTextView.text = confObj.charges
    //binding.exciseDutyAmountTextView.text = requestJObejct.getString("accountNumber")
   // binding.exciseDutyAmountDesTextView.text = resources.getString(R.string.account_number)
    binding.etDebitAccount.text = confObj.recipient
    binding.showBalanceTextView.text = resources.getString(R.string.account_name)
}

fun populateDefault(){
    binding.tvTitle.text = confObj.title
    binding.amountTextView.text  =confObj.amount
    binding.transactionCostTextView.text = confObj.amount
    //binding.exciseDutyAmountTextView.text = confObj.amount
    binding.etDebitAccount.text = confObj.recipient
}
}