package com.ekenya.rnd.common.utils.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.ekenya.rnd.common.dialogs.base.adapter_detail.model.DialogDetailCommon
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCommon
import com.ekenya.rnd.common.dialogs.dialog_progress.ProgressDialogCommon
import com.ekenya.rnd.common.utils.custom.simsDetails
import com.ekenya.rnd.common.utils.custom.snackBarCustom
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.util.ArrayList
import java.util.HashMap

abstract class BaseCommonBuyerCargillDIFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : DaggerFragment() {

    private var _binding: VB? = null
    val binding get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) throw IllegalArgumentException("Binding Not Found")
        return binding.root
    }

    /**
     *CUSTOM DIALOGS
     */
    fun showConfirmationDialog(
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
    }

    /**
     *TO ENABLE OFFLINE USSD
     */
    fun checkPermissionsForCallPhone() {
        if (
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.e("Request Permissions")
            requestMultiplePermissions.launch(
                arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS)
            )
        } else {
            Timber.e("Permission Already Granted")
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Timber.e("${it.key} = ${it.value}")
            }
            if (permissions[Manifest.permission.CALL_PHONE] == true && permissions[Manifest.permission.READ_CONTACTS] == true) {
                simsDetails()
            } else {
                Timber.e("Permission not granted")
                snackBarCustom(
                    "Please to grant this permission to access best offline capabilities"
                ) { checkPermissionsForCallPhone() }
            }
        }
}
