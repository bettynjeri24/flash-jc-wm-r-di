package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class CountyLookUpResponse(
    @SerializedName("data")
    val `data`: CountInfoData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class CountInfoData(
        @SerializedName("Amount")
        val amount: Int, //
         @SerializedName("AccountNames")
        val accountNames: String, // 50
        @SerializedName("BillNumber")
        val billNumber: String, // L880ZD54
        @SerializedName("billerCode")
        val billerCode: String // COUNTY_PAYMENT
    )
