package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("data")
    val `data`: SignUpData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
data class SignUpData(
    @SerializedName("form_id")
    val formId: Int // 182
)