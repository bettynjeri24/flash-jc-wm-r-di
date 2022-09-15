package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class BillPaymentResponse(
    @SerializedName("data")
    val `data`: PayBillData,
    @SerializedName("message")
    val message: String, // Successful Transaction Processing
    @SerializedName("status")
    val status: Int // 1
)
    data class PayBillData(
        @SerializedName("amountTobeDeducted")
        val amountTobeDeducted: Int, // 50
        @SerializedName("transactionCode")
        val transactionCode: String // F3YNLGZW
    )
