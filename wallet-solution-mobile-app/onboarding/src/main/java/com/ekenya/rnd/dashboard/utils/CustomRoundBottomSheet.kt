package com.ekenya.rnd.dashboard.utils

import android.app.Dialog
import android.os.Bundle
import com.ekenya.rnd.onboarding.R
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class CustomRoundBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
       val dialog= BottomSheetDialog(requireContext(), theme)
//        dialog.behavior.skipCollapsed = true
//        dialog.behavior.state = STATE_EXPANDED
        return dialog
    }

}