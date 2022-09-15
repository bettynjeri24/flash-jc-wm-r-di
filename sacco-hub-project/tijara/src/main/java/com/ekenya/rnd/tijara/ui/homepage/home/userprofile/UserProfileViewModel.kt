package com.ekenya.rnd.tijara.ui.homepage.home.userprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.GenderItems
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.requestDTO.BasicInfoDTO
import com.ekenya.rnd.tijara.requestDTO.GenderDTO
import com.ekenya.rnd.tijara.requestDTO.ResetPassDTO
import com.ekenya.rnd.tijara.requestDTO.VerifyOtpDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.TijaraRoomDatabase
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories.SaccoDetailsRepository
import com.ekenya.rnd.tijara.utils.createMultipartRequestBody
import com.ekenya.rnd.tijara.utils.createRequestBody
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.File

class UserProfileViewModel(private val app: Application) : AndroidViewModel(app) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode

    var selfiePhoto:File?=null

    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private val viewModelJob = Job()
    private var _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl


    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _isObserving.value = false
    }


    fun uploadProfile(){
        uiScope.launch {

            val compressedselfie = Compressor.compress(app.applicationContext, selfiePhoto!!)
            val selfieRequestBody = createMultipartRequestBody(compressedselfie, "selfie_photo", "image/*")
            val photoRequest= SaccoApi.retrofitService.
            uploadProfileAsync(selfieRequestBody)
            try {
                val photoResponse=photoRequest.await()
                if (photoResponse.toString().isNotEmpty()){
                    Timber.d("REGISTER USER PERSONAL INFO RESPONSE: $photoResponse")
                    when (photoResponse.status) {
                        1 -> {
                            _imageUrl.value=photoResponse.data.facePhotoUrl
                            _statusCode.value=photoResponse.status
                        }
                        0 -> {
                            _statusMessage.value=photoResponse.message
                            _statusCode.value=0
                        }
                    }

                }
            }catch (e: HttpException){
                _statusCode.value = e.code()
                Timber.d("REGISTER ERROR: ${e.message()}")

            }
        }

    }

    fun stopObserving(){
        _statusCode.value=null
        _isObserving.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}