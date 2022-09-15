package com.ekenya.rnd.common.data.model


data class BuyAirtimeReq(
    var service_name:String,
    var amount:Int,
    var service_code:String,
    var fund_source:Int,
    var phone_number:String,
    var biller_ref:String,
    var biller_ref_2:String,
    var is_bank:Boolean,
    var stage:String,
    var debit_account:String,
    var geolocation:String,
    var user_agent:String,
    var user_agent_version:String
)
