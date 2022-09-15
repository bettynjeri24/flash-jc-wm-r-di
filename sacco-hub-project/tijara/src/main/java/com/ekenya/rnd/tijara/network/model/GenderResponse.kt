package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class GenderResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<GenderItems>
)
    data class GenderItems(
        @SerializedName("code")
        val code: String,//F
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("name")
        val name: String,//Female
        @SerializedName("org_id")
        val orgId: Int
    ){
        override fun toString(): String {
            return name
        }
    }
