package com.ekenya.rnd.tmd.data.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("metadata")
    val metadata: Any? = null,

    @field:SerializedName("data")
    val data: LoginData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: Any? = null
)

data class LoginData(

    @field:SerializedName("access_token")
    val accessToken: String? = null,

    @field:SerializedName("refresh_token")
    val refreshToken: String? = null,

    @field:SerializedName("firstTimeLogin")
    val firstTimeLogin: Boolean? = null,

    @field:SerializedName("scope")
    val scope: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("token_type")
    val tokenType: String? = null,

    @field:SerializedName("expires_in")
    val expiresIn: Int? = null,

    @field:SerializedName("jti")
    val jti: String? = null
)
