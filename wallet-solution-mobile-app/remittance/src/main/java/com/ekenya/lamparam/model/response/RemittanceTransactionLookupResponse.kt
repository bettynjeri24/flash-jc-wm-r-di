package com.ekenya.lamparam.model.response

data class RemittanceTransactionLookupResponse(
    val status:Int,
    val message:String,
    val otp:String,
)
