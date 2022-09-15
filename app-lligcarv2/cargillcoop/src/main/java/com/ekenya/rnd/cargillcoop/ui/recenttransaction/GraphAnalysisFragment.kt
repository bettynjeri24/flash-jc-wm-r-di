package com.ekenya.rnd.cargillcoop.ui.recenttransaction

import android.os.Bundle
import android.view.View
import com.ekenya.rnd.cargillcoop.R
import com.ekenya.rnd.cargillcoop.databinding.FragmentGraphAnalysisBinding
import com.ekenya.rnd.common.utils.base.BaseCommonCargillCoopFragment
import com.ekenya.rnd.common.utils.custom.setToolbarTitle

class GraphAnalysisFragment : BaseCommonCargillCoopFragment<FragmentGraphAnalysisBinding>(
    FragmentGraphAnalysisBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.evalue_analysis),
            resources.getString(com.ekenya.rnd.common.R.string.evalue_analysis),
            binding.mainLayoutToolbar,
            requireActivity()
        )
    }
}
