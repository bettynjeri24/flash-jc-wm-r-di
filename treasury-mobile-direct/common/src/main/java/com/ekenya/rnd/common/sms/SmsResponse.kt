package com.ekenya.rnd.common.sms

import com.google.gson.annotations.SerializedName

data class SmsResponse(

	@field:SerializedName("ResultDetails")
	val resultDetails: ResultDetails? = null,

	@field:SerializedName("RequestId")
	val requestId: Int? = null,

	@field:SerializedName("ResultDesc")
	val resultDesc: String? = null,

	@field:SerializedName("ResultCode")
	val resultCode: Int? = null
)

data class ResultDetails(

	@field:SerializedName("COUNTRY")
	val cOUNTRY: String? = null,

	@field:SerializedName("DATA")
	val dATA: String? = null,

	@field:SerializedName("PREFIX")
	val pREFIX: String? = null,

	@field:SerializedName("CREDIT_BALANCE")
	val cREDITBALANCE: String? = null
)
