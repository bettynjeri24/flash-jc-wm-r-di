package com.ekenya.rnd.tmd.data.network.response

import com.google.gson.annotations.SerializedName

data class LookUpResponse(

	@field:SerializedName("metadata")
	val metadata: Any? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Any? = null
)

data class Data(

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("firstTimeLogin")
	val firstTimeLogin: Boolean? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("loginAttempts")
	val loginAttempts: Int? = null,

	@field:SerializedName("blocked")
	val blocked: Boolean? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
