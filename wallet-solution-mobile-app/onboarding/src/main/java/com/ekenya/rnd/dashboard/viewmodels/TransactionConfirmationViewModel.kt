package com.ekenya.rnd.dashboard.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekenya.rnd.common.data.model.MainDataObject

class TransactionConfirmationViewModel : ViewModel() {
    val paymentOption = MutableLiveData<String>()

    val cardPaymentsPayload = MutableLiveData<String>()
    val mobileMoneyPaymentPayload = MutableLiveData<MainDataObject>()

    fun setPaymentOption(text: String = "Mobile Money")
     {
        paymentOption.value = text
    }

    fun setCardPaymentPayload(text: String) {
        cardPaymentsPayload.value = text
    }
    fun setMobileMoneyPaymentPayload(mobileMoneyData: MainDataObject) {
        mobileMoneyPaymentPayload.value = mobileMoneyData
    }
}