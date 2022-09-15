package com.ekenya.rnd.common.sms


import com.ekenya.rnd.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ISendSmsRepository {
    fun sendSms(smsRequest: SmsRequest) : Flow<Resource<SmsResponse>>
}