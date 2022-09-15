package com.ekenya.rnd.dashboard.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekenya.rnd.common.data.model.BuyAirtimeReq

class ConfirmSendingMoneyViewModel : ViewModel() {
    val paymentOption = MutableLiveData<String>()
    val buyAirtimeReq = MutableLiveData<BuyAirtimeReq>()
    val dstvAccountNumber = MutableLiveData<String>()
    val savings_purpose = MutableLiveData<String>()
    val receivingAccountNumber = MutableLiveData<String>()
    val receiversPhoneNumber = MutableLiveData<String>()
    val receiversName = MutableLiveData<String>()

    val botswanaPMetreNumber = MutableLiveData<String>()
    val accountName = MutableLiveData<String>()
    val agentName = MutableLiveData<String>()
    val sendingFromValue = MutableLiveData<String>()
    val agentNumber = MutableLiveData<String>()
    val amount = MutableLiveData<Int>()
    val requestingFragment = MutableLiveData<String>()
    val customerMetreNumberName = MutableLiveData<String>()


    fun setRequestingFragment(text: String) {
        requestingFragment.value = text
    }


    fun setCustomerMetreNumberName(text: String) {
        customerMetreNumberName.value = text
    }
    fun setSavingsPurpose(text: String) {
        savings_purpose.value = text
    }

    fun setReceivingAccountNumber(text: String) {
        receivingAccountNumber.value = text
    }

    fun setReceiversName(text: String) {
        receiversName.value = text
    }


    fun sendingFromPaymentOption(text: String) {
        sendingFromValue.value = text
    }

    fun setPaymentOption(text: String = "Tollo Wallet") {
        paymentOption.value = text
    }

    fun setDstvAccountNumber(text: String) {
        dstvAccountNumber.value = text
    }

    fun setBuyAirtimeReq(req:BuyAirtimeReq)
    {
        buyAirtimeReq.value = req
    }

    fun setBotswanaPMetreNumber(text: String) {
        botswanaPMetreNumber.value = text
    }

    fun setDstvAccountName(value: String) {
        accountName.value = value
    }

    fun setAmount(text: Int) {
        amount.value = text
    }

    fun setReceiversPhoneNumber(text: String) {
        receiversPhoneNumber.value = text
    }

    fun setAgentName(text: String) {
        agentName.value = text
    }

    fun setAgentNumber(text: String) {
        agentNumber.value = text
    }
}