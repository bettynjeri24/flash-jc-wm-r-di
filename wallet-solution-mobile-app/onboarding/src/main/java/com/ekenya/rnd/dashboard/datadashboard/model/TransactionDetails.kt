package com.ekenya.rnd.dashboard.datadashboard.model


data class TransactionDetails(
    val access_token: String? = null,
    val refresh_token: String? = null,
    val user: User,
    val charge_amount: String,
    val merchant_name: String,
val merchant_city: String,
    val additional_data:MerchantData
)


