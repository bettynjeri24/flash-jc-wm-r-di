package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ResetPinResponse(
    @SerializedName("data")
    val `data`: ResetPinData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)
    data class ResetPinData(
        @SerializedName("username")
        val username: String
    )
