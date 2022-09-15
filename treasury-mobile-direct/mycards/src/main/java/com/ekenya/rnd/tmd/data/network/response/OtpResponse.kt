package com.ekenya.rnd.tmd.data.network.response

import com.google.gson.annotations.SerializedName

data class OtpResponse(

    @field:SerializedName("metadata")
    val metadata: Any? = null,

    @field:SerializedName("data")
    val data: Any? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: Any? = null
)
