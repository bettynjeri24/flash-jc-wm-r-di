package com.ekenya.rnd.cargillcoop.ui.evalue

import android.os.Bundle
import android.view.View
import com.ekenya.rnd.cargillcoop.R
import com.ekenya.rnd.cargillcoop.databinding.FragmentEvalueRequestListBinding
import com.ekenya.rnd.common.utils.base.BaseCommonCargillCoopFragment
import com.ekenya.rnd.common.utils.custom.setToolbarTitle

class EvalueRequestListFragment : BaseCommonCargillCoopFragment<FragmentEvalueRequestListBinding>(
    FragmentEvalueRequestListBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.evalue_booking),
            resources.getString(com.ekenya.rnd.common.R.string.evalue_list),
            binding.mainLayoutToolbar,
            requireActivity()
        )
    }
}
