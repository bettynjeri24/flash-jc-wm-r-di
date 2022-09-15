package com.ekenya.rnd.authcargill.data.responses


import com.google.gson.annotations.SerializedName

data class AccountMobileLookUpResponse(
    @SerializedName("statusDescription")
    var statusDescription: String? = "", // Invalid Credentials. Try Again
    @SerializedName("statusCode")
    var statusCode: Int? = null, // 1
    @SerializedName("data")
    var accountLookUpData: AccountMobileLookUpData? = null
)

data class AccountMobileLookUpData(
    @SerializedName("message")
    var message: String? = "", // Account Was Deactivated
    @SerializedName("phonenumber")
    var phonenumber: String? = "" // 2250701686379
)
