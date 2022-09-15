package com.ekenya.rnd.common.data.model

data class TrafficFinesLookupReq(val requestType:String,val region:String,val license_grade:String,
                                 val license_number:String, val phone_number:String)
