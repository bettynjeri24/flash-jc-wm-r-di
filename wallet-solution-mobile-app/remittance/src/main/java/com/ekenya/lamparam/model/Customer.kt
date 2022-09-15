package com.ekenya.lamparam.model

import com.google.gson.annotations.SerializedName

data class Customer (

    @SerializedName("walletAccountNo") var walletAccountNo   : String?  = null,

)
