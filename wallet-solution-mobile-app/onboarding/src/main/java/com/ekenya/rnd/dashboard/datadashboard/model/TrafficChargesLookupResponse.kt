package com.ekenya.rnd.dashboard.datadashboard.model

import com.google.gson.annotations.SerializedName

data class TrafficChargesLookupResponse(

    @SerializedName("status"  ) var status: Int?    = null,
    @SerializedName("message" ) var message: String? = null,
    @SerializedName("data"    ) var data: TrafficChargesResponseData = TrafficChargesResponseData()

)

