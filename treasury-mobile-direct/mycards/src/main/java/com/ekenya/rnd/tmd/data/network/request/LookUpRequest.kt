package com.ekenya.rnd.tmd.data.network.request

import com.google.gson.annotations.SerializedName

data class LookUpRequest(
    @field:SerializedName("phoneNumberOrEmail")
    val phoneNumberOrEmail: String? = null
)
