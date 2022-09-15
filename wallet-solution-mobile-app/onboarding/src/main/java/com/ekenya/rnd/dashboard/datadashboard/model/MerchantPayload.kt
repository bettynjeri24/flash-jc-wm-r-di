package com.ekenya.rnd.dashboard.datadashboard.model

data class MerchantPayload(
    val service_name: String = "MerchantQrCode",
    val qr_code: String,
    val geolocation: String = "Home",
    val user_agent: String = "android",
    val user_agent_version: String = "5.1"
)
