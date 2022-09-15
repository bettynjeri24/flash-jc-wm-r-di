package com.ekenya.rnd.dashboard.datadashboard.remote

import com.google.gson.annotations.SerializedName

data class APIErrorResponse(
    @SerializedName("error")
    val error:String = "",
    @SerializedName("error_description")
    val error_description:String = "" )