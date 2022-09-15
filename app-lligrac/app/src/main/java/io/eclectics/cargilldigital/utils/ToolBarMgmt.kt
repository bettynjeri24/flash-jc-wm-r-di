package io.eclectics.cargilldigital.utils

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.PayfarmerToolBarBinding
import io.eclectics.cargilldigital.databinding.TransferLayoutTopBinding

object ToolBarMgmt {
    fun setToolbarTitle(
        title: String,
        description: String,
        mainLayoutToolbar: TransferLayoutTopBinding,
        activity: FragmentActivity
    ){
        val toolBar =  mainLayoutToolbar
       mainLayoutToolbar.toolbar.visibility = View.VISIBLE
        toolBar.toolbarTitle.text = title
        toolBar.toolbarDescription.text = description
        toolBar.toolbarCancel.setOnClickListener {
            (activity as MainActivity?)!!.navigationMgmt()
        }
        //layoutToolbar.visibility = View.VISIBLE

    }

    //HANDLE PAYFARMER TOOL BAR
    fun setPayfarmerToolbarTitle(
        title: String,
        description: String,
        mainLayoutToolbar: PayfarmerToolBarBinding,
        activity: Activity
    ){
        val toolBar =  mainLayoutToolbar
        mainLayoutToolbar.toolbar.visibility = View.VISIBLE
        toolBar.toolbarTitle.text = title
        toolBar.toolbarDescription.text = description
        toolBar.toolbarCancel.setOnClickListener {
            (activity as MainActivity?)!!.navigationMgmt()
        }
        toolBar.imgQRCode.setOnClickListener {
            it.findNavController().navigate(R.id.nav_scanQrCode)
        }
        //layoutToolbar.visibility = View.VISIBLE

    }
}