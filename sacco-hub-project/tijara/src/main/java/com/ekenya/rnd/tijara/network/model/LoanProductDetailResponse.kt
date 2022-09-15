package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class LoanProductDetailResponse(
    @SerializedName("data")
    val `data`: LoanProductData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
data class LoanProductData(
    @SerializedName("activeLoan")
    val activeLoan:ActiveLoan,
    @SerializedName("canTopUp")
    val canTopUp: Boolean, // true
    @SerializedName("hasActiveLoan")
    val hasActiveLoan: Boolean, // true
    @SerializedName("history")
    val history: List<LoanHistory>,
    @SerializedName("productDetails")
    val loanDetails: LoanDetails,
    @SerializedName("qualificationAnalysis")
    val qualificationAnalysis:QualificationAnalysis
)
    data class ActiveLoan(
        @SerializedName("amountApplied")
        val amountApplied: String, // 200,000.00
        @SerializedName("amountApproved")
        val amountApproved: String, // 200,000.00
        @SerializedName("amountDisbursed")
        val amountDisbursed: String, // 200,000.00
        @SerializedName("amountRepaid")
        val amountRepaid: String, // 237,010.00
        @SerializedName("applicationDate")
        val applicationDate: String, // 09-05-2019
        @SerializedName("disbursementDate")
        val disbursementDate: String, // 09-05-2019
        @SerializedName("lastAmountRepaid")
        val lastAmountRepaid: String, // 0.00
        @SerializedName("loanBalance")
        val loanBalance: String, // 125,902.47
        @SerializedName("loanId")
        val loanId: Int, // 9
        @SerializedName("interestAmount")
        val interestAmount: String, // KES 60.398
        @SerializedName("name")
        val name: String // Ordinary Loan
    )

    data class LoanHistory(
        @SerializedName("amountApplied")
        val amountApplied: String, // 20000.0000
        @SerializedName("amountApproved")
        val amountApproved: String, // 20000.0000
        @SerializedName("amountDisbursed")
        val amountDisbursed: String, // 20000.0000
        @SerializedName("amountRepaid")
        val amountRepaid: String, // 33082.5220
        @SerializedName("applicationDate")
        val applicationDate: String, // 2019-08-30
        @SerializedName("disbursementDate")
        val disbursementDate: String, // 2019-08-30
        @SerializedName("lastAmountRepaid")
        val lastAmountRepaid: String, // 0.0000
        @SerializedName("loanBalance")
        val loanBalance: String, // 0.0000
        @SerializedName("loanId")
        val loanId: String, // 52
        @SerializedName("name")
        val name: String, // SME Loan
        @SerializedName("productCode")
        val productCode: String, // LN2020
        @SerializedName("product_id")
        val productId: String, // 7
        @SerializedName("status")
        val status: String // 2
    )
data class QualificationAnalysis(
    @SerializedName("availableBalance")
    val availableBalance: String, // KES 9,800,000
    @SerializedName("borrowedAmount")
    val borrowedAmount: String, // KES 200,000
    @SerializedName("date")
    val date: String, // 12-07-2021
    @SerializedName("limit")
    val limit: String // KES 10,000,000
)

    data class LoanDetails(
        @SerializedName("calculationMethod")
        val calculationMethod: String, // Aggregated Method [PMT]
        @SerializedName("interestRate")
        val interestRate: String, // 1.00% Per MONTH(S)
        @SerializedName("maxAmount")
        val maxAmount: String, // KES 25,000
        @SerializedName("maxRepaymentPeriod")
        val maxRepaymentPeriod: String, // 5 MONTH(S)
        @SerializedName("minAmount")
        val minAmount: String, // KES 0
        @SerializedName("penaltyRate")
        val penaltyRate: String // 0.00% Per MONTH(S)
    )

