package com.ekenya.lamparam.model

data class FundsTransferReceiveMoneyReq(
val otp:String,
val customerNo:String,
val requestType:String,
val coreAccountNo:String,
val accountNo:String)

