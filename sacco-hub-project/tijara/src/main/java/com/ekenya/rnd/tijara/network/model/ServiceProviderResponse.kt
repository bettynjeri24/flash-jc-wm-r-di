package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

class ServiceProviderResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<ServiceProviderItem>
)
    data class ServiceProviderItem(
        @SerializedName("code")
        val code: String, // SAF
        @SerializedName("id")
        val id: Int, // 19
        @SerializedName("logoUrl")
        val logoUrl: String, // https://test-portal.ekenya.co.ke/tijara/uploads/billers/SAF/logo.png
        @SerializedName("name")
        val name: String // MPESA
    )
