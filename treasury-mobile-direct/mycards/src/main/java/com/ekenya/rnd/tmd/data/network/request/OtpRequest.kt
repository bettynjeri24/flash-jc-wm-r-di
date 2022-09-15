package com.ekenya.rnd.tmd.data.network.request

import com.google.gson.annotations.SerializedName

data class OtpRequest(

    @field:SerializedName("phoneNumberOrEmail")
    val phoneNumberOrEmail: String? = null,

    @field:SerializedName("otpValue")
    val otpValue: String? = null,

    @field:SerializedName("otpType")
    val otpType: String? = null
)
