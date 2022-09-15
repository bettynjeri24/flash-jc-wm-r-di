package com.ekenya.rnd.tmd.data.network.request

import com.google.gson.annotations.SerializedName

data class TransactionsRequest(

    @field:SerializedName("size")
    val size: Int? = null,

    @field:SerializedName("page")
    val page: Int? = null
)
