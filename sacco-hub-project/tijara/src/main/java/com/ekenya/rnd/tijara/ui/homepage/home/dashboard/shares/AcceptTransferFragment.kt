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
import com.ekenya.rnd.tijara.databinding.AcceptTransferFragmentBinding
import com.ekenya.rnd.tijara.databinding.CancelDialogLayoutBinding
import com.ekenya.rnd.tijara.requestDTO.CancelTransferDTO
import com.ekenya.rnd.tijara.utils.onInfoDialog
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*

class AcceptTransferFragment : Fragment() {
    private lateinit var cardBinding: CancelDialogLayoutBinding
    private lateinit var viewModel: CancelTransferViewModel
private lateinit var acceptBinding:AcceptTransferFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        acceptBinding= AcceptTransferFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(requireActivity()).get(CancelTransferViewModel::class.java)
        acceptBinding.lifecycleOwner=this
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
                    etReason.error = "Enter the reason for accepting the request"
                } else {

                    val cancelTransferDTO = CancelTransferDTO()
                    cancelTransferDTO.remarks = narration
                    cancelTransferDTO.responseType = 1
                    cancelTransferDTO.transactionCode = Constants.ACCEPTCODE
                    val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                    cardBinding = CancelDialogLayoutBinding.inflate(LayoutInflater.from(context))
                    cardBinding.apply {
                         tvHeading.text=getString(R.string.acceppt_transfer)
                         tvName.text=getString(R.string.are_you_sure_you_want_to_accept)

                    }

                    cardBinding.btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    cardBinding.btnSubmit.setOnClickListener {
                        dialog.dismiss()
                        acceptBinding.progressbar.visibility = View.VISIBLE
                        acceptBinding.progressbar.tv_pbTitle.visibility = View.GONE
                        acceptBinding.progressbar.tv_pbTex.text = getString(R.string.please_wait)
                        viewModel.cancelShareTransfer(cancelTransferDTO)
                    }

                    dialog.setContentView(cardBinding.root)
                    dialog.show()
                    dialog.setCancelable(false)
                }
            }
            viewModel.status.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    acceptBinding.progressbar.visibility=View.GONE
                    when (it) {

                        1 -> {
                            SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setContentText(getString(R.string.accept_success))
                                .setConfirmClickListener { obj: Dialog ->
                                    obj.dismiss()
                                    findNavController().navigateUp()
                                    viewModel.stopObserving()
                                }
                                .show()


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
        acceptBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        acceptBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.acceppt_transfer)
    }

}