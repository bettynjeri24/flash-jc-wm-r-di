package com.ekenya.rnd.cargillfarmer.data.responses


import com.google.gson.annotations.SerializedName

data class FarmerCashoutResponse(
    @SerializedName("statusDescription") var statusDescription: String? = null,
    @SerializedName("statusCode") var statusCode: Int? = null,
    @SerializedName("data")
    var farmerCashOutData: FarmerCashOutData? = FarmerCashOutData()
) {
    data class FarmerCashOutData(
        @SerializedName("message") var message: String? = null // Please wait while we process your transaction. We will notify you once it is processed.
    )
}