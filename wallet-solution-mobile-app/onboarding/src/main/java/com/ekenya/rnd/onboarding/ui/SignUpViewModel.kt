package com.ekenya.rnd.onboarding.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel()  : ViewModel() {
    val firstName = MutableLiveData<String>()

    fun setFirstName(firstname:String) {
        firstName.postValue(firstname)

    }




}

