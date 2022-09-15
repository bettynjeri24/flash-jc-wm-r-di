package com.ekenya.rnd.dashboard.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class TestMainViewModel(private val mainRepository: MainRepository, private val apiHelper: ApiHelper, private val apiHelper2: ApiHelper2) : ViewModel() {

    fun registerUser(userData: UserData,file: List<MultipartBody.Part>) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.registerUser2(apiHelper2,userData,file )))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}