package com.ekenya.rnd.cargillfarmer.data.responses


import com.google.gson.annotations.SerializedName

data class VerifyAddAccountResponse(
    @SerializedName("statusDescription")
    var statusDescription: String? = "", // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var verifyAddAccountData: VerifyAddAccountData? = null
)

data class VerifyAddAccountData(
    @SerializedName("message")
    var message: String? = "" // Account Removed Successfully
)
