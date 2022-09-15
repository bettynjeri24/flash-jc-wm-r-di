package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ActiveLoanResponse(
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int, // 1
    @SerializedName("data")
    val `data`: List<ActivesLoan>
)
    data class ActivesLoan(
        @SerializedName("amountApplied")
        val amountApplied: String, // KES 30.00
        @SerializedName("amountDisbursed")
        val amountDisbursed: String, // KES 30.00
        @SerializedName("amountRepaid")
        val amountRepaid: String, // KES 0.00
        @SerializedName("interestAmount")
        val interestAmount: String, // KES 3.60
        @SerializedName("lastAmountRepaid")
        val lastAmountRepaid: String, // KES 0.00
        @SerializedName("loanBalance")
        val loanBalance: String, // KES 33.60
        @SerializedName("mustBeGuarateed")
        val mustBeGuarateed: Boolean,
        @SerializedName("loanId")
        val loanId: Int, // 208
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("name")
        val name: String, // FIXED LOAN
        @SerializedName("penaltyAmount")
        val penaltyAmount: String, // KES 0.00
        @SerializedName("product")
        val product: String // FIXED LOAN
    )
