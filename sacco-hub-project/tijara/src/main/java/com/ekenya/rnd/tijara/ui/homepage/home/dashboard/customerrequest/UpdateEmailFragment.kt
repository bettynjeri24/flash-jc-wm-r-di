package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.CancelDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentUpdateEmailBinding
import com.ekenya.rnd.tijara.requestDTO.ComplaintDTO
import com.ekenya.rnd.tijara.requestDTO.EmailDTO
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*


class UpdateEmailFragment : Fragment() {
private lateinit var binding:FragmentUpdateEmailBinding
    private lateinit var cardBinding: CancelDialogLayoutBinding
    private lateinit var viewModel: MemberShipCardViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUpdateEmailBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(MemberShipCardViewModel::class.java)
        binding.etOldmail.setText(Constants.EMAILADDRESS)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSubmit.setOnClickListener {
                val oldMail = etOldmail.text.toString().trim()
                val newMail = etNewMail.text.toString().trim()
                if (oldMail.isEmpty()) {
                    tlOldMail.error = getString(R.string.required)
                }else if (newMail.isEmpty()) {
                    tlNewMail.error = getString(R.string.required)
                } else {
                    val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
                    cardBinding = CancelDialogLayoutBinding.inflate(LayoutInflater.from(context))
                    cardBinding.apply {
                        tvHeading.text = "Confirm request"
                        tvName.text = "You are about to update your email address"
                        tvFromValur.text= FormatDigit.formatDigits("KES 0.00")
                    }

                    cardBinding.btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    cardBinding.btnSubmit.setOnClickListener {
                        dialog.dismiss()
                        tlOldMail.error = ""
                        tlNewMail.error = ""
                        val emailDTO=EmailDTO()
                        emailDTO.newEmail=newMail
                        binding.progressr.visibility = View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility = View.GONE
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        binding.btnSubmit.isEnabled = false
                        viewModel.updateEmail(emailDTO)
                    }

                    dialog.setContentView(cardBinding.root)
                    dialog.show()
                    dialog.setCancelable(false)

                }
            }
            viewModel.statusEmaile.observe(viewLifecycleOwner,  {
                if (null != it) {
                    binding.btnSubmit.isEnabled=true
                    binding.progressr.visibility=View.GONE
                    viewModel.stopObserving()
                    when (it) {

                        1 -> {
                            binding.btnSubmit.isEnabled=true
                            viewModel.stopObserving()
                            binding.progressr.visibility=View.GONE
                            val builder = AlertDialog.Builder(requireContext())
                                .setTitle("successful!")
                                .setMessage("Your email has been updated successfully")
                                .setPositiveButton("OK") { _, which ->
                                    findNavController().navigateUp()
                                }
                                .setIcon(R.drawable.ic_check_sucess)
                                .setCancelable(false)
                            builder .show()
                            viewModel.stopObserving()
                        }
                        0 -> {
                            binding.btnSubmit.isEnabled=true
                            binding.progressr.visibility=View.GONE
                            onInfoDialog(requireContext(),viewModel.emailMessage.value)
                            viewModel.stopObserving()
                        }
                        else -> {
                            binding.btnSubmit.isEnabled=true
                            binding.progressr.visibility=View.GONE
                            onInfoDialog(requireContext(), getString(R.string.error_occurred))
                            viewModel.stopObserving()

                        }
                    }
                }
            })
        }
        setupNavUp()

    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = "Update Email"
    }


}