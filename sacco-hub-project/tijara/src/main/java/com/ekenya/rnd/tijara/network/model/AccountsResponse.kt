package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class AccountsResponse(
    @SerializedName("status")
    val status: Int, // 200
     @SerializedName("message")
    val message: String, // 200
    @SerializedName("data")
    val `data`: List<Accounts>
)

    data class Accounts(
        @SerializedName("accountId")
        val accountId: Int, // 354
        @SerializedName("accountName")
        val accountName: String, // PRIME ACCOUNT
        @SerializedName("accountNumber")
        val accountNumber: String, // 1
        @SerializedName("availableBalance")
        val availableBalance: String, // 104,000.00
        @SerializedName("currentBalance")
        val currentBalance: String, // 104,000.00
        @SerializedName("dateOpened")
        val dateOpened: String, // 01-07-2021
        @SerializedName("defaultCurrency")
        val defaultCurrency: String, // KES
        @SerializedName("dividend")
        val dividend: Int, // 0
        @SerializedName("dividendRate")
        val dividendRate: Int, // 0
        @SerializedName("isShare")
        val isShare: Int, // 0
        @SerializedName("lastAmountTransacted")
        val lastAmountTransacted: String, // 40,000.00
        @SerializedName("lastSavingDate")
        val lastSavingDate: String, // 16-07-2021
        @SerializedName("product")
        val product: String, // FOSA
        @SerializedName("productId")
        val productId: Int, // 33
        @SerializedName("shareCapital")
        val shareCapital: Int, // 0
       var showBalance:Boolean=false
    )
