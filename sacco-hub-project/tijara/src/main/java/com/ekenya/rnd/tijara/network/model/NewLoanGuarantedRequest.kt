package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class NewLoanGuarantedRequest(
    @SerializedName("data")
    val `data`: List<NewLoanGuaranteData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
@Parcelize
    data class NewLoanGuaranteData(
        @SerializedName("amountRequested")
        val amountRequested: String, // 1000.0000
        @SerializedName("encodedStatus")
        val encodedStatus: Int, // 1
        @SerializedName("loanName")
        val loanName: String, // REDUCING BALANCE LOAN
        @SerializedName("memberNumber")
        val memberNumber: String, // SA001
        @SerializedName("name")
        val name: String, // Shemar atis Ankunding
        @SerializedName("percentageRequested")
        val percentageRequested: Double, // 5.0000
        @SerializedName("requestId")
        val requestId: Int, // 236
        @SerializedName("status")
        val status: String // PENDING
    ):Parcelable
