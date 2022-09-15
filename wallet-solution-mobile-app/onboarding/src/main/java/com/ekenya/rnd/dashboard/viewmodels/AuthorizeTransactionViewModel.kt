package com.ekenya.rnd.dashboard.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthorizeTransactionViewModel : ViewModel() {

    val requestingFragment = MutableLiveData<String>()
    val spinnerOption1 = MutableLiveData<String>()
    val spinnerOption2 = MutableLiveData<String>()


    fun setRequestingFragment(value: String)
    {
        requestingFragment.value = value
    }
    fun setSpinnerOption1(value: String)
    {
        spinnerOption1.value = value
    }
    fun setSpinnerOption2(value: String)
    {
        spinnerOption2.value = value
    }

}