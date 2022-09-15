package com.ekenya.rnd.tmd.data.network.response

import com.google.gson.annotations.SerializedName

data class BidRequestResponse(

    @field:SerializedName("data")
    val data: Any? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)
