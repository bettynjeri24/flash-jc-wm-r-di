package com.ekenya.rnd.dashboard.datadashboard.model

data class SavingsPayload(
    val service_name: String,
    val amount: Int,
    val target_amount: Int,
    val savings_purpose: String,
    val phone_number: String,
    val host_code: String = "MM",
    val transaction_type: String = "SAVINGS",
    val direction: String = "REQUEST",
    val savings_code: String = "FOSA",
    val duration: Int = 4,
    val terms_accepted: Boolean = true,
    val debit_account: String,
    val transaction_code: String,
    val currency: String = "GHS",
    val geolocation: String = "Home",
    val user_agent: String = "android",
    val user_agent_version: String = "5.1"
)
