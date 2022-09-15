package com.ekenya.rnd.onboarding.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfirmViewModel : ViewModel() {
    val isComplete = MutableLiveData<Boolean>()

    fun setComplete() {

        isComplete.postValue(true)

    }
}