package com.ekenya.rnd.dashboard.datadashboard.model

import com.google.gson.annotations.SerializedName

data class RegisterUserResp(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("message")
    val message: String = ""
)
