package com.ekenya.rnd.cargillbuyer.data.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

data class FfPendingPaymentsResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var data: List<FfPendingPaymentsData>?
) {

}
@Parcelize
data class FfPendingPaymentsData(
    @SerializedName("date")
    var date: String?, // 2022-05-08T00:00:00
    @SerializedName("farmerIdx")
    var farmerIdx: Int?, // 2326
    @SerializedName("firstName")
    var firstName: String?, // Daniel
    @SerializedName("lastName")
    var lastName: String?, // Kimani
    @SerializedName("fullAmount")
    var fullAmount: String?, // 340000
    @SerializedName("amountPaid")
    var amountPaid: String?, // 26421
    @SerializedName("farmForceRef")
    var farmForceRef: String?, // G5005445
    @SerializedName("paymentDescription")
    var paymentDescription: String?, // Cocoa Purchase
    @SerializedName("farmerPhonenumber")
    var farmerPhonenumber: String? // 2250701686379
) : Parcelable