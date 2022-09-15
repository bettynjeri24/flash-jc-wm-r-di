package com.ekenya.rnd.cargillfarmer.data.responses


import com.google.gson.annotations.SerializedName

data class AddBeneficiaryAccountResponse(
    @SerializedName("statusDescription")
    var statusDescription: String? = "", // Successfull
    @SerializedName("statusCode")
    var statusCode: Int? = 2, // 0
    @SerializedName("data")
    var data: AddBeneficiaryAccountData? =null
) {
    data class AddBeneficiaryAccountData(
        @SerializedName("message")
        var message: String? = "", // OTP Sent
        @SerializedName("accountholdersNumber")
        var accountholdersNumber: String? = "", // 2250701686379
        @SerializedName("newNumber")
        var newNumber: String?, // 2250554954258
        @SerializedName("otp")
        var otp: String? = "" // Vous avez reçu une demande OTP de2250701686379. l'OTP est 4315
    )
}