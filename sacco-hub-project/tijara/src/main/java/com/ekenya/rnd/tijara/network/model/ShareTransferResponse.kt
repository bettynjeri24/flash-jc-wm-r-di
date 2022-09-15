package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ShareTransferResponse(
    @SerializedName("data")
    val `data`: ShareTranferData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class ShareTranferData(
        @SerializedName("charges")
        val charges: Int, // 0
        @SerializedName("currency")
        val currency: String, // KES
        @SerializedName("exciseDuty")
        val exciseDuty: Int, // 0
        @SerializedName("fromAccountId")
        val fromAccountId: Int, // 1
        @SerializedName("fromClientId")
        val fromClientId: Int, // 110
        @SerializedName("numberOfShares")
        val numberOfShares: Int, // 10
        @SerializedName("orgId")
        val orgId: Int, // 12
        @SerializedName("toClientId")
        val toClientId: Int, // 142
        @SerializedName("transactionCode")
        val transactionCode: String, // Y3TFACSK
        @SerializedName("transactionDate")
        val transactionDate: String, // 2021-09-21
        @SerializedName("transactionType")
        val transactionType: String, // null
        @SerializedName("valuePerShare")
        val valuePerShare: Int // 1
    )
