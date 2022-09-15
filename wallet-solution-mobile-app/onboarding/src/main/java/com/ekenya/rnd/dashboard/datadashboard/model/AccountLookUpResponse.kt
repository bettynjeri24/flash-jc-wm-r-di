package com.ekenya.rnd.dashboard.datadashboard.model

data class AccountLookUpResponse(
    val status: Int,
    val message: String,
    val data: String,
    val activeStatus:Int
)
