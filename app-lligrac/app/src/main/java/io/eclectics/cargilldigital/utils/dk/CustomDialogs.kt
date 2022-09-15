package io.eclectics.cargilldigital.utils.dk

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.DialogCustomWarningLayoutBinding


fun Activity.showCargillCustomWarningDialog(
    title: String = getString(R.string.error_message),
    description: String = getString(R.string.home),
    btnConfirmText: String = getString(R.string.retry),
    positiveButtonFunction: (() -> Unit)? = null
) {
    val dialog = Dialog(this, R.style.Theme_Dialog)
    val layoutInflater = LayoutInflater.from(this)
    val binding = DialogCustomWarningLayoutBinding.inflate(layoutInflater)

    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    dialog.setContentView(binding.root)

    val dialogTitle = binding.tvWarningDialogTitle
    val dialogDescription = binding.tvWarningDialogDescription
    val dialogPositiveButton = binding.btnConfirm
    dialogPositiveButton.text = btnConfirmText
    dialogTitle.text = title
    dialogDescription.text = description
    dialogPositiveButton.setOnClickListener {
        positiveButtonFunction?.invoke()
        dialog.dismiss()
    }

    dialog.show()
}