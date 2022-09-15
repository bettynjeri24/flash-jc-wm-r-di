package io.eclectics.cargilldigital.data.repository



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.model.PaymentOptionsModel

object PaymentOptionsRepository {
    
    private val paymentOptions: List<PaymentOptionsModel> = listOf(
        PaymentOptionsModel(R.drawable.logo,"Cocoa Wallet"),
        PaymentOptionsModel(R.drawable.mtn_momo,"MTN Money"),
        PaymentOptionsModel(R.drawable.orange_money,"Orange Money"),
        PaymentOptionsModel(R.drawable.logo,"Bank Account")
    )

    private val _paymentProviders = MutableLiveData<List<PaymentOptionsModel>>()
    val paymentProviders: LiveData<List<PaymentOptionsModel>>
        get() = _paymentProviders

    init {
        _paymentProviders.value = paymentOptions
    }
}