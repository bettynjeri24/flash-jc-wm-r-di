package com.ekenya.rnd.common.dk

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import cn.pedant.SweetAlert.SweetAlertDialog

class SweetAlertDialogUsage(
    private val mActivity: Activity,
    private var alertType: Int,
) {
    private val pDialog by lazy {
        SweetAlertDialog(mActivity, alertType)
    }

    fun showLOADINGSweetAlert(titleText: String = "Loading..") {
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = titleText
        pDialog.setCancelable(false)
        pDialog.show()
    }


    fun showSUCCESSSweetAlert(titleText: String = "Success..") {
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = titleText
        pDialog.setCancelable(false)
        pDialog.setCancelClickListener { dismissSweetAlert() }
        pDialog.show()
    }


    fun showWARNINGSweetAlert(titleText: String = "Error..") {
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = titleText
        pDialog.setCancelable(false)
        pDialog.setCancelClickListener { dismissSweetAlert() }
        pDialog.show()
    }

    fun dismissSweetAlert() {
        //pDialog.cancel()
        pDialog.dismiss()
    }

}