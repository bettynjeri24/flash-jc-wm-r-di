package com.ekenya.rnd.dashboard.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.data.model.*
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiServiceDashBoard
import com.ekenya.rnd.dashboard.datadashboard.model.*
import com.ekenya.rnd.dashboard.datadashboard.remote.APIErrorResponse
import com.ekenya.rnd.onboarding.dataonboarding.api.RemoteDataSource
import com.ekenya.rnd.onboarding.dataonboarding.model.RegistrationResponse2
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class MainRepository() {

    // val apiService: ApiService = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
    val apiServiceDashBoard: ApiServiceDashBoard =
        RemoteDataSource().buildApi(ApiServiceDashBoard::class.java)

    suspend fun login(apiHelper: ApiHelper, userData: MainDataObject): LoginResponse {
        return apiHelper.login(userData)
    }

    fun regiserUser(mUser: RegisterUserReq): LiveData<MyApiResponse> {
        val data = MutableLiveData<MyApiResponse>()

        apiServiceDashBoard.registerUserReq(mUser)
            .enqueue(object : Callback<RegisterUserResp> {
                override fun onResponse(
                    call: Call<RegisterUserResp>,
                    response: Response<RegisterUserResp>
                ) {
                    if (response.code() == 200) {
                        response.toString()
                        data.postValue(
                            MyApiResponse(
                                "RegisterUserReq",
                                response.body() as RegisterUserResp,
                                true,
                                response.code()
                            )
                        )
                    } else {
                        response.toString()
                        val errorResponse = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            APIErrorResponse::class.java
                        )
                        data.postValue(
                            MyApiResponse(
                                "RegisterUserReq",
                                errorResponse,
                                true,
                                response.code()
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<RegisterUserResp>, t: Throwable) {
                    handleAPIError(t, data, "RegisterUserReq")
                }
            })
        return data
    }

    fun doPhoneNumberLookup(mPhoneNumberLookupReq: PhoneNumberLookupReq): LiveData<MyApiResponse> {
        val data = MutableLiveData<MyApiResponse>()

        apiServiceDashBoard.phoneNumberLookupReq(mPhoneNumberLookupReq)
            .enqueue(object : Callback<RegisterUserResp> {
                override fun onResponse(
                    call: Call<RegisterUserResp>,
                    response: Response<RegisterUserResp>
                ) {
                    if (response.code() == 200) {
                        response.toString()
                        data.postValue(
                            MyApiResponse(
                                "PhoneNumberLookupReq",
                                response.body() as RegisterUserResp,
                                true,
                                response.code()
                            )
                        )
                    } else {
                        response.toString()
                        val errorResponse = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            APIErrorResponse::class.java
                        )
                        data.postValue(
                            MyApiResponse(
                                "PhoneNumberLookupReq",
                                errorResponse,
                                true,
                                response.code()
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<RegisterUserResp>, t: Throwable) {
                    handleAPIError(t, data, "PhoneNumberLookupReq")
                }
            })
        return data
    }

    fun userLoginCbg(mLoginUserReq: LoginUserReq): LiveData<MyApiResponse> {
        val data = MutableLiveData<MyApiResponse>()

        apiServiceDashBoard.loginUserReq(mLoginUserReq)
            .enqueue(object : Callback<LoginUserResp> {
                override fun onResponse(
                    call: Call<LoginUserResp>,
                    response: Response<LoginUserResp>
                ) {
                    if (response.code() == 200) {
                        response.toString()
                        data.postValue(
                            MyApiResponse(
                                "LoginUserReq",
                                response.body() as LoginUserResp,
                                true,
                                response.code()
                            )
                        )
                    } else {
                        response.toString()
                        val errorResponse = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            APIErrorResponse::class.java
                        )
                        data.postValue(
                            MyApiResponse(
                                "LoginUserReq",
                                errorResponse,
                                true,
                                response.code()
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<LoginUserResp>, t: Throwable) {
                    handleAPIError(t, data, "LoginUserReq")
                }
            })
        return data
    }

    fun getWalletBalance(mLoginUserReq: LoginUserReq): LiveData<MyApiResponse> {
        val data = MutableLiveData<MyApiResponse>()
        try {
            apiServiceDashBoard.walletBalanceReq(mLoginUserReq)
                .enqueue(object : Callback<WalletBalanceResp> {
                    override fun onResponse(
                        call: Call<WalletBalanceResp>,
                        response: Response<WalletBalanceResp>
                    ) {
                        if (response.code() == 200) {
                            response.toString()
                            data.postValue(
                                MyApiResponse(
                                    "WalletBalanceReq",
                                    response.body() as WalletBalanceResp,
                                    true,
                                    response.code()
                                )
                            )
                        } else {
                            response.toString()

                            val errorResponse = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                APIErrorResponse::class.java
                            )
                            data.postValue(
                                MyApiResponse(
                                    "WalletBalanceReq",
                                    errorResponse,
                                    true,
                                    response.code()
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<WalletBalanceResp>, t: Throwable) {
                        handleAPIError(t, data, "WalletBalanceReq")
                    }
                })
        } catch (e: Exception) {
            Log.e("Exception", "ERROR EXCEPTION IS $e")
        }
        return data
    }

    fun handleAPIError(t: Throwable, mData: MutableLiveData<MyApiResponse>, req: String) {
        if (t is IOException || t is UnknownHostException) {
            mData.postValue(
                MyApiResponse(
                    req,
                    null,
                    false,
                    300
                )
            )
        } else if (t is HttpException) {
            val jerror: String = t.response()?.errorBody()?.string()!!
            mData.postValue(
                MyApiResponse(
                    req,
                    jerror,
                    false,
                    t.code()
                )
            )
        }
    }

//    suspend fun registerUser(apiHelper: ApiHelper,userData: RegisterUserReq): RegisterUserResp
//    {
//        return apiHelper.registerUser(userData)
//    }

    suspend fun getWalletBalance(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): AccountBalanceResponse {
        return apiHelper.getWalletBalance(token, userData)
    }

    suspend fun getMiniStatement(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): MiniStateMentResponse {
        return apiHelper.getMiniStatement(token, userData)
    }

    suspend fun confirmUserRegistration(
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): LoginResponse {
        return apiHelper.confirmUserRegistration(userData)
    }

    suspend fun buyAirtimeReq(
        token: String,
        userData: BuyAirtimeReqWrapper,
        apiHelper: ApiHelper
    ): LoginResponse {
        return apiHelper.buyAirtime(token, userData)
    }

    suspend fun payBotswanaPower(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): LoginResponse {
        return apiHelper.payBotswanaPower(token, userData)
    }

    suspend fun getSavingsAccounts(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): LoginResponse {
        return apiHelper.getSavingsAccounts(token, userData)
    }

    suspend fun scanMerchantQRCode(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): LoginResponse {
        return apiHelper.scanMerchantQRCode(token, userData)
    }

    suspend fun getFullStatementViaEmail(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): LoginResponse {
        return apiHelper.getFullStatementViaEmail(token, userData)
    }

    suspend fun withDrawMoneytoMobileMoney(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): LoginResponse {
        return apiHelper.withDrawMoneytoMobileMoney(token, userData)
    }

    suspend fun checkIfUserisRegistered(
        apiHelper2: ApiHelper2,
        data: AccountLookUpPayload
    ): AccountLookUpResponse {
        return apiHelper2.checkIfUserIsRegistered(data)
    }

    suspend fun doBpcMetreNumberLookUp(
        apiHelper2: ApiHelper2,
        data: MetreNumber
    ): MetreNoLookupResponse {
        return apiHelper2.doBpcMetreNumberLookUp(data)
    }

    suspend fun getStatistics(
        apiHelper2: ApiHelper2,
        data: StatisticPayload
    ): StatisticResponse {
        return apiHelper2.getStatistics(data)
    }

    suspend fun verifyDefaultPin(
        apiHelper2: ApiHelper2,
        data: VerifyDefaultPinPayload
    ): AccountLookUpResponse {
        return apiHelper2.verifyDefaultPin(data)
    }

    suspend fun resendDeviceToken(
        apiHelper2: ApiHelper2,
        data: AccountLookUpPayload
    ): AccountLookUpResponse {
        return apiHelper2.resendDeviceToken(data)
    }

    suspend fun doDeviceLookUp(
        apiHelper2: ApiHelper2,
        data: DeviceLookUpPayload
    ): AccountLookUpResponse {
        return apiHelper2.doDeviceLookUp(data)
    }

    suspend fun verifyDeviceOtp(
        apiHelper2: ApiHelper2,
        data: VerifyDevicePayload
    ): AccountLookUpResponse {
        return apiHelper2.verifyDeviceOtp(data)
    }

    suspend fun verifyDeviceOtpLookUP(
        apiHelper2: ApiHelper2,
        data: VerifyDevicePayload
    ): AccountLookUpResponse {
        return apiHelper2.verifyDeviceOtpLookUP(data)
    }

    suspend fun registerUser2(
        apiHelper2: ApiHelper2,
        data: UserData,
        file: List<MultipartBody.Part>
    ): RegistrationResponse2 {
        return apiHelper2.registerUser2(data, file)
    }

    suspend fun topUpWallet(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): TopUpWalletResponse {
        return apiHelper.topUpWallet(token, userData)
    }

    suspend fun doAccountLookUP(
        token: String,
        apiHelper: ApiHelper,
        userData: MainDataObject
    ): TopUpWalletResponse {
        return apiHelper.doAccountLookUP(token, userData)
    }

    suspend fun doTrafficFineLookup(
        apiHelper2: ApiHelper2,
        userData: TrafficFinesLookupReq
    ): TrafficLookupResponse {
        return apiHelper2.trafficFineLookup(userData)
    }

    suspend fun doTrafficChargesLookup(
        apiHelper2: ApiHelper2,
        userData: TrafficFinesChargesLookupReq
    ): TrafficChargesLookupResponse {
        return apiHelper2.trafficChargesLookup(userData)
    }

    suspend fun doSubmitTrafficCharges(
        apiHelper2: ApiHelper2,
        userData: SubmitTrafficFineReq
    ): TrafficLookupResponse {
        return apiHelper2.doSubmitTrafficCharges(userData)
    }
}
