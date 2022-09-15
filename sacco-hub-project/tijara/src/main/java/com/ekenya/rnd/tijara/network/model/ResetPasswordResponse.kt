package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
    @SerializedName("data")
    val `data`: ResetPassData,
    @SerializedName("message")
    val message: String, // Activation code successfully created. The password will expiry in 2 minutes time from 2021-08-12 10:39:17
    @SerializedName("status")
    val status: Int // 1
)
data class ResetPassData(
    @SerializedName("expiry_date")
    val expiryDate: String // 2021-08-12 10:41:17
)
