package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String
)