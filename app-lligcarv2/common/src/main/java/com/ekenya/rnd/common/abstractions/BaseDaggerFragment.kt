package com.ekenya.rnd.common.abstractions


import android.os.Bundle
import com.ekenya.rnd.common.dialogs.base.adapter_detail.model.DialogDetailCommon
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCommon
import com.ekenya.rnd.common.dialogs.dialog_progress.ProgressDialogCommon
import dagger.android.support.DaggerFragment
import java.util.ArrayList
import java.util.HashMap

abstract class BaseDaggerFragment : DaggerFragment() {
    // HashMap<String, String> hashMap = new HashMap<String, String>();
//    var progressAlertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

  /*  fun showConfirmationDialog(
        title: String?,
        subtitle: String?,
        dialogDetailCommons: List<DialogDetailCommon>,
        confirmDialogCallBacks: ConfirmDialogCallBacks?
    ) {
        val confirmDialog = ConfirmDialogCommon(context!!)
        confirmDialog.setDialogTitle(title!!)
        confirmDialog.setDialogSubtitle(subtitle!!)
        confirmDialog.setUpRecyclerAdapter(dialogDetailCommons)
        confirmDialog.setCallbacks(confirmDialogCallBacks!!)
        confirmDialog.create().show()
    }

    fun showConfirmationDialog(
        title: String?,
        subtitle: String?,
        details: HashMap<String, String>,
        confirmDialogCallBacks: ConfirmDialogCallBacks?
    ) {
        val dialogDetailCommons: MutableList<DialogDetailCommon> = ArrayList()
        for (key in details.keys) {
            dialogDetailCommons.add(DialogDetailCommon(key!!, details[key]!!))
        }
        val confirmDialog = ConfirmDialogCommon(context!!)
        confirmDialog.setDialogTitle(title!!)
        confirmDialog.setDialogSubtitle(subtitle!!)
        confirmDialog.setUpRecyclerAdapter(dialogDetailCommons)
        confirmDialog.setCallbacks(confirmDialogCallBacks!!)
        confirmDialog.create().show()
    }

    private val progressDialog by lazy {
        ProgressDialogCommon(context!!)
    }

    private val progressAlertDialog by lazy {
        progressDialog.create()
    }

    fun showCustomDialog(message: String?) {
        // init dialog if empty
        if (progressAlertDialog == null) {
            // val progressDialog = ProgressDialogCommon(context!!)
            progressDialog.setloadingMessage(message)
            // progressAlertDialog = progressDialog.create()
        }
        if (!progressAlertDialog!!.isShowing) {
            progressAlertDialog!!.show()
        } else {
            progressAlertDialog!!.dismiss()
        }
    }


    fun dismissCustomDialog() {
        progressAlertDialog!!.dismiss()
    }*/
}