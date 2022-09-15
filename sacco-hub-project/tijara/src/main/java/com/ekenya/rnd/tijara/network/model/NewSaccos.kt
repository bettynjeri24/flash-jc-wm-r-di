package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class NewSaccos(
    @SerializedName("data")
    val `data`: List<NewSaccoData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class NewSaccoData(
        @SerializedName("id")
        val id: Int, // 20
        @SerializedName("isSacco")
        val isSacco: Boolean, // false
        @SerializedName("name")
        val name: String, // Muungano
        @SerializedName("website")
        val website: String? // null
    )
