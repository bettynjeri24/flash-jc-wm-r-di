package com.ekenya.rnd.cargillbuyer.data.responses


import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var `data`: GeneralResponseData // null
)

data class GeneralResponseData(
    @SerializedName("message")
    var message: String? // Successfull

)