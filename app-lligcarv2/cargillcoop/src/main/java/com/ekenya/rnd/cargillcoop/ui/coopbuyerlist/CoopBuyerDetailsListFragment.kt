package com.ekenya.rnd.cargillcoop.ui.coopbuyerlist

import android.os.Bundle
import android.view.View
import com.ekenya.rnd.cargillcoop.R
import com.ekenya.rnd.cargillcoop.databinding.FragmentCoopBuyerDetailsListBinding
import com.ekenya.rnd.common.utils.base.BaseCommonCargillCoopFragment
import com.ekenya.rnd.common.utils.custom.setToolbarTitle

class CoopBuyerDetailsListFragment :
    BaseCommonCargillCoopFragment<FragmentCoopBuyerDetailsListBinding>(
        FragmentCoopBuyerDetailsListBinding::inflate
    ) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.fund_request),
            resources.getString(com.ekenya.rnd.common.R.string.request_sttle),
            binding.mainLayoutToolbar,
            requireActivity()
        )
    }
}
