package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class StandingOrdersResponse(
    @SerializedName("data")
    val `data`: List<StandingOrderData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)@Parcelize
    data class StandingOrderData(
        @SerializedName("amount")
        val amount: String, // 100.00
        @SerializedName("currency")
        val currency: String, // KSH
        @SerializedName("enddate")
        val enddate: String, // 23-12-2021
        @SerializedName("frequencyMeasure")
        val frequencyMeasure: String, // Hourly
        @SerializedName("fromAccount")
        val fromAccount: String, // PRIME ACCOUNT
        @SerializedName("fromAccountNo")
        val fromAccountNo: String, // FS002021/01
        @SerializedName("purpose")
        val purpose: String, // Testing
        @SerializedName("refNo")
        val refNo: String, // TGLFRMZX
        @SerializedName("startdate")
        val startdate: String, // 22-12-2021
        @SerializedName("toAccount")
        val toAccount: String, // ALPHA DEPOSIT
        @SerializedName("toAccountNo")
        val toAccountNo: String // BS002021/02
    ):Parcelable
