package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class EmploymentTermResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<EmpTermList>
)
    data class EmpTermList(
        @SerializedName("code")
        val code: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("org_id")
        val orgId: Int
    )
