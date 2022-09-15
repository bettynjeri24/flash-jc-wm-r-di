package com.ekenya.rnd.dashboard.datadashboard.model

data class SavingsItem(
    val account_number: String,
    val target_amount: Double,
    val current_balance: Double,
    val saving_purpose: String
)
