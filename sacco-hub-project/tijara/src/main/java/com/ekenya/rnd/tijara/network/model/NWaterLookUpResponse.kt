package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class NWaterLookUpResponse(
    @SerializedName("data")
    val `data`: NairobiWData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class NairobiWData(
        @SerializedName("accountNumber")
        val accountNumber: String, // 1234567
        @SerializedName("Amount")
        val amount: String, // 7500
        @SerializedName("billerCode")
        val billerCode: String, // NRBC_WATER
        @SerializedName("DueDate")
        val dueDate: String, // New
        @SerializedName("Names")
        val names: String, // John Doe
        @SerializedName("ServiceNumber")
        val serviceNumber: String // 01010101
    )
