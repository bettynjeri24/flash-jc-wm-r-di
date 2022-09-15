package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class IDTypeResponse(
    @SerializedName("data")
    val `data`: List<IDType>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)
    data class IDType(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
    {
        override fun toString(): String {
            return name
        }
    }

