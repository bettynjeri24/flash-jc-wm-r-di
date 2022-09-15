package com.ekenya.rnd.common.data.model

import java.time.temporal.TemporalAmount

data class TrafficFinesChargesLookupReq(val requestType:String, val region:String, val license_grade:String,
                                        val license_number:String, val phone_number:String, val offense_id:String)
