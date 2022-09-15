package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LoanProductResponse(
    @SerializedName("status")
    val status: Int, // 200
    @SerializedName("data")
    val `data`: List<LoanProduct>
)
    data class LoanProduct(
        @SerializedName("canApply")
        val canApply: Boolean, // true
        @SerializedName("approvalThreshhold")
        val approvalThreshhold: String, // 50.00 %
        @SerializedName("canRepay")
        val canRepay: Boolean, // false
        @SerializedName("showHistory")
        val showHistory: Boolean,// true
        @SerializedName("activeLoan")
        val activeLoan:ActiveLoans,
        @SerializedName("availableBalance")
        val availableBalance: String, // KES 70,000
        @SerializedName("mustBeGuarateed")
        val mustBeGuarateed: Boolean,
        @SerializedName("borrowedAmount")
        val borrowedAmount: String, // null
        @SerializedName("calculationMethod")
        val calculationMethod: String, // Aggregated Method [PMT]
        @SerializedName("canTopUp")
        val canTopUp: Boolean, // true
        @SerializedName("code")
        val code: String, // RDB
        @SerializedName("date")
        val date: String, // 27-10-2021
        @SerializedName("hasActiveLoan")
        val hasActiveLoan: Boolean, // true
        @SerializedName("history")
        val history: List<History>,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("interestRate")
        val interestRate: String, // 3.00% Per MONTH(S)
        @SerializedName("limit")
        val limit: String, // KES 90,000
        @SerializedName("maxAmount")
        val maxAmount: String, // KES 90,000.00
        @SerializedName("maxRepaymentPeriod")
        val maxRepaymentPeriod: String, // 3 MONTH(S)
        @SerializedName("minAmount")
        val minAmount: String, // KES 0.00
        @SerializedName("name")
        val name: String, // REDUCING BALANCE LOAN
        @SerializedName("penaltyRate")
        val penaltyRate: String, // 0.00% Per MONTH(S)
        @SerializedName("productId")
        val productId: Int// 45

    )

@Parcelize
    data class ActiveLoans(
        @SerializedName("amountApplied")
        val amountApplied: String, // KES 20,000.00
        @SerializedName("amountApproved")
        val amountApproved: String, // KES 0.0000
        @SerializedName("amountDisbursed")
        val amountDisbursed: String, // KES 0.00
        @SerializedName("amountRepaid")
        val amountRepaid: String, // KES 0.00
        @SerializedName("applicationDate")
        val applicationDate: String, // 26-10-2021
        @SerializedName("disbursementDate")
        val disbursementDate: String, // null
        @SerializedName("interestAmount")
        val interestAmount: String, // KES 0.00
        @SerializedName("lastAmountRepaid")
        val lastAmountRepaid: String, // KES 0.00
        @SerializedName("loanBalance")
        val loanBalance: String, // KES 0.00
        @SerializedName("loanId")
        val loanId: Int, // 205
        @SerializedName("name")
        val name: String // REDUCING BALANCE LOAN
    ): Parcelable
    data class History(
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