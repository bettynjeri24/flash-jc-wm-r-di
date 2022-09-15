package com.ekenya.rnd.cargillbuyer.data.responses

import com.google.gson.annotations.SerializedName

data class BuyerPurchaseResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var `data`: BuyerPurchaseData?
)

data class BuyerPurchaseData(
    @SerializedName("message")
    var message: String? // Votre transaction d'achat de cacao a été complétée avec succès
)
