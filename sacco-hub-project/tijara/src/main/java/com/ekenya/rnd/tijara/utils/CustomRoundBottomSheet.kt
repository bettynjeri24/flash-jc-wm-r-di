package com.ekenya.rnd.tijara.utils

import android.app.Dialog
import android.os.Bundle
import com.ekenya.rnd.tijara.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class CustomRoundBottomSheet : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogimageTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
       val dialog= BottomSheetDialog(requireContext(), theme)
//        dialog.behavior.skipCollapsed = true
//        dialog.behavior.state = STATE_EXPANDED
        return dialog
    }

}