package com.ekenya.rnd.dashboard.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SavingsViewModel : ViewModel() {
    val accountType = MutableLiveData<String>()

    fun setAccountType(text: String) {
        accountType.value = text
    }


}