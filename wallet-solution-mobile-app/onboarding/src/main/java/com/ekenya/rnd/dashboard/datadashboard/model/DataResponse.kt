package com.ekenya.rnd.dashboard.datadashboard.model


data class DataResponse(
    val savings: List<SavingsItem>,
    val response: ResponseObject,
    val transaction_details: TransactionDetails
)