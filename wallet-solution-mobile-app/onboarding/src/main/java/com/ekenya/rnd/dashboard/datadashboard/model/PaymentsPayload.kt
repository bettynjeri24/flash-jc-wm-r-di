package com.ekenya.rnd.dashboard.datadashboard.model

data class PaymentsPayload (

    val service_name:String,

    val amount: Int,

    val fund_source:Int,

    val phone_number:String,

    val host_code: String,

    val biller_ref: String ="",

    val transaction_id:String,

    val stage: String = "",

    val biller_ref_2:String = "",

    val debit_account: String,

    val geolocation: String = "Home",

    val user_agent: String = "android",

    val user_agent_version: String = "9.1",

    val meternumber:String="",

    val accountno:String="",

    val names:String,
    val is_bank:Boolean,
    val merchant_code:String="",
    val complete_payment:Int =1,

)
