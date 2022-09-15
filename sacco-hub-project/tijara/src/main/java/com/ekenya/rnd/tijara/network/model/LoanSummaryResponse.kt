package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class LoanSummaryResponse(
    @SerializedName("status")
    val status: Int, // 200
    @SerializedName("data")
    val `data`: LoanStatementData,
    @SerializedName("message")
    val message: String // Account Summary
)
    data class LoanStatementData(
        @SerializedName("memberInformation")
        val memberInformation: MemberInformation,
        @SerializedName("transactions")
        val transactions: List<LoanStatement>
    )
        data class MemberDetails(
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

        data class LoanStatement(
            @SerializedName("balance")
            val balance: String, // 20.00
            @SerializedName("credit")
            val credit: String, // 0.00
            @SerializedName("debit")
            val debit: String, // 20.00
            @SerializedName("notes")
            val notes: String, // - Amount Payable to the client
            @SerializedName("refNo")
            val refNo: Any, // null
            @SerializedName("transactionDate")
            val transactionDate: String // 2021-11-05
        )

