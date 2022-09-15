package com.ekenya.rnd.tijara.ui.auth.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.GenderItems
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.requestDTO.BasicInfoDTO
import com.ekenya.rnd.tijara.requestDTO.GenderDTO
import com.ekenya.rnd.tijara.requestDTO.ResetPassDTO
import com.ekenya.rnd.tijara.requestDTO.VerifyOtpDTO
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.TijaraRoomDatabase
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories.SaccoDetailsRepository
import com.ekenya.rnd.tijara.utils.PrefUtils
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

class RegistrationViewModel(private val app: Application) : AndroidViewModel(app) {

    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _genderCode = MutableLiveData<Int?>()
    val genderCode: MutableLiveData<Int?>
        get() = _genderCode
    private var _formId = MutableLiveData<Int?>()
    val formId: MutableLiveData<Int?>
        get() = _formId
    private var _username = MutableLiveData<String?>()
    val username: MutableLiveData<String?>
        get() = _username

    fun setFormId(value: Int) {
        _formId.value = value
    }

    var frontIdPhoto: File? = null
    var backIdPhoto: File? = null
    var passportPhoto: File? = null
    var selfiePhoto: File? = null

    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _photoMessage = MutableLiveData<String>()
    val photoMessage: LiveData<String>
        get() = _photoMessage
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _otpstatus = MutableLiveData<Int?>()
    val otpstatus: LiveData<Int?>
        get() = _otpstatus
    private var _resendStatus = MutableLiveData<Int?>()
    val resendStatus: LiveData<Int?>
        get() = _resendStatus
    private var _otpMessage = MutableLiveData<String>()
    val otpMessage: LiveData<String>
        get() = _otpMessage
    private var _resendMessage = MutableLiveData<String>()
    val resendMessage: LiveData<String>
        get() = _resendMessage
    private val viewModelJob = Job()
    private var _genderListProperties = MutableLiveData<List<GenderItems>>()
    val genderListProperties: LiveData<List<GenderItems>?>
        get() = _genderListProperties
    private lateinit var saccoRepository: SaccoDetailsRepository
    fun getAllSaccos(): LiveData<List<SaccoDetailEntity>> {
        return saccoRepository.allSaccoDetails
    }

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        val saccoDao = TijaraRoomDatabase.getDatabase(app).getAllSaccoDao()
        saccoRepository = SaccoDetailsRepository(saccoDao)
        _status.value = null
        _otpstatus.value = null
        _statusCode.value = null
        _genderCode.value = null
        _resendStatus.value = null
        _isObserving.value = false
        loadGenderNames()
    }

    fun registerBasicInfo(basicInfoDTO: BasicInfoDTO) {
        uiScope.launch {
            val userPersonalInfo = SaccoApi.retrofitService.registerClientInfoAsync(basicInfoDTO)
            try {
                val userInfoResponse = userPersonalInfo.await()
                if (userInfoResponse.toString().isNotEmpty()) {
                    Timber.d("REGISTER USER PERSONAL INFO RESPONSE: $userInfoResponse")
                    when (userInfoResponse.status) {
                        1 -> {
                            _formId.value = userInfoResponse.data.formId
                            _status.value = userInfoResponse.status
                        }
                        0 -> {
                            _statusMessage.value = userInfoResponse.message
                            _status.value = 0
                        }
                    }
                }
            } catch (e: HttpException) {
                _status.value = e.code()
                Timber.d("REGISTER ERROR: ${e.message()}")
            }
        }
    }

    /**get Gender lists*/
    private fun loadGenderNames() {
        uiScope.launch {
            val genderDTO = GenderDTO()
            genderDTO.org_id = Constants.SIGNUPORGID
            Timber.d("GENDER DTO $genderDTO")
            val genderProperties = SaccoApi.retrofitService.getGender(genderDTO)
            try {
                val genderResult = genderProperties.await()
                if (genderResult.toString().isNotEmpty()) {
                    Timber.d("GENDER RESULTS $genderResult")
                    if (genderResult.status == 1) {
                        /**for the first item in the list*/
                        /* val first= GenderItems("F",0,1,"Gender",0)
                         val firstItem=ArrayList<GenderItems>()
                         firstItem.add(first)
                         firstItem.addAll(genderResult.data)*/
                        _genderListProperties.value = genderResult.data
                        _genderCode.value = genderResult.status
                    } else if (genderResult.status == 0) {
                        _genderCode.value = genderResult.status
                    }
                }
            } catch (e: HttpException) {
                _genderCode.value = e.code()
                Timber.e("ERROR GENDER ${e.message}")
            }
        }
    }

    fun uploadPhotos(kra: String, email: String) {
        uiScope.launch {
            val compressedfrontId = Compressor.compress(app.applicationContext, frontIdPhoto!!)
            val compressedbackId = Compressor.compress(app.applicationContext, backIdPhoto!!)
            val compressedselfie = Compressor.compress(app.applicationContext, selfiePhoto!!)
            // val compressedpassport = Compressor.compress(app.applicationContext, passportPhoto!!)
            val frontPhotoRequestBody =
                createMultipartRequestBody(compressedfrontId, "id_front_photo", "image/*")
            val backPhotoRequestBody =
                createMultipartRequestBody(compressedbackId, "id_back_photo", "image/*")
            val selfieRequestBody =
                createMultipartRequestBody(compressedselfie, "selfie_photo", "image/*")
            // val passportRequestBody = createMultipartRequestBody(compressedpassport, "tmp_passport_photo", "image/*")
            val emailBody = createRequestBody(email)
            val kraBody = createRequestBody(kra)
            val formIdBody = createRequestBody(_formId.value!!)
            val photoRequest = SaccoApi.retrofitService.uploadIdPhotosAsync(
                frontPhotoRequestBody,
                backPhotoRequestBody,
                selfieRequestBody,
                formId = formIdBody,
                krapin = kraBody,
                email = emailBody
            )
            try {
                val photoResponse = photoRequest.await()
                if (photoResponse.toString().isNotEmpty()) {
                    Timber.d("REGISTER USER PERSONAL INFO RESPONSE: $photoResponse")
                    when (photoResponse.status) {
                        1 -> {
                            _username.value = photoResponse.data.username
                            Constants.RegUsername = photoResponse.data.username
                            PrefUtils.setPreference(
                                app.applicationContext,
                                app.applicationContext.getString(R.string.stored_username),
                                photoResponse.data.username
                            )
                            Constants.userFname = photoResponse.data.firstName
                            val items = photoResponse.data.associatedOrgs.map {
                                SaccoDetailEntity(
                                    it.name,
                                    it.firstName,
                                    it.username,
                                    it.orgId,
                                    it.isSacco
                                )
                            }
                            saccoRepository.insertSacco(items)
                            Constants.SaccoName = items.first().name.trim()
                            Constants.isSacco = items.first().isSacco
                            _statusCode.value = photoResponse.status
                        }
                        0 -> {
                            _photoMessage.value = photoResponse.message
                            _statusCode.value = 0
                        }
                    }
                }
            } catch (e: HttpException) {
                _statusCode.value = e.code()
                Timber.d("REGISTER ERROR: ${e.message()}")
            }
        }
    }

    fun upPassPortPhotos(kra: String, email: String) {
        uiScope.launch {
            val compressedselfie = Compressor.compress(app.applicationContext, selfiePhoto!!)
            val compressedpassport = Compressor.compress(app.applicationContext, passportPhoto!!)
            val selfieRequestBody =
                createMultipartRequestBody(compressedselfie, "selfie_photo", "image/*")
            val passportRequestBody =
                createMultipartRequestBody(compressedpassport, "tmp_passport_photo", "image/*")
            val emailBody = createRequestBody(email)
            val kraBody = createRequestBody(kra)
            val formIdBody = createRequestBody(_formId.value!!)
            val photoRequest = SaccoApi.retrofitService.uploadIdPhotosAsync(
                selfieRequestBody,
                passportRequestBody,
                formId = formIdBody,
                krapin = kraBody,
                email = emailBody
            )
            try {
                val photoResponse = photoRequest.await()
                if (photoResponse.toString().isNotEmpty()) {
                    Timber.d("REGISTER USER PERSONAL INFO RESPONSE: $photoResponse")
                    when (photoResponse.status) {
                        1 -> {
                            _username.value = photoResponse.data.username
                            Constants.RegUsername = photoResponse.data.username
                            Constants.userFname = photoResponse.data.firstName
                            PrefUtils.setPreference(
                                app.applicationContext,
                                app.applicationContext.getString(R.string.stored_username),
                                photoResponse.data.username.toString()
                            )
                            val items = photoResponse.data.associatedOrgs.map {
                                SaccoDetailEntity(
                                    it.name,
                                    it.firstName,
                                    it.username,
                                    it.orgId,
                                    it.isSacco
                                )
                            }
                            saccoRepository.insertSacco(items)
                            Constants.SaccoName = items.first().name.trim()
                            Constants.isSacco = items.first().isSacco

                            _statusCode.value = photoResponse.status
                        }
                        0 -> {
                            _photoMessage.value = photoResponse.message
                            _statusCode.value = 0
                        }
                    }
                }
            } catch (e: HttpException) {
                _statusCode.value = e.code()
                Timber.d("REGISTER ERROR: ${e.message()}")
            }
        }
    }

    fun verifyRegOTP(verifyOtpDTO: VerifyOtpDTO) {
        uiScope.launch {
            val getOtpResults = SaccoApi.retrofitService.verifyOTP(verifyOtpDTO)
            try {
                val otpResult = getOtpResults.await()
                if (otpResult.toString().isNotEmpty()) {
                    Timber.d("OTP DETAILS RESPONSE $otpResult")
                    if (otpResult.status == 1) {
                        _otpMessage.value = otpResult.message
                        _otpstatus.value = 1
                    }
                    if (otpResult.status == 0) {
                        _otpMessage.value = otpResult.message
                        _otpstatus.value = otpResult.status
                    }
                }
            } catch (e: HttpException) {
                _otpstatus.value = e.code()
                Timber.e("OTP ERROR ${e.message}")
            }
        }
    }

    fun resendOTP(resetPassDTO: ResetPassDTO) {
        uiScope.launch {
            val getResults = SaccoApi.retrofitService.resendRegOtp(resetPassDTO)
            try {
                val result = getResults.await()
                if (result.toString().isNotEmpty()) {
                    Timber.d("OTP DETAILS RESPONSE $result")
                    if (result.status == 1) {
                        _resendMessage.value = result.message
                        _resendStatus.value = 1
                    }
                    if (result.status == 0) {
                        _resendMessage.value = result.message
                        _resendStatus.value = result.status
                    }
                }
            } catch (e: HttpException) {
                _otpstatus.value = e.code()
                Timber.e("OTP ERROR ${e.message}")
            }
        }
    }

    fun stopObserving() {
        _status.value = null
        _statusCode.value = null
        _otpstatus.value = null
        _resendStatus.value = null
        _isObserving.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
