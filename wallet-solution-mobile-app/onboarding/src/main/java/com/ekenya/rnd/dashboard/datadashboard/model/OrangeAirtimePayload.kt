package com.ekenya.rnd.dashboard.datadashboard.model

data class OrangeAirtimePayload(
    val service_name: String = "AirtimeTopup",
    val amount: Int,
    val fund_source: Int,
    val phone_number: String,
    val account_number: String,
    val host_code: String,
    val biller_ref: String,
    val stage: String = "",
    val biller_ref_2: String,
    val debit_account: String,
    val names: String,
    val geolocation: String,
    val user_agent: String,
    val user_agent_version: String,
    val service_code:String ="SAFARICOM",
)
