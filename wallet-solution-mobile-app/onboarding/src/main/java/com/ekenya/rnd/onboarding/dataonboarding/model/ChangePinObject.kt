package com.ekenya.rnd.onboarding.dataonboarding.model

data class ChangePinObject (val service_name: String?,
                                val phone_number: String?,
                                val password: String?,
                                val otp_token: String?,
                                val grant_type : String?,
                                val geolocation : String?,
                                val user_agent_version : String?,
                                val user_agent : String?)
