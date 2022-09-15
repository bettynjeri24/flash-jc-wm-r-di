package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class StandingOrderFrequeny(
    @SerializedName("data")
    val `data`: List<StandingOrderFrequenyData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class StandingOrderFrequenyData(
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("label")
        val label: String // Hourly
    ){
        override fun toString(): String {
            return label
        }
    }
