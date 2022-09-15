package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class PesalinkPCheckResponse(
    @SerializedName("data")
    val `data`: PesaLinkData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class PesaLinkData(
        @SerializedName("banks")
        val banks: List<PesalinkBank>,
        @SerializedName("name")
        val name: String, // JAMES
        @SerializedName("phoneNumber")
        val phoneNumber: String // 254716735875
    )
        data class PesalinkBank(
            @SerializedName("BANKNAME")
            val bANKNAME: String, // NBK
            @SerializedName("SORTCODE")
            val sORTCODE: String // 40412000
        ){
            override fun toString(): String {
                return bANKNAME
            }
        }
