package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class MinistatementResponse(
    @SerializedName("data")
    val `data`: List<MiniStatementData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class MiniStatementData(
        @SerializedName("account")
        val account: String, // PRIME ACCOUNT
        @SerializedName("amount")
        val amount: String, // KES 2,580
        @SerializedName("entryType")
        val entryType: String, // DEBIT
        @SerializedName("transactionDate")
        val transactionDate: String, // 12-08-2021
        @SerializedName("transactionType")
        val transactionType: String, //
         @SerializedName("id")
        val id: String // Withdrawal
    )
