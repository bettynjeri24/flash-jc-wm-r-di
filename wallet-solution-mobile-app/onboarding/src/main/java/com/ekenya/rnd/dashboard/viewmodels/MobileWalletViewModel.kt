package com.ekenya.rnd.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ekenya.rnd.common.data.model.BuyAirtimeReqWrapper
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.repositories.MainRepository
import kotlinx.coroutines.Dispatchers

class MobileWalletViewModel(
    private val mainRepository: MainRepository,
    private val apiHelper: ApiHelper
) : ViewModel() {
    fun getWalletBalance(token: String, userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.getWalletBalance(
                        token,
                        apiHelper,
                        userData
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun getMiniStatement(token: String, userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.getMiniStatement(
                        token,
                        apiHelper,
                        userData
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun confirmUserRegistration(userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.confirmUserRegistration(
                        apiHelper,
                        userData
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun forgotPassword(userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.confirmUserRegistration(
                        apiHelper,
                        userData
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun topUpWallet(token: String, topUpData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.topUpWallet(token, apiHelper, topUpData)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun buyAirtime(token: String, userData:BuyAirtimeReqWrapper) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.buyAirtimeReq(
                        token,
                        userData,
                        apiHelper
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun payBotswanaPower(token: String, userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.payBotswanaPower(
                        token,
                        apiHelper,
                        userData
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun buyAirtimeReq(token: String,data:BuyAirtimeReqWrapper) = liveData(Dispatchers.IO)
    {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.buyAirtimeReq(
                        token,
                        data,
                        apiHelper
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getSavingsAccounts(token: String, userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.getSavingsAccounts(
                        token,
                        apiHelper,
                        userData
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun scanMerchantQRCode(token: String, userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.scanMerchantQRCode(
                        token,
                        apiHelper,
                        userData
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun getFullStatementViaEmail(token: String, userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.getFullStatementViaEmail(
                        token,
                        apiHelper,
                        userData
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun withDrawMoneytoMobileMoney(token: String, userData: MainDataObject) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(
                    Resource.success(
                        data = mainRepository.withDrawMoneytoMobileMoney(
                            token,
                            apiHelper,
                            userData
                        )
                    )
                )
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun doAccountLookUP(token: String, data: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.doAccountLookUP(token, apiHelper, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}