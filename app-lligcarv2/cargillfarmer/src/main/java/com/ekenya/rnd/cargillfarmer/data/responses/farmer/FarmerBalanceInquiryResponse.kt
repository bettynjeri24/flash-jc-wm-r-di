package com.ekenya.rnd.cargillfarmer.data.responses.farmer


import com.google.gson.annotations.SerializedName

data class FarmerBalanceInquiryResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var `data`: Int? // 7740
)