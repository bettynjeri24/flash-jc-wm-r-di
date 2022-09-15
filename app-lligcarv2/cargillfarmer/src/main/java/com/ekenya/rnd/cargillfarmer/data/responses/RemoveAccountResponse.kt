package com.ekenya.rnd.cargillfarmer.data.responses


import com.google.gson.annotations.SerializedName

data class RemoveAccountResponse(
    @SerializedName("statusDescription")
    var statusDescription: String? = "", // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var data: RemoveAccountData?
)

data class RemoveAccountData(
    @SerializedName("message")
    var message: String? = "" // Account Removed Successfully
)

