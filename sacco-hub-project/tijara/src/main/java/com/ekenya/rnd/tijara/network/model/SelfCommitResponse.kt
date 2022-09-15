package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class SelfCommitResponse(
    @SerializedName("data")
    val `data`: SelfTransferData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class SelfTransferData(
        @SerializedName("transactionCode")
        val transactionCode: String // WMAB1IU6
    )
