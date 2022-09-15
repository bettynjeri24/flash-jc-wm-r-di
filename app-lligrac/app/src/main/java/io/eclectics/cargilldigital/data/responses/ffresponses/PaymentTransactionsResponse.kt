package io.eclectics.cargilldigital.data.responses.ffresponses


import com.google.gson.annotations.SerializedName

data class PaymentTransactionsResponse(
    @SerializedName("status")
    var status: Int?, // 1
    @SerializedName("message")
    var message: String? // Transaction Failed.
)