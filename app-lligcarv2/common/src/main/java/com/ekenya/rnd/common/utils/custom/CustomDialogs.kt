package com.ekenya.rnd.common.utils.custom

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ekenya.rnd.common.R
import com.ekenya.rnd.common.databinding.DialogCustomWarningLayoutBinding

fun Activity.showCargillCustomWarningDialog(
    title: String = getString(R.string.error_message),
    description: String = getString(R.string.msg_request_failed),
    btnConfirmText: String = getString(R.string.try_again),
    action: (() -> Unit)? = null
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
        action?.invoke()
        dialog.dismiss()
    }

    dialog.show()
}

fun Activity.showCargillCustomSuccessDialog(
    title: String = "SUCCESS",
    description: String = getString(R.string.msg_request_was_successful),
    btnConfirmText: String = getString(R.string.okay),
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

    binding.lottieAnimationView.setAnimation(R.raw.lottie_successful_tick)

    dialogTitle.text = title
    dialogDescription.text = description
    dialogPositiveButton.setOnClickListener {
        positiveButtonFunction?.invoke()
        dialog.dismiss()
    }
    dialog.show()
}
