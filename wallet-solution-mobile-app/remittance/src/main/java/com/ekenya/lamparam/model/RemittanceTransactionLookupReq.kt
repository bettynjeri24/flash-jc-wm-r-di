package com.ekenya.lamparam.model

data class RemittanceTransactionLookupReq(
val customerNo:String,
val expectedAmount:String,
val moneyTransferProvider:String,
val purpose:String,
val receiveMoneyFromMtoRefNo:String,
val relationship:String)
