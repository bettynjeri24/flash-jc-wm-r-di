package com.ekenya.rnd.common.sms

data class SmsRequest(
	val password: String? = null,
	val clientid: String? = null,
	val from: String? = null,
	var to: String? = null,
	var message: String? = null,
	val transactionID: String? = null,
	val username: String? = null
)

