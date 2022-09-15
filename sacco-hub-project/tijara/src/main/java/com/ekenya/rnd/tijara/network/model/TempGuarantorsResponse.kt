package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class TempGuarantorsResponse(
    @SerializedName("data")
    val `data`: List<TempGuarantor>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class TempGuarantor(
        @SerializedName("amount")
        val amount: String, // 1.00
        @SerializedName("dateAdded")
        val dateAdded: String, // 2021-11-01 10:28:08
        @SerializedName("memberName")
        val memberName: String, // Shemar atis Ankunding
        @SerializedName("memberNumber")
        val memberNumber: String, // SA001
        @SerializedName("tempGuarantorId")
        val tempGuarantorId: Int // 2
    )
