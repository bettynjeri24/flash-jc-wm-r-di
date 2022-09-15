package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.fundtransfer

import android.os.Bundle
import android.view.View
import com.ekenya.rnd.cargillfarmer.databinding.FragmentTransferTowalletBinding
import com.ekenya.rnd.common.utils.base. BaseCommonCargillFragment


class TransferToWallet :  BaseCommonCargillFragment<FragmentTransferTowalletBinding>(
    FragmentTransferTowalletBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBuyAirtime.setOnClickListener {
            //findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
        }
    }
}