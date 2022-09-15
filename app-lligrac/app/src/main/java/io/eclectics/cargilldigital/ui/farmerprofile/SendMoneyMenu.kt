package io.eclectics.cargilldigital.ui.farmerprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentSendMoneymenuBinding


@AndroidEntryPoint
class SendMoneyMenu : Fragment() {
    private var _binding: FragmentSendMoneymenuBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_send_moneymenu, container, false)
        _binding = FragmentSendMoneymenuBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.setToolbarTitle(resources.getString(R.string.transfer_money_toolbar),resources.getString(R.string.transfer_money_tsubttle))
        //(activity as MainActivity?)!!.hideToolbar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardTransfertoTelco.setOnClickListener {
            findNavController().navigate(R.id.nav_TransferToTelco)
        }
        binding.cardTobank.setOnClickListener {
            findNavController().navigate(R.id.nav_TransferToBank)
        }
        binding.cardTransfertoWallet.setOnClickListener {
            findNavController().navigate(R.id.nav_transferToWallet)
        }
    }
}