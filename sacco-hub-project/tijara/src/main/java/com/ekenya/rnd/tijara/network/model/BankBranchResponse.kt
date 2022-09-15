package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class BankBranchResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<BankBranchList>
)
    data class BankBranchList(
        @SerializedName("bank_id")
        val bankId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("org_id")
        val orgId: Int
    )
