package io.eclectics.cargilldigital.ui.auth.pinmgmt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class PinViewModel @Inject constructor(): ViewModel(){

    private var navigated: MutableLiveData<Boolean>? = null

    fun getNavigated(): LiveData<Boolean>? {
        if (navigated == null) {
            navigated = MutableLiveData()
        }
        return navigated
    }

    private var pin: MutableLiveData<String>? = null

    fun getPin(): LiveData<String>? {
        if (pin == null) {
            pin = MutableLiveData()
        }
        return pin
    }

    fun OtpViewModel() {
        navigated = MutableLiveData()
        navigated!!.value = false
        pin = MutableLiveData()
        pin!!.value = ""
    }

    fun navigate() {
        navigated!!.value = true
    }

    /**
     * The method maps the key stoke to input fields, determines if input is correct and displays appropriate actions
     *
     * @param code :pressed key
     */
    fun mapToCode(code: String) {
        pin!!.value = pin!!.value.toString() + code
    }

    /**
     * The method deletes the key stoke to input fields,
     *
     * @param :pressed key
     */
    fun deleteKeyStoke() {
        if (pin!!.value!!.length == 0) {
            return
        }
        pin!!.value = removeLastChars(pin!!.value, 1)
        Log.d("otpviewmodel", "pin values is " + pin!!.value)
    }

    fun removeLastChars(str: String?, chars: Int): String {
        return str?.substring(0, str.length - chars) ?: ""
    }

    /**
     * Show that the navigation has occurred
     */
    fun hasNavigated() {
        navigated!!.value = false
        pin!!.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        navigated!!.value = false
        pin!!.value = ""
    }


}