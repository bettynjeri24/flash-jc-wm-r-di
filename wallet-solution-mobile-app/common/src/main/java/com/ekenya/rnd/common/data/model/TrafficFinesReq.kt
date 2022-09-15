package com.ekenya.rnd.common.data.model

data class TrafficFinesReq(val requestType:String,val region:String,val license_grade:String,
val license_number:String,val penalty_invoice:String,val offense_code:String)

