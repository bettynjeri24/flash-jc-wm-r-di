package com.ekenya.rnd.dashboard.datadashboard.model

data class SendMoneyToAnotherWalletRequest(

    val service_name: String,
    val amount: Int,
    val fund_source: Int,
    val phone_number: String,
    val biller_ref: String,
    val is_bank: Boolean,
    val stage: String,
    val credit_account: String,
    val debit_account: String,
    val geolocation: String,
    val user_agent: String,
    val user_agent_version: String

)
