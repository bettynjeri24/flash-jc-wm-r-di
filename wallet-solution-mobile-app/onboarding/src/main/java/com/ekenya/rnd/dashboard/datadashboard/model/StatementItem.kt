package com.ekenya.rnd.dashboard.datadashboard.model

data class StatementItem(
    val amount: Double,
    val dr_cr: String,
    val transaction_date: String,
    val narration: String,
    val receipt_number: Int,
    val transaction_type: String
)