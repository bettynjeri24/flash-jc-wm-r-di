package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.CancelDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.CancelTransferFragmentBinding
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.ShareDialogLayoutBinding
import com.ekenya.rnd.tijara.requestDTO.CancelTransferDTO
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.showSuccessSnackBar
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*

class CancelTransferFragment : Fragment() {
    private lateinit var cancelBinding:CancelTransferFragmentBinding
    private lateinit var cardBinding: CancelDialogLayoutBinding
    private lateinit var viewModel: CancelTransferViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cancelBinding= CancelTransferFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(requireActivity()).get(CancelTransferViewModel::class.java)
        cancelBinding.lifecycleOwner=this
        setupNavUp()
        cancelBinding.apply {
            if (Constants.TOREJECT == 0) {
                tvTextTitle.text=getString(R.string.reason_for_reject)
                cancelBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.reject)
            } else {
                //cancel
                tvTextTitle.text=getString(R.string.reason_for_cancel)
                cancelBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.cancel_transfer)
            }
        }
        return cancelBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelBinding.apply {

            btnContinue.setOnClickListener {
                val narration=etReason.text.toString().trim()
                if (narration.isEmpty()) {
                    etReason.isFocusable
                    etReason.error = "Enter the reason for canceling the request"
                } else {
                    val cancelTransferDTO = CancelTransferDTO()
                    cancelTransferDTO.remarks = narration
                    if (Constants.TOREJECT==0){
                        cancelTransferDTO.responseType = 3
                        cancelTransferDTO.transactionCode = Constants.DETAILCODE
                    }
                    if (Constants.TOREJECT==1) {
                        cancelTransferDTO.responseType = 2
                        cancelTransferDTO.transactionCode = Constants.CANCELCODE
                    }
                    val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                    cardBinding = CancelDialogLayoutBinding.inflate(LayoutInflater.from(context))
                    cardBinding.apply {
                        if (Constants.TOREJECT==0){
                            tvHeading.text=getString(R.string.reject)
                            tvName.text=getString(R.string.are_you_sure_you_want_to_reject)
                        }else{
                            tvHeading.text=getString(R.string.cancel_transfer)
                            tvName.text=getString(R.string.are_you_sure_you_want_to_cancel_this_transfer_of_shares_process)
                        }

                    }

                    cardBinding.btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    cardBinding.btnSubmit.setOnClickListener {
                        dialog.dismiss()
                        cancelBinding.progressbar.visibility = View.VISIBLE
                        cancelBinding.progressbar.tv_pbTitle.visibility = View.GONE
                        cancelBinding.progressbar.tv_pbTex.text = getString(R.string.please_wait)
                        viewModel.cancelShareTransfer(cancelTransferDTO)
                    }

                    dialog.setContentView(cardBinding.root)
                    dialog.show()
                    dialog.setCancelable(false)
                }
            }
            viewModel.status.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    cancelBinding.progressbar.visibility=View.GONE
                    when (it) {

                        1 -> {
                            if (Constants.TOREJECT==0){
                                SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)

                                    .setContentText(getString(R.string.reject_success))
                                    .setConfirmClickListener { obj: Dialog ->
                                        obj.dismiss()
                                        findNavController().navigateUp()
                                        viewModel.stopObserving()
                                    }
                                    .show()
                            }else{
                                SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)

                                    .setContentText(getString(R.string.cancel_success))
                                    .setConfirmClickListener { obj: Dialog ->
                                        obj.dismiss()
                                        findNavController().navigateUp()
                                        viewModel.stopObserving()
                                    }
                                    .show()
                            }



                        }
                        0 -> {
                            onInfoDialog(requireContext(),viewModel.statusMessage.value)
                        }
                        else -> {
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))

                        }
                    }
                }
            })
        }
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        cancelBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)

    }

}