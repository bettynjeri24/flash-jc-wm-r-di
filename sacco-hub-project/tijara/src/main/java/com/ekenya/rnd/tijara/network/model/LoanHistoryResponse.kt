package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class LoanHistoryResponse(
    @SerializedName("data")
    val `data`: LoanAppData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
data class LoanAppData(
    @SerializedName("history")
    val history: List<LoanDataHistory>
)
data class LoanDataHistory(
    @SerializedName("amountApplied")
    val amountApplied: String, // 2580.0000
    @SerializedName("amountApproved")
    val amountApproved: String, // 2580.0000
    @SerializedName("amountDisbursed")
    val amountDisbursed: String, // 2580.0000
    @SerializedName("amountRepaid")
    val amountRepaid: String, // 2889.6000
    @SerializedName("applicationDate")
    val applicationDate: String, // 2021-08-14
    @SerializedName("disbursementDate")
    val disbursementDate: String, // 2021-08-14
    @SerializedName("lastAmountRepaid")
    val lastAmountRepaid: String, // 0.0000
    @SerializedName("loanBalance")
    val loanBalance: String, // 0.0000
    @SerializedName("loanId")
    val loanId: String, // 201
    @SerializedName("name")
    val name: String, // FIXED LOAN
    @SerializedName("productCode")
    val productCode: String, // FXD
    @SerializedName("product_id")
    val productId: String, // 44
    @SerializedName("status")
    val status: String // 2
)
