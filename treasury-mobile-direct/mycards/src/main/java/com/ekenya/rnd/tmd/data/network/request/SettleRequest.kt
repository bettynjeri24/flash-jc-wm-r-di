package com.ekenya.rnd.tmd.data.network.request

import com.google.gson.annotations.SerializedName

data class SettleRequest(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("bondId")
	val bondId: Int? = null
)
