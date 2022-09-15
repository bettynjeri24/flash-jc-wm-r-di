package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class OTPResponse(
    @SerializedName("data")
    val `data`: OtpData,
    @SerializedName("message")
    val message: String, // Successfully Verified
    @SerializedName("status")
    val status: Int // 1
)
data class OtpData(
    @SerializedName("token")
    val token: String // 5036

)