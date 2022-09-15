package com.ekenya.rnd.dashboard.datadashboard.api

import com.ekenya.rnd.common.data.model.*
import com.ekenya.rnd.dashboard.datadashboard.model.*
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import okhttp3.MultipartBody

class ApiHelper2(private val apiService2: ApiService2)
{
    suspend fun verifyDefaultPin(   data: VerifyDefaultPinPayload) =apiService2.verifyDefaultPin( data)
    suspend fun checkIfUserIsRegistered(   data: AccountLookUpPayload) =apiService2.checkIfUserIsRegistered( data)
    suspend fun doBpcMetreNumberLookUp(   data: MetreNumber) =apiService2.doBpcMetreNumberLookUp( data)
    suspend fun getStatistics(   data: StatisticPayload) =apiService2.getStatistics( data)
    suspend fun resendDeviceToken(   data: AccountLookUpPayload) =apiService2.resendDeviceToken( data)
    suspend fun doDeviceLookUp(   data: DeviceLookUpPayload) =apiService2.doDeviceLookUp( data)
    suspend fun verifyDeviceOtp(   data: VerifyDevicePayload) =apiService2.verifyDeviceOtp( data)
    suspend fun verifyDeviceOtpLookUP(   data: VerifyDevicePayload) =apiService2.verifyDeviceOtpLookUP( data)
    suspend fun registerUser2( userData: UserData,file: List<MultipartBody.Part>) = apiService2.registerUser(userData,file)
    suspend fun trafficFineLookup(data: TrafficFinesLookupReq) =
        apiService2.doTrafficFinesLookup(data)
    suspend fun trafficChargesLookup(data: TrafficFinesChargesLookupReq) =
        apiService2.doTrafficChargesLookup(data)
    suspend fun doSubmitTrafficCharges(data: SubmitTrafficFineReq) =
        apiService2.doSubmitTrafficCharges(data)
//    suspend fun submitTrafficFine(data:TrafficFinesReq) =
//        apiService2.doTrafficFinesLookup(data)
}