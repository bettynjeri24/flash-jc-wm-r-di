package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("data")
    val `data`: RegistrationData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
) {
    data class RegistrationData(
        @SerializedName("form_id")
        val formId: Int // 206
    )
}