package io.eclectics.cargilldigital.ui.farmerprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentTransferTobankBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.FarmerAccount
import io.eclectics.cargilldigital.ui.spinnermgmt.BeneficiaryChannelSpinner
import io.eclectics.cargill.utils.NetworkUtility
import javax.inject.Inject

@AndroidEntryPoint
class TransferToBank : Fragment() {
    private var _binding: FragmentTransferTobankBinding? = null
    private val binding get() = _binding!!
    lateinit var spnAdapter: BeneficiaryChannelSpinner
    @Inject
    lateinit var navOption:NavOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferTobankBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        setToolbarTitle(resources.getString(R.string.transfer_tobank),resources.getString(R.string.transfer_totelco_subttle))
        return binding.root
       // return inflater.inflate(R.layout.fragment_transfer_tobank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var confirmationObj = ConfirmationObj(resources.getString(R.string.transfer_tobank_subttle),"1 0000 CFA","0 CFA","Evans Gangla",R.id.nav_farmerDashboard,"ftbank")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        binding.btnBuyAirtime.setOnClickListener{
            findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOption)
        }

    var bundleAccount = requireArguments().getString("account")
        var acc:FarmerAccount.BeneficiaryAccObj = NetworkUtility.jsonResponse(bundleAccount!!)
        var beneficiaryList = FarmerAccount.providerList()
        binding.etPhoneNumber.setText(acc.channelNumber)
        var mylist=  arrayListOf(acc).toList()
        spnAdapter = BeneficiaryChannelSpinner(requireActivity(),mylist)
        //spnAdapter = BeneficiaryChannelSpinner(requireActivity(),productFilterSort)
        binding.spinnerProvider.adapter = spnAdapter
        binding.spinnerProvider.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // selectedFarmer = spnAdapter.getItem(position)
                //farmerName = "${selectedFarmer.firstName}${selectedFarmer.lastName}"
            }
        }

    }
    fun setToolbarTitle(title:String,description:String){
        val toolBar =  binding.mainLayoutToolbar
        binding.mainLayoutToolbar.toolbar.visibility = View.VISIBLE
        toolBar.toolbarTitle.text = title
        toolBar.toolbarDescription.text = description
        toolBar.toolbarCancel.setOnClickListener {
            (activity as MainActivity?)!!.navigationMgmt()
        }
        //layoutToolbar.visibility = View.VISIBLE

    }
}