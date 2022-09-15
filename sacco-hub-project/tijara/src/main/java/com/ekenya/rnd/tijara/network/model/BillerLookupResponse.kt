package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class BillerLookupResponse(
    @SerializedName("data")
    val `data`: BillerLookupData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class BillerLookupData(
        @SerializedName("AccountID")
        val accountID: String, // 11111
        @SerializedName("accountNumber")
        val accountNumber: String, // 11111
        @SerializedName("AmountDue")
        val amountDue: String, // 100
        @SerializedName("billerCode")
        val billerCode: String, // KPLC_POST_PAID
        @SerializedName("DueAmount")
        val dueAmount: String, // 100
        @SerializedName("Name")
        val name: String // JANE OTIENO
    )
