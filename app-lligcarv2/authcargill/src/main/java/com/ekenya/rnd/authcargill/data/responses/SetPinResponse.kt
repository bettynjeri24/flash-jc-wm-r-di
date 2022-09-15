package com.ekenya.rnd.authcargill.data.responses


import com.google.gson.annotations.SerializedName

data class SetPinResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var setPinData: SetPinData?
)

data class SetPinData(
    @SerializedName("message")
    var message: String?, // Successfull
    @SerializedName("phonenumber")
    var phonenumber: String? // 2250701686379
)
