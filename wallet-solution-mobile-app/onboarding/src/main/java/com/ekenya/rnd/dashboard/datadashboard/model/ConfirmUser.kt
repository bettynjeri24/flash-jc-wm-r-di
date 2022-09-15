package com.ekenya.rnd.dashboard.datadashboard.model

data class ConfirmUser (val service_name: String,
val phone_number: String,
val password: String,
val otp_token:String,
val grant_type: String,
val geolocation: String,
val user_agent_version: String,
val user_agent: String)
