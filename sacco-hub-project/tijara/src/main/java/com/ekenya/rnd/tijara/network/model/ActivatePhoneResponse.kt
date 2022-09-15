package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ActivatePhoneResponse(
    @SerializedName("data")
    val `data`: PhoneData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class PhoneData(
        @SerializedName("phone")
        val phone: String // 0718194920
    )
