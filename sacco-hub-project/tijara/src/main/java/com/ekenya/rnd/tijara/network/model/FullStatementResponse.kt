package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class FullStatementResponse(
    @SerializedName("data")
    val `data`: FullStatItems,
    @SerializedName("message")
    val message: String, // Statement Successfully Generated
    @SerializedName("status")
    val status: Int // 1
)
    data class FullStatItems(
        @SerializedName("charges")
        val charges: Int, // 0
        @SerializedName("exerciseDuty")
        val exerciseDuty: Int, // 0
        @SerializedName("formId")
        val formId: Int, // 1
        @SerializedName("from")
        val from: String, // 01-07-2021
        @SerializedName("recipientEmail")
        val recipientEmail: String, // orikohjacktone@gmail.com
        @SerializedName("to")
        val to: String // 28-09-2021
    )
