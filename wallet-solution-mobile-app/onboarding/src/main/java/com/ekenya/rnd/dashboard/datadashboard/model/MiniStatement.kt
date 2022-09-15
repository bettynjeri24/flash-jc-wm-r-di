package com.ekenya.rnd.dashboard.datadashboard.model

data class MiniStatement (
    val mini_statement: List<StatementItem>,
    val statement: String,
    val response_code: String,
    val response_message: String
)