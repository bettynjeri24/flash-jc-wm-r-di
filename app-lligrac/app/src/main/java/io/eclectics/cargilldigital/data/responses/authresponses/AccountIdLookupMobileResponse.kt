package io.eclectics.cargilldigital.data.responses.authresponses


import com.google.gson.annotations.SerializedName

data class AccountIdLookupMobileResponse(

    @field:SerializedName("statusDescription")
    val statusDescription: String? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null
)

data class Data(

    @field:SerializedName("phonenumber")
    val phonenumber: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)