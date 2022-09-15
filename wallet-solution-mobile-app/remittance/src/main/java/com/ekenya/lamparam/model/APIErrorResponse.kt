package com.ekenya.lamparam.model

import com.google.gson.annotations.SerializedName

data class APIErrorResponse(
    @SerializedName("error")
    val error:String = "",
    @SerializedName("error_description")
    val error_description:String = "" )