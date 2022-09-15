package com.ekenya.rnd.onboarding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TakeSelfieViewModel : ViewModel() {
    private val _isCaptured = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isCaptured: LiveData<Boolean> = _isCaptured

    fun isCaptured(value:Boolean){
        _isCaptured.postValue(value)

    }
}