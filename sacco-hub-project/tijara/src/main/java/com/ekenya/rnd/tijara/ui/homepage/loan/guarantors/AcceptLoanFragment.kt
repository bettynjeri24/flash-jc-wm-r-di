package com.ekenya.rnd.tijara.ui.homepage.loan.guarantors

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.AcceptTransferFragmentBinding
import com.ekenya.rnd.tijara.databinding.CancelDialogLayoutBinding
import com.ekenya.rnd.tijara.requestDTO.ActLoanDTO
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.toastySuccess
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*

class AcceptLoanFragment : Fragment() {
    private lateinit var cardBinding: CancelDialogLayoutBinding
    private lateinit var viewModel: LoanGuarantedViewModel
private lateinit var acceptBinding:AcceptTransferFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        acceptBinding= AcceptTransferFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(requireActivity()).get(LoanGuarantedViewModel::class.java)
        acceptBinding.lifecycleOwner=this
        if (Constants.ISFROMBLOOKUP==1){
            acceptBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.accept_request)
            acceptBinding.tvTextTitle.text=getString(R.string.reason_for_accept_loan)
        }else{
            acceptBinding.tvTextTitle.text=getString(R.string.reason_for_reject_loan)
            acceptBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.reject_request)

        }

        setupNavUp()
        return acceptBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        acceptBinding.apply {

            btnContinue.setOnClickListener {
                val narration=etReason.text.toString().trim()
                if (narration.isEmpty()) {
                    etReason.isFocusable
                    etReason.error = getString(R.string.required)
                } else {

                    val actLoanDTO=ActLoanDTO()
                    actLoanDTO.remarks = narration
                    if (Constants.ISFROMBLOOKUP==1){
                        actLoanDTO.responseType = 1
                    }else{
                        actLoanDTO.responseType = 2
                    }

                        actLoanDTO.requestId = Constants.GENDER_ID.toString().toInt()


                    val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                    cardBinding = CancelDialogLayoutBinding.inflate(LayoutInflater.from(context))
                    cardBinding.apply {
                        if (Constants.ISFROMBLOOKUP==1){
                            tvHeading.text=getString(R.string.accept_request)
                            tvName.text=getString(R.string.are_you_sure_you_want_to_accept_loan)
                        }else{
                            tvHeading.text=getString(R.string.reject_request)
                            tvName.text=getString(R.string.are_you_sure_you_want_to_reject_loan)
                        }


                    }

                    cardBinding.btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    cardBinding.btnSubmit.setOnClickListener {
                        dialog.dismiss()
                        acceptBinding.progressbar.visibility = View.VISIBLE
                        acceptBinding.progressbar.tv_pbTitle.visibility = View.GONE
                        acceptBinding.progressbar.tv_pbTex.text = getString(R.string.please_wait)
                        viewModel.actOnLoan(actLoanDTO)
                    }

                    dialog.setContentView(cardBinding.root)
                    dialog.show()
                    dialog.setCancelable(false)
                }
            }
            viewModel.statusCan.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    acceptBinding.progressbar.visibility=View.GONE
                    when (it) {

                        1 -> {
                            toastySuccess(requireContext(),getString(R.string.successful))
                            findNavController().navigateUp()
                            acceptBinding.progressbar.visibility=View.GONE
                            viewModel.stopObserving() }
                        0 -> {
                            onInfoDialog(requireContext(),viewModel.canMessage.value)
                            acceptBinding.progressbar.visibility=View.GONE
                            viewModel.stopObserving()
                        }
                        else -> {
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))
                            acceptBinding.progressbar.visibility=View.GONE
                            viewModel.stopObserving()

                        }
                    }
                }
            })
        }
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        acceptBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}