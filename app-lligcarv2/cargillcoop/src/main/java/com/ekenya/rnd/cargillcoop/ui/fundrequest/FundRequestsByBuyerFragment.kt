package com.ekenya.rnd.cargillcoop.ui.fundrequest

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.cargillcoop.R
import com.ekenya.rnd.cargillcoop.databinding.FragmentFundRequestsByBuyerBinding
import com.ekenya.rnd.common.utils.base.BaseCommonCargillCoopFragment
import com.ekenya.rnd.common.utils.custom.setToolbarTitle

class FundRequestsByBuyerFragment :
    BaseCommonCargillCoopFragment<FragmentFundRequestsByBuyerBinding>(
        FragmentFundRequestsByBuyerBinding::inflate
    ) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.fund_request),
            resources.getString(com.ekenya.rnd.common.R.string.request_sttle),
            binding.mainLayoutToolbar,
            requireActivity()
        )

        binding.rcvBuyers.setOnClickListener {
            findNavController().navigate(R.id.action_fundRequestsByBuyerFragment_to_buyerRequestApprovalFragment)
        }
    }
}
