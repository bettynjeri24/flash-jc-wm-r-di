package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ShareRequestResponse(
    @SerializedName("data")
    val `data`: ShareRequestData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class ShareRequestData(
        @SerializedName("requestsReceived")
        val requestsReceived: List<RequestsReceived>,
        @SerializedName("requestsSent")
        val requestsSent: List<RequestsSent>,
        @SerializedName("shareBalance")
        val shareBalance: ShareBalance
    )
data class RequestsSent(
    @SerializedName("isSent")
    val isSent: Int, // 1
    @SerializedName("memberName")
    val memberName: String, // Sherry Pauline
    @SerializedName("memberNumber")
    val memberNumber: String, // 121
    @SerializedName("memberPhone")
    val memberPhone: String, // 0723456341
    @SerializedName("numberOfShares")
    val numberOfShares: Int, // 60
    @SerializedName("status")
    val status: String, // Approved
    @SerializedName("transactionCode")
    val transactionCode: String, // QM13JYR2
    @SerializedName("transactionType")
    val transactionType: String // Transfer
)
data class RequestsReceived(
    @SerializedName("isSent")
    val isSent: Int, // 0
    @SerializedName("memberName")
    val memberName: String, // Sherry Pauline
    @SerializedName("memberNumber")
    val memberNumber: String, // 121
    @SerializedName("memberPhone")
    val memberPhone: String, // 0723456341
    @SerializedName("numberOfShares")
    val numberOfShares: Int, // 825
    @SerializedName("status")
    val status: String, // Approved
    @SerializedName("transactionCode")
    val transactionCode: String, // MCL8A6OK
    @SerializedName("transactionType")
    val transactionType: String // Transfer
)

        data class ShareBalance(
            @SerializedName("currency")
            val currency: String, // KES
            @SerializedName("numberOfShares")
            val numberOfShares: Int, // 34803
            @SerializedName("totalValue")
            val totalValue: Int, // 34803
            @SerializedName("valuePerShare")
            val valuePerShare: Int // 1
        )
