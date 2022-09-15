package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class MemberPreviewResponse(
    @SerializedName("data")
    val `data`: MemberInfo,
    @SerializedName("message")
    val message: String, // Preview
    @SerializedName("status")
    val status: Int // 1
)
    data class MemberInfo(
        @SerializedName("charges")
        val charges: Int, // 0
        @SerializedName("exerciseDuty")
        val exerciseDuty: Int, // 0
        @SerializedName("formId")
        val formId: Int, // 200
        @SerializedName("toAccountNumber")
        val toAccountNumber: String, // 0011/SAL001
        @SerializedName("toClientName")
        val toClientName: String // Shemar
    )
