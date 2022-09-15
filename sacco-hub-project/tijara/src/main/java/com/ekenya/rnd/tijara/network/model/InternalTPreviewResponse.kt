package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class InternalTPreviewResponse(
    @SerializedName("data")
    val `data`: ITransferData,
    @SerializedName("message")
    val message: String, // Preview
    @SerializedName("status")
    val status: Int // 1
)
    data class ITransferData(
        @SerializedName("charges")
        val charges: Int, // 0
        @SerializedName("exerciseDuty")
        val exerciseDuty: Int, // 0
        @SerializedName("formId")
        val formId: Int, // 198
        @SerializedName("toAccountNumber")
        val toAccountNumber: String, // BS002021/02
        @SerializedName("toClientName")
        val toClientName: String // Moses
    )
