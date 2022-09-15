package com.ekenya.rnd.tmd.data.network.request

import com.google.gson.annotations.SerializedName

data class BidRequestRequest(

    @field:SerializedName("biddingRate")
    val biddingRate: Double? = null,

    @field:SerializedName("providedRate")
    val providedRate: Double? = null,

    @field:SerializedName("biddingAmount")
    val biddingAmount: Int? = null,

    @field:SerializedName("bidId")
    val bidId: Int? = null
)
