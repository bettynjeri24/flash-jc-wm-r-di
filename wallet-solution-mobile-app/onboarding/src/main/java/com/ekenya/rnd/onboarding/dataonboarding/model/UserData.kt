package com.ekenya.rnd.onboarding.dataonboarding.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData(
    val service_name: String?,
    val first_name: String?,
    val middle_name: String?,
    val surname: String?,
    val email_address: String?,
    @PrimaryKey
    @NonNull
    val id_number: String,
    val dob: String?,
    val phone_number: String?,
    val geolocation: String? = "Home",
    val user_agent_version: String? = "29(5.1.1)",
    val user_agent: String? = "android",
    val version: String
)