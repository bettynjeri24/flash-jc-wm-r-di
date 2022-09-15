package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ChequeBranchAdapter
import com.ekenya.rnd.tijara.databinding.CancelDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentComplaintsBinding
import com.ekenya.rnd.tijara.requestDTO.ComplaintDTO
import com.ekenya.rnd.tijara.utils.FormatDigit
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.onInfoDialog
import com.ekenya.rnd.tijara.utils.toastyInfos
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*

class ComplaintsFragment : Fragment() {
private lateinit var binding:FragmentComplaintsBinding
    private lateinit var cardBinding: CancelDialogLayoutBinding
    private lateinit var viewModel: MemberShipCardViewModel
    var orgBranchName=""
    var orgBranchId=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentComplaintsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(MemberShipCardViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chequeBranch.observe(viewLifecycleOwner,  {
            val adapter= ChequeBranchAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.spBranch.adapter=adapter
            binding.spBranch.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    orgBranchId= it[position].id.toString()
                    orgBranchName= it[position].name
                    viewModel.setBankName(it[position].name)
                }
            }
        })
        binding.apply {
            btnSubmit.setOnClickListener {
                val desc = etAmount.text.toString().trim()
                val subject = etPayable.text.toString().trim()
                if (subject.isEmpty()) {
                    tlpayableTo.error = getString(R.string.required)
                } else if (orgBranchName.isEmpty()) {
                    toastyInfos("select branch")
                } else if (desc.isEmpty()) {
                    tlEnterAmount.error = getString(R.string.required)
                } else {
                    val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
                    cardBinding = CancelDialogLayoutBinding.inflate(LayoutInflater.from(context))
                    cardBinding.apply {
                        tvHeading.text = "Confirm Complaint"
                        tvName.text = "You are about to submit a complaint"
                        tvFromValur.makeGone()
                        tvFrom.makeGone()

                    }

                    cardBinding.btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    cardBinding.btnSubmit.setOnClickListener {
                        dialog.dismiss()
                        tlEnterAmount.error = ""
                        tlpayableTo.error = ""
                        val complaintDTO = ComplaintDTO()
                        complaintDTO.branchId = orgBranchId
                        complaintDTO.description = desc
                        complaintDTO.subject = subject
                        binding.progressr.visibility = View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility = View.GONE
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        binding.btnSubmit.isEnabled = false
                        viewModel.createComplaint(complaintDTO)
                    }

                    dialog.setContentView(cardBinding.root)
                    dialog.show()
                    dialog.setCancelable(false)

                }
            }
            viewModel.status.observe(viewLifecycleOwner,  {
                if (null != it) {
                    binding.progressr.visibility=View.GONE
                    viewModel.stopObserving()
                    when (it) {

                        1 -> {
                            viewModel.stopObserving()
                            binding.progressr.visibility=View.GONE
                            val builder = AlertDialog.Builder(requireContext())
                                .setTitle("successful!")
                                .setMessage("Your complaint request has been received")
                                .setPositiveButton("OK") { _, which ->
                                    findNavController().navigateUp()
                                }
                                .setIcon(R.drawable.ic_check_sucess)
                                .setCancelable(false)
                            builder .show()
                            viewModel.stopObserving()
                        }
                        0 -> {
                            binding.progressr.visibility=View.GONE
                            onInfoDialog(requireContext(),viewModel.statusCMessage.value)
                            viewModel.stopObserving()
                        }
                        else -> {
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
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = "Complaint"
    }
}