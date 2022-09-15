package com.ekenya.rnd.dashboard.datadashboard.model

import com.google.gson.annotations.SerializedName

data class WalletBalanceResp (
    @SerializedName("status"   ) var status   : Int?    = null,
    @SerializedName("balance" ) var balance : Int? = null
)
