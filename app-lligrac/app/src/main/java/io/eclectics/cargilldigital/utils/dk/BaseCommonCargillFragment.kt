package io.eclectics.cargilldigital.utils.dk

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import cn.pedant.SweetAlert.SweetAlertDialog
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.utils.sweetAlertDialogWithCancelAndConfirm
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.util.ArrayList
import kotlin.properties.Delegates

abstract class BaseCommonCargillFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {
    var sweetAlertDialog: SweetAlertDialog? = null
    private var _binding: VB? = null
    val binding get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) throw IllegalArgumentException("Binding Not Found")
        sweetAlertDialog =
            SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE).apply {
                progressHelper.barColor = Color.parseColor("#A5DC86")
                titleText = "Loading"
                setCancelable(false)
            }

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SIM_MAP = mutableMapOf()
        SIMCARDNAMES = ArrayList()


        telephonyManager =
            requireActivity().getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
    }

    private var callCode: String? = null
    private var shortCode: String? = null
    private var transIdPassedCode by Delegates.notNull<Int>()
    private var telephonyManager: TelephonyManager? = null
    private var ussdResponseCallback: TelephonyManager.UssdResponseCallback? = null

    fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requireActivity().sweetAlertDialogWithCancelAndConfirm(
                message = getString(R.string.enable_offline_capability),
                actionConfirm = { checkPermissionsForCallPhone() },
                actionCancel = { errorTransactionCanceled() }
            )

        } else {
            Timber.e(getString(R.string.enabled_offline_capability))
        }
    }

    private fun checkPermissionsForCallPhone() {
        if (
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestMultiplePermissions.launch(
                arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS)
            )
        } else {
            Timber.e("Permission Already Granted")
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            permissions.entries.forEach {
                Timber.e("${it.key} = ${it.value}")
            }
            if (permissions[Manifest.permission.CALL_PHONE] == true && permissions[Manifest.permission.READ_CONTACTS] == true) {
                simsDetails()
            } else {
                Timber.e("Permission not granted")
            }

        }


    private fun simsDetails() {
        val subscriptionInfo = SubscriptionManager.from(
            requireActivity()
        ).activeSubscriptionInfoList
        // this requires READ_PHONE_STATE permission

        // loop through all info objects and store info we store 2 details here carrier name and subscription Id fro each active SIM card
        // so we need map and an array to set SIM card names to Spinner
        for (subscriptionInfo in subscriptionInfo) {
            SIM_MAP[subscriptionInfo.carrierName.toString()] = subscriptionInfo.subscriptionId

            SIMCARDNAMES!!.add(subscriptionInfo.carrierName.toString())
            Timber.e("SIMCARDNAMES === \n$SIMCARDNAMES")
        }
    }

    fun dismissCustomDialog() {
        pdialog.dismiss()
    }

    private lateinit var pdialog: SweetAlertDialog
    fun showCustomDialog(string: String = getString(R.string.sending_request_cargill)) {
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        pdialog.titleText = string//"Loading"
        pdialog.setCancelable(false)
    }


}

