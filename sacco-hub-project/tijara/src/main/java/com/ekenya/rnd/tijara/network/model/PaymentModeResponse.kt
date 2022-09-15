package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class PaymentModeResponse(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("data")
    val `data`: List<PaymentMode>
)
    data class PaymentMode(
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("name")
        val name: String // CASH,Bank,
    )
