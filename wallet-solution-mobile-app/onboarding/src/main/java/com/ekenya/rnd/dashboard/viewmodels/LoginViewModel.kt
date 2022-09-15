package com.ekenya.rnd.dashboard.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ekenya.rnd.common.data.model.*
import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.model.*
import com.ekenya.rnd.dashboard.repositories.MainRepository
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class LoginViewModel(
    private val mainRepository: MainRepository,
    private val apiHelper: ApiHelper,
    private val apiHelper2: ApiHelper2
) : ViewModel() {
    private val _bioMetric = MutableLiveData<Boolean>()
    val bioMetric: LiveData<Boolean>
        get() = _bioMetric

    private var regUserLiveData: LiveData<MyApiResponse>? = null



    fun checkedBiometrics() {
        _bioMetric.value = false
    }

    fun login(userData: MainDataObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.login(apiHelper, userData)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

//    fun registerUser(user: RegisterUserReq) = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            emit(Resource.success(data = mainRepository.registerUser(apiHelper,user)))
//        } catch (exception: Exception) {
//            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
//        }
//    }

    fun registerUser(user: RegisterUserReq): LiveData<MyApiResponse> {

        regUserLiveData = mainRepository.regiserUser(user)

        return regUserLiveData!!
    }

    fun userLoginCbg(user: LoginUserReq): LiveData<MyApiResponse> {

        regUserLiveData = mainRepository.userLoginCbg(user)

        return regUserLiveData!!
    }

    fun getWalletBalance(user: LoginUserReq): LiveData<MyApiResponse> {

        regUserLiveData = mainRepository.getWalletBalance(user)

        return regUserLiveData!!
    }


    fun performLookup(mPhoneNumberLookupReq: PhoneNumberLookupReq): LiveData<MyApiResponse> {

        regUserLiveData = mainRepository.doPhoneNumberLookup(mPhoneNumberLookupReq)

        return regUserLiveData!!
    }

    fun checkIfUserIsRegistered(data: AccountLookUpPayload) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.checkIfUserisRegistered(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun doBpcMetreNumberLookUp(data: MetreNumber) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.doBpcMetreNumberLookUp(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getStatistics(data: StatisticPayload) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getStatistics(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun verifyDefaultPin(data: VerifyDefaultPinPayload) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.verifyDefaultPin(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun resendDeviceToken(data: AccountLookUpPayload) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.resendDeviceToken(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun doDeviceLookUp(data: DeviceLookUpPayload) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.doDeviceLookUp(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun verifyDeviceOtp(data: VerifyDevicePayload) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.verifyDeviceOtp(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun verifyDeviceOtpLookUp(data: VerifyDevicePayload) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.verifyDeviceOtpLookUP(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun registerUser(data: UserData, file: List<MultipartBody.Part>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.registerUser2(apiHelper2, data, file)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun doTrafficFinesLookUP(data: TrafficFinesLookupReq) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.doTrafficFineLookup(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun doTrafficFinesChargesLookUP(data: TrafficFinesChargesLookupReq) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.doTrafficChargesLookup(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun doSubmitTrafficFine(data:SubmitTrafficFineReq) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.doSubmitTrafficCharges(apiHelper2, data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun bioMetrics() {
        _bioMetric.value = true
    }
}