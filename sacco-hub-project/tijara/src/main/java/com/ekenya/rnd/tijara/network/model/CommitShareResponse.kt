package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class CommitShareResponse(
    @SerializedName("data")
    val `data`: CommitShareData,
    @SerializedName("message")
    val message: String, // Request successful
    @SerializedName("status")
    val status: Int // 1
)
    data class CommitShareData(
        @SerializedName("charges")
        val charges: Int, // 0
        @SerializedName("currency")
        val currency: String, // KES
        @SerializedName("exciseDuty")
        val exciseDuty: Int, // 0
        @SerializedName("memberName")
        val memberName: String, // Sherry Pauline
        @SerializedName("numberOfShares")
        val numberOfShares: Int, // 10
        @SerializedName("valuePerShare")
        val valuePerShare: Int // 1
    )
