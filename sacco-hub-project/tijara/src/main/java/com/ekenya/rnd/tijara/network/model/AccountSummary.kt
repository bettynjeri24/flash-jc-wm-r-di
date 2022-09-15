package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class AccountSummary(
    @SerializedName("status")
    val status: Int, // 200
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String // Account Summary
)
    data class Data(
        @SerializedName("memberInformation")
        val memberInformation: MemberInformation,
        @SerializedName("transactions")
        val transactions: List<TransactionDetails>
    )
        data class MemberInformation(
            @SerializedName("from")
            val from: Any, // null
            @SerializedName("fullName")
            val fullName: String, // Moses Ochola
            @SerializedName("memberNo")
            val memberNo: String, // 110
            @SerializedName("nationalId")
            val nationalId: String, // 23222333
            @SerializedName("phone")
            val phone: String, // 0718194920
            @SerializedName("to")
            val to: Any // null
        )

        data class TransactionDetails(
            @SerializedName("balance")
            val balance: String, // 59,000.00
            @SerializedName("credit")
            val credit: String, // 59,000.00
            @SerializedName("debit")
            val debit: String, // 0.00
            @SerializedName("notes")
            val notes: String, // null
            @SerializedName("refNo")
            val refNo: String, // null
            @SerializedName("refCode")
            val refCode: String, // null
            @SerializedName("transactionDate")
            val transactionDate: String, // 2021-07-01
             @SerializedName("id")
            val id: Int, // 2021-07-01
        )
