package com.ekenya.rnd.dashboard.datadashboard.api

import com.ekenya.rnd.common.data.model.BuyAirtimeReqWrapper
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.dashboard.datadashboard.model.RegisterUserReq

class ApiHelper(private val apiServiceDashBoard: ApiServiceDashBoard) {

    suspend fun login(data: MainDataObject) = apiServiceDashBoard.login(data)

    fun registerUser(data:RegisterUserReq) = apiServiceDashBoard.registerUserReq(data)

    suspend fun getWalletBalance(token: String, data: MainDataObject) =
        apiServiceDashBoard.getWalletBalance("Bearer " + token, data)

    suspend fun getSavingsAccounts(token: String, data: MainDataObject) =
        apiServiceDashBoard.getSavingsAccounts("Bearer " + token, data)

    suspend fun scanMerchantQRCode(token: String, data: MainDataObject) =
        apiServiceDashBoard.scanMerchantQRCode("Bearer " + token, data)

    suspend fun getMiniStatement(token: String, data: MainDataObject) =
        apiServiceDashBoard.getMiniStatement("Bearer " + token, data)

    suspend fun confirmUserRegistration(data: MainDataObject) =
        apiServiceDashBoard.confirmUserRegistration(data)

    suspend fun buyAirtime(token: String, data: BuyAirtimeReqWrapper) =
        apiServiceDashBoard.buyAirtime("Bearer " + token,data)

    suspend fun payBotswanaPower(token: String, data: MainDataObject) =
        apiServiceDashBoard.payBotswanaPower("Bearer " + token, data)

    suspend fun getFullStatementViaEmail(token: String, data: MainDataObject) =
        apiServiceDashBoard.getFullStatementViaEmail("Bearer " + token, data)

    suspend fun withDrawMoneytoMobileMoney(token: String, data: MainDataObject) =
        apiServiceDashBoard.withDrawMoneytoMobileMoney("Bearer " + token, data)


    suspend fun topUpWallet(token: String, data: MainDataObject) =
        apiServiceDashBoard.topUpWallet("Bearer " + token, data)

    suspend fun doAccountLookUP(token: String, data: MainDataObject) =
        apiServiceDashBoard.doAccountLookUP("Bearer " + token, data)


    /*suspend fun fileUpload( token:String,data: com.ekenya.rnd.onboarding.data.model.UserData) =
        apiService.fileUpload( data)*/
}