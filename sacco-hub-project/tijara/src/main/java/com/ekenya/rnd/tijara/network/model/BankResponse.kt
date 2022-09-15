package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class BankResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<BankList>
)
    data class BankList(
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("org_id")
        val orgId: Int,
        @SerializedName("swiftcode")
        val swiftcode: String
    ){
        override fun toString(): String {
            return name
        }
    }
