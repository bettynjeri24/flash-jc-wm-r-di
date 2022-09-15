package com.ekenya.rnd.tijara.utils

import android.app.Activity
import android.app.AlertDialog
import com.ekenya.rnd.tijara.R


class SpinKitDialogLoading(val activity: Activity) {
        private lateinit var dialog: AlertDialog
        fun showSpinkitDialog(text1:String,text2:String){
            /**set View*/
            val infalter = activity.layoutInflater
            val dialogView = infalter.inflate(R.layout.spinkit_dialog_loading,null)
            /**set Dialog*/
            val bulider = AlertDialog.Builder(activity)
            bulider.setView(dialogView)
            bulider.setCancelable(false)

            dialog = bulider.create()
            dialog.show()
        }
        fun hideSpinKitDialog(){
            dialog.dismiss()
        }

}