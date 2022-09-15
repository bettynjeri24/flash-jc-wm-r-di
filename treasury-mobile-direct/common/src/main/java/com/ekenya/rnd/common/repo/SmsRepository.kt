package com.ekenya.rnd.common.repo

import com.ekenya.rnd.common.sms.ISendSmsRepository
import com.ekenya.rnd.common.sms.SmsRequest
import com.ekenya.rnd.common.sms.SmsService
import com.ekenya.rnd.common.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SmsRepository @Inject constructor(
    private val smsService: SmsService
) : ISendSmsRepository {

    override fun sendSms(smsRequest: SmsRequest) = flow{
        try {
            emit(Resource.Loading())
            val smsResponse = smsService.sendSms(smsRequest)
            emit(Resource.Success(smsResponse))
        }catch (exception : Exception){
            emit(Resource.Error(exception))
        }
    }.flowOn(Dispatchers.Default)

}