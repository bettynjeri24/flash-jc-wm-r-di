package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ShareMemberLookupResponse(
    @SerializedName("data")
    val `data`: ShareMemberData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class ShareMemberData(
        @SerializedName("currency")
        val currency: String, // KES
        @SerializedName("recipientMemberNumber")
        val recipientMemberNumber: String, // 121
        @SerializedName("recipientName")
        val recipientName: String, // Sherry Pauline
        @SerializedName("recipientPhone")
        val recipientPhone: String, // 0723456341
        @SerializedName("valuePerShare")
        val valuePerShare: Int //
    )
