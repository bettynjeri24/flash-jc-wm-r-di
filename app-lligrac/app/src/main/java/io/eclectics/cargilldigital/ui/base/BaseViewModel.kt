package io.eclectics.cargilldigital.ui.base

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel

import io.eclectics.cargilldigital.utils.dk.USSDSHORTCODE
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates


@HiltViewModel
open class BaseViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    protected val context
        get() = getApplication<Application>()


    var callCode: String? = null
    var shortCode: String? = null
    var transIdPassedCode by Delegates.notNull<Int>()

    private var _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String>
        get() = _responseMessage

    init {
        _responseMessage.value = null
    }


    fun offlineUssdModeBackGround(userInput: String /*= "345671234889654689"*/) {
        shortCode = USSDSHORTCODE //"605*214"//USSDSHORTCODE
        callCode = "$shortCode*991$userInput" // "605*214*1234"
        // transIdPassedCode = userInput.take(2).toInt()
        Timber.e("callCode : $callCode")
        dialUssdToGetPhoneNumberBackGround("*$callCode#", 1)
    }

    private fun dialUssdToGetPhoneNumberBackGround(ussdCode: String, sim: Int) {
        Timber.e("ussdTag ========== tag $ussdCode")
        if (ussdCode.equals("", ignoreCase = true)) {
            _responseMessage.value = "-1"
            return
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            _responseMessage.value = "-1"
            Timber.e("PackageManager NOT PERMISSION_GRANTED")
            return
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager =
                    context.applicationContext.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager

                val telephonyManager = manager.createForSubscriptionId(2)

                //val managerMain = if (sim == 0) manager else telephonyManager
                val managerMain = if (sim == 0) {
                    manager
                    //manager.createForSubscriptionId(1)
                } else {
                    manager.createForSubscriptionId(2)
                }
                managerMain.sendUssdRequest(
                    ussdCode,
                    object : TelephonyManager.UssdResponseCallback() {
                        override fun onReceiveUssdResponse(
                            telephonyManager: TelephonyManager,
                            request: String,
                            response: CharSequence
                        ) {
                            super.onReceiveUssdResponse(telephonyManager, request, response)
                            Timber.e("USSDCODE=$ussdCode REQUEST =$request\n, RESPONSE=$response\n")
                            _responseMessage.value = response.toString().trim()
                        }

                        override fun onReceiveUssdResponseFailed(
                            telephonyManager: TelephonyManager,
                            request: String,
                            failureCode: Int
                        ) {
                            super.onReceiveUssdResponseFailed(
                                telephonyManager,
                                request,
                                failureCode
                            )
                            Timber.e(
                                "onReceiveUssdResponseFailed Method " +
                                        "FAILURECODE CODE IS $failureCode ==   REQUEST IS $request"
                            )
                            _responseMessage.value = failureCode.toString()
                        }
                    },
                    Handler(Looper.getMainLooper())
                )
            } else {
                Timber.e("ELSE IN 111")
                _responseMessage.value = "-1"
            }
        } catch (e: Exception) {
            _responseMessage.value = "-1"
            Timber.e("Exception ${e.message.toString()}")
        }
    }

}


class SharedViewModel @Inject constructor() :
    ViewModel() {
    var unixTimestamp: Long = 0L
}