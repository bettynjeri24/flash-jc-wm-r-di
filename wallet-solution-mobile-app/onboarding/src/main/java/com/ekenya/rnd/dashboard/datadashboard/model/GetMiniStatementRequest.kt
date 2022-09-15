package com.ekenya.rnd.dashboard.datadashboard.model

data class GetMiniStatementRequest (
    val service_name :String,
    val amount : Int,
    val fund_source: Int,
    val phone_number: String,
    val is_bank:Boolean,
    val debit_account: String,
    val geolocation: String,
    val user_agent: String,
    val user_agent_version: String)
