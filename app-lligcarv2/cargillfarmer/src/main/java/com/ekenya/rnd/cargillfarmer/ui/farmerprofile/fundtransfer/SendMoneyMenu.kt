package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.fundtransfer

import android.os.Bundle
import android.view.View
import com.ekenya.rnd.cargillfarmer.databinding.FragmentSendMoneymenuBinding
import com.ekenya.rnd.common.utils.base. BaseCommonCargillFragment

class SendMoneyMenu :
     BaseCommonCargillFragment<FragmentSendMoneymenuBinding>(FragmentSendMoneymenuBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardTransfertoTelco.setOnClickListener {
            //findNavController().navigate(R.id.nav_TransferToTelco)
        }
        binding.cardTobank.setOnClickListener {
            //findNavController().navigate(R.id.nav_TransferToBank)
        }
        binding.cardTransfertoWallet.setOnClickListener {
            // findNavController().navigate(R.id.nav_transferToWallet)
        }
    }
}