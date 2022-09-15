package com.ekenya.rnd.cargillbuyer.data.responses


import com.google.gson.annotations.SerializedName

data class BuyerBalanceInquiryResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var `data`: Int? // 7740
)