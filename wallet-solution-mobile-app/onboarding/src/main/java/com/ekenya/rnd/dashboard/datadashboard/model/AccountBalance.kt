package com.ekenya.rnd.dashboard.datadashboard.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class AccountBalance (

    val  available_balance:Double,

    val  actual_balance:Double,
    @PrimaryKey
    @NonNull
    val account_name :String,
    )