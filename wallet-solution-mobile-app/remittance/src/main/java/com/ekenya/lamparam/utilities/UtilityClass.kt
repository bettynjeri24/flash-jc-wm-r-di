package com.ekenya.lamparam.utilities

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.lamparam.R
import kotlin.reflect.KFunction0

class UtilityClass {
    fun getNavoptions(): NavOptions {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.slide_out)
            .setPopEnterAnim(R.anim.slide_in)
            .setPopExitAnim(R.anim.slide_out)
            .build()

        return navOptions
    }

    fun confirmTransactionEnd(
        it: View?,
        title: String,
        message: String,
        activity: FragmentActivity,
        customFunction: () -> (Unit)
    ) {

        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.apply {
            setTitle(title)
            //alertDialog.setIcon(R.drawable.ic_send_success)
            setMessage(message)
            setPositiveButton(
                "OK"
            ) { dialog, _ -> //
                dialog.dismiss()
//                it?.findNavController()?.navigate(destination)
//                it?.findNavController()?.popBackStack(destination,false)
                customFunction()
            }
            setCancelable(false)
            show()
        }
    }

    fun confirmTransactionEnd(
        it: View?,
        title: String,
        message: String,
        activity: FragmentActivity
    ) {

        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK")
            { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            show()
        }
    }
}