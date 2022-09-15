package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class GeneralCommitResponse(
    @SerializedName("data")
    val `data`: AirtimeCommitData,
    @SerializedName("message")
    val message: String, // Successful Transaction Processing
    @SerializedName("status")
    val status: Int // 1
)
    data class AirtimeCommitData(
        @SerializedName("amountTobeDeducted")
        val amountTobeDeducted: String, // 300
        @SerializedName("transactionCode")
        val transactionCode: String // YSPHJ4MZ
    )
