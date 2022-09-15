package com.ekenya.lamparam.ui.confirmPin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfirmPinViewModel: ViewModel() {

    /**
     * Navigate to Check navigated Fragment
     */
    private val _navigated = MutableLiveData<Boolean>()
    val navigated: LiveData<Boolean>
        get() = _navigated

    private val _pin = MutableLiveData<String>()
    val pin: LiveData<String>
        get() = _pin

    private val _bioMetric = MutableLiveData<Boolean>()
    val bioMetric: LiveData<Boolean>
        get() = _bioMetric

    init {
        //initialise the values at first so that no navigation can occur
        _navigated.value = false
        _pin.value = ""
        _bioMetric.value = false
    }

    fun bioMetrics() {
        _bioMetric.value = true
    }

    fun checkedBiometrics(){
        _bioMetric.value = false
    }

    fun navigate() {
        _navigated.value = true
    }

    /**
     * The method maps the key stoke to input fields, determines if input is correct and displays appropriate actions
     *
     * @param code :pressed key
     */
    fun mapToCode(code: String) {
        _pin.value += code
    }

    /**
     * The method deletes the key stoke to input fields,
     *
     * @param code :pressed key
     */
    fun deleteKeyStoke() {
        if (_pin.value?.length == 0)
            return

        _pin.value = _pin.value?.dropLast(1)
    }

    /**
     * Show that the navigation has occurred
     */
    fun hasNavigated() {
        _navigated.value = false
        _bioMetric.value = false
        _pin.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        _navigated.value = false
        _bioMetric.value = false
        _pin.value = ""
    }

    fun clearFields() {
        _pin.value = _pin.value?.drop(4)
    }

}