package com.ekenya.rnd.common.sms

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SmsService {

    @Headers("Content-Type: application/json")
    @POST("ServiceLayer/pgsms/send")
    suspend fun sendSms(@Body smsRequest: SmsRequest) : SmsResponse

}