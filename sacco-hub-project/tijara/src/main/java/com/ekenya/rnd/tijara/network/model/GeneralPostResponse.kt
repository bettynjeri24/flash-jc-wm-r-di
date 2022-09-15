package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName
data class GeneralPostResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)