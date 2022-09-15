package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class BillerResponse(
    @SerializedName("data")
    val `data`: List<BillerData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class BillerData(
        @SerializedName("id")
        val category_id: Int, // 1
        @SerializedName("name")
        val name: String, // KPLC PREPAID
        @SerializedName("code")
        val code: String, // KPLC_PREPAID
        @SerializedName("has_presentment")
        val hasPresentment: Int, // 1
        @SerializedName("logo_url")
        val logoUrl: String, // /var/www/html/tijara/uploads/billers/KPLC_PREPAID/logo.png
    )
