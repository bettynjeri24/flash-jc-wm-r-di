package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ChequeBranchesResponse(
    @SerializedName("data")
    val `data`: List<ChequeData>,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class ChequeData(
        @SerializedName("code")
        val code: String, // 001
        @SerializedName("country_id")
        val countryId: Int, // 114
        @SerializedName("created_at")
        val createdAt: String, // 2019-07-16 12:34:22
        @SerializedName("created_by")
        val createdBy: Int, // 1
        @SerializedName("id")
        val id: Int, // 12
        @SerializedName("is_active")
        val isActive: Int, // 1
        @SerializedName("name")
        val name: String, // Harare
        @SerializedName("office_location")
        val officeLocation: Any, // null
        @SerializedName("org_id")
        val orgId: Int, // 12

    ){
        override fun toString(): String {
            return name
        }
    }
