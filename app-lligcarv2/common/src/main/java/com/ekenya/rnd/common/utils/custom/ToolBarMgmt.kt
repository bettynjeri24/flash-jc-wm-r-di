package com.ekenya.rnd.common.utils.custom

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.ekenya.rnd.common.databinding.TransferLayoutTopBinding


fun setToolbarTitle(
    title: String,
    description: String,
    mainLayoutToolbar: TransferLayoutTopBinding,
    activity: FragmentActivity
) {
    val toolBar = mainLayoutToolbar
    mainLayoutToolbar.toolbar.visibility = View.VISIBLE
    toolBar.toolbarTitle.text = title
    toolBar.toolbarDescription.text = description
    toolBar.toolbarCancel.setOnClickListener {
        activity.onBackPressed()
    }
}

