package com.ekenya.rnd.dashboard.datadashboard.model

data class SavingsAccountData(
    val service_name: String = "SavingsAccounts",
    val phone_number: String,
    val host_code: String = "MM",
    val geolocation: String = "Home",
    val user_agent: String = "android",
    val user_agent_version: String = "5.1"
)
