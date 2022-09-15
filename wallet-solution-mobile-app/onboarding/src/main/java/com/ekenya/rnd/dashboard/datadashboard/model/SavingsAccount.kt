package com.ekenya.rnd.dashboard.datadashboard.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavingsAccount(
    @PrimaryKey
    @NonNull
    val savings_title: String,
    val savings_category: String?,
    val amount_Saved: String?,
    val target_amount: String?,
    val deadline: String?,
)
