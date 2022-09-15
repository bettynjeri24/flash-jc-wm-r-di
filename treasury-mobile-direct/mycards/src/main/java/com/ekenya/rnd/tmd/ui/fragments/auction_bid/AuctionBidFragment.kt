package com.ekenya.rnd.tmd.ui.fragments.auction_bid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentAuctionBidBinding
import com.ekenya.rnd.tmd.utils.toast

class AuctionBidFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentAuctionBidBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentAuctionBidBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hh = arguments?.getString("title")
        binding.textView87.text = arguments?.getString("title")
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            buttonBidTreasuryBill.setOnClickListener {
                findNavController().navigate(R.id.action_auctionBidFragment_to_securityOffersFragment)
            }
            buttonBidTreasuryBond.setOnClickListener {
                findNavController().navigate(R.id.action_auctionBidFragment_to_securityOffersFragment)
            }
        }
    }
}
