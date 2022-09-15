package com.ekenya.rnd.common.dialogs.dialog_confirm

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

/*
 *  Dialog
 */

// Make it available to fragments
fun Fragment.showAlertDialog(func: ConfirmDialogCommon.() -> Unit): AlertDialog =
    ConfirmDialogCommon(requireContext()).apply {
        func()
    }.create()
