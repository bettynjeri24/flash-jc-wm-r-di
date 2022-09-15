package com.ekenya.rnd.dashboard.datadashboard.model

import com.ekenya.lamparam.model.Customer
import com.google.gson.annotations.SerializedName

data class LoginUserResp (

    @SerializedName("status"   ) var status   : Int?    = null,
    @SerializedName("messages" ) var messages : String? = null,
    @SerializedName("data"     ) var data     : DataToken?   = DataToken()

)
data class DataToken (
    @SerializedName("accessToken" ) var accessToken : String? = null,
    @SerializedName("customer") var customer : Customer? = Customer()
)