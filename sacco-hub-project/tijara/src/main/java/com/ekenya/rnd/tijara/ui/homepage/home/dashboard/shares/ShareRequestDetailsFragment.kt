package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.CancelDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentShareRequestDetailsBinding
import com.ekenya.rnd.tijara.requestDTO.CancelTransferDTO
import com.ekenya.rnd.tijara.utils.onInfoDialog
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.shares_received_item_list.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*

class ShareRequestDetailsFragment : Fragment() {
    private lateinit var binding:FragmentShareRequestDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentShareRequestDetailsBinding.inflate(layoutInflater)
        binding.apply {
            if (Constants.FROMSHARESENT==1) {
                btnReject.visibility=View.VISIBLE
                tvTransTypeValue.text = Constants.DTRANSTYPE
                textShareTT.text = Constants.DSHARETRANS
                textName.text = Constants.DNAME
                textMNo.text = Constants.DMEMNUMBER
                textM0bileN.text = Constants.DPHONENUMBER
                textStatus.text = Constants.DSTATUS
                if (Constants.DSTATUS == "Accepted" || Constants.DSTATUS == "Approved") {
                    binding.btnReject.visibility = View.GONE
                    binding.textStatus.setTextColor(resources.getColor(R.color.ForestGreen))

                } else if (Constants.DSTATUS == "Rejected") {
                    binding.btnReject.visibility = View.GONE
                    binding.textStatus.setTextColor(resources.getColor(R.color.textColor))
                } else {
                    binding.btnReject.visibility = View.VISIBLE
                    binding.textStatus.setTextColor(resources.getColor(R.color.textColor))

                }
                btnReject.setOnClickListener {
                    Constants.TOREJECT = 0
                    findNavController().navigate(R.id.action_shareRequestDetailsFragment_to_cancelTransferFragment)
                }
            }else{
                btnReject.visibility=View.GONE
                tvTransTypeValue.text = Constants.DSentTRANSTYPE
                textShareTT.text = Constants.DSentSHARETRANS
                textName.text = Constants.DSentNAME
                textMNo.text = Constants.DSentMEMNUMBER
                textM0bileN.text = Constants.DSentPHONENUMBER
                textStatus.text = Constants.DSentSTATUS
                if (Constants.DSentSTATUS == "Approved") {
                    binding.btnReject.visibility = View.GONE
                    binding.textStatus.setTextColor(resources.getColor(R.color.ForestGreen))

                } else if (Constants.DSentSTATUS == "Pending" || Constants.DSentSTATUS == "Cancelled") {
                    binding.btnReject.visibility = View.GONE
                    binding.textStatus.setTextColor(resources.getColor(R.color.textColor))
                } else {
                    binding.btnReject.visibility = View.VISIBLE
                    binding.textStatus.setTextColor(resources.getColor(R.color.textColor))

                }
            }
            }

        setupNavUp()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.view_details)
    }


}