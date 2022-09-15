package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class BillerCategories(
    @SerializedName("data")
    val `data`: List<BillerCatgData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class BillerCatgData(
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("name")
        val name: String // Electricity

    )
