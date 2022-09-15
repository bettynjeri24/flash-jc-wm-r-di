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
import io.eclectics.cargilldigital.databinding.FragmentTransferTowalletBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.FarmerAccount
import io.eclectics.cargilldigital.ui.spinnermgmt.BeneficiaryChannelSpinner
import io.eclectics.cargill.utils.NetworkUtility
import javax.inject.Inject

@AndroidEntryPoint
class TransferToWallet : Fragment() {
    private var _binding: FragmentTransferTowalletBinding? = null
    private val binding get() = _binding!!
    lateinit var spnAdapter: BeneficiaryChannelSpinner
    @Inject
    lateinit var navOptions:NavOptions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferTowalletBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        setToolbarTitle(resources.getString(R.string.transfer_towallet),resources.getString(R.string.transfer_towallet_subttle))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var confirmationObj = ConfirmationObj(resources.getString(R.string.transfer_towallet),"10000 CFA","0 CFA","Evans Gangla",R.id.nav_farmerDashboard,"ftwallet")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        var bundleAccount = requireArguments().getString("account")
        var acc:FarmerAccount.BeneficiaryAccObj = NetworkUtility.jsonResponse(bundleAccount!!)

        binding.btnBuyAirtime.setOnClickListener{
            findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
        }
        var beneficiaryList = FarmerAccount.getWalletAccount()
       // spnAdapter = BeneficiarySpinner(requireActivity(),beneficiaryList)
        var mylist=  arrayListOf(acc).toList()

        spnAdapter = BeneficiaryChannelSpinner(requireActivity(),mylist)
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