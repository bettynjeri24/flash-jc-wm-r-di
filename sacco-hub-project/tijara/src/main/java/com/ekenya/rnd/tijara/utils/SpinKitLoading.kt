package com.ekenya.rnd.tijara.utils

import android.app.Activity
import android.app.AlertDialog
import com.ekenya.rnd.tijara.R



    class SpinKitLoading(val mActivity: Activity) {
        private lateinit var isdialog:AlertDialog
        fun startLoading(){
            /**set View*/
            val infalter = mActivity.layoutInflater
            val dialogView = infalter.inflate(R.layout.spinkit_layout,null)
            /**set Dialog*/
            val bulider = AlertDialog.Builder(mActivity)
            bulider.setView(dialogView)
            bulider.setCancelable(false)
            isdialog = bulider.create()
            isdialog.show()
        }
        fun isDismiss(){
            isdialog.dismiss()
        }

}