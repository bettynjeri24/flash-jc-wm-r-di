package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GuarantorResponse(
    @SerializedName("data")
    val `data`: List<GuarantorData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
@Parcelize
    data class GuarantorData(
        @SerializedName("amountRequested")
        val amountRequested: String, // 10.00
        @SerializedName("guarantorId")
        val guarantorId: Int, // 230
        @SerializedName("loanName")
        val loanName: String, // REDUCING BALANCE LOAN
        @SerializedName("memberNumber")
        val memberNumber: String, // SA001
        @SerializedName("name")
        val name: String, // Shemar atis Ankunding
        @SerializedName("percentageRequested")
        val percentageRequested: Double, // 50.0000
        @SerializedName("status")
        val status: String // PENDING
    ):Parcelable
