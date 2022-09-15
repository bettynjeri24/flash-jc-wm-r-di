package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class LoanCalculatorResponse(
    @SerializedName("data")
    val `data`: LoanCalculatorData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
data class LoanCalculatorData(
    @SerializedName("interestAmount")
    val interestAmount: String, // 260.00
    @SerializedName("principalAmount")
    val principalAmount: String, // 26,000.00
    @SerializedName("totalLoanAmount")
    val totalLoanAmount: String // 26,260.00
)
