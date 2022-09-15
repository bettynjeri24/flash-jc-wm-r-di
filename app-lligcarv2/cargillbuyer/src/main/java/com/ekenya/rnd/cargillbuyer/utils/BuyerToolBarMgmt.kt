package com.ekenya.rnd.cargillbuyer.utils

import android.app.Activity
import android.view.View
import com.ekenya.rnd.cargillbuyer.databinding.PayfarmerToolBarBuyerBinding



//HANDLE PAYFARMER TOOL BAR
fun setPayfarmerToolbarTitle(
    title: String,
    description: String,
    mainLayoutToolbar: PayfarmerToolBarBuyerBinding,
    activity: Activity
) {
    val toolBar = mainLayoutToolbar
    mainLayoutToolbar.toolbar.visibility = View.VISIBLE
    toolBar.toolbarTitle.text = title
    toolBar.toolbarDescription.text = description
    toolBar.toolbarCancel.setOnClickListener {
        activity.onBackPressed()
    }
    toolBar.imgQRCode.setOnClickListener {
        //it.findNavController().navigate(R.id.nav_scanQrCode)
    }
    //layoutToolbar.visibility = View.VISIBLE

}
