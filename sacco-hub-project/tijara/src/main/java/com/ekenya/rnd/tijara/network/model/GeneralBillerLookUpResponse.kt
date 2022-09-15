package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class GeneralBillerLookUpResponse(
    @SerializedName("data")
    val `data`: BillerLookupInfo,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class BillerLookupInfo(
        @SerializedName("accountName")
        val accountName: String,
        @SerializedName("accountNumber")
        val accountNumber: String, // 37196413209
        @SerializedName("Address")
        val address: String, // Mr. John     Linde34 Tokai, Cape Town.7999.
        @SerializedName("billerCode")
        val billerCode: String, // KPLC_PREPAID
        @SerializedName("amount")
        val amount: String
    )
