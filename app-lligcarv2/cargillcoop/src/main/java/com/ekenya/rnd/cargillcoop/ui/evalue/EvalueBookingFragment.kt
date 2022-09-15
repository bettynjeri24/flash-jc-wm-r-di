package com.ekenya.rnd.cargillcoop.ui.evalue

import android.os.Bundle
import android.view.View
import com.ekenya.rnd.cargillcoop.R
import com.ekenya.rnd.cargillcoop.databinding.FragmentEvalueBookingBinding
import com.ekenya.rnd.common.utils.base.BaseCommonCargillCoopFragment
import com.ekenya.rnd.common.utils.custom.setToolbarTitle

class EvalueBookingFragment : BaseCommonCargillCoopFragment<FragmentEvalueBookingBinding>(
    FragmentEvalueBookingBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.evalue_booking),
            resources.getString(com.ekenya.rnd.common.R.string.fund_booking),
            binding.mainLayoutToolbar,
            requireActivity()
        )
    }
}
