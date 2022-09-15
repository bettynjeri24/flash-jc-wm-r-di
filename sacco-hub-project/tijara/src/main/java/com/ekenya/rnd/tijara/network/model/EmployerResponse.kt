package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class EmployerResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<EmployerList>
)
    data class EmployerList(
        @SerializedName("code")
        val code: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("org_id")
        val orgId: Int,
    )
