package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ShareAccountResponse(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("data")
    val `data`: List<ShareAccount>
)
    data class ShareAccount(
        @SerializedName("accountId")
        val accountId: Int, // 1
        @SerializedName("accountName")
        val accountName: String // SHARE CAPITAL
    )
