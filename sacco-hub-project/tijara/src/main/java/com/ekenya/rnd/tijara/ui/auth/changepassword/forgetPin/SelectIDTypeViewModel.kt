package com.ekenya.rnd.tijara.ui.auth.changepassword.forgetPin

import android.app.Application
import androidx.lifecycle.*
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.IDType
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
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
import java.lang.Exception

class SelectIDTypeViewModel (private val app: Application) : AndroidViewModel(app) {

    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _identityType = MutableLiveData<List<IDType>>()
    val identityType: LiveData<List<IDType>?>
        get() = _identityType
    private val viewModelJob = Job()
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _statusCMessage = MutableLiveData<String>()
    val statusCMessage: LiveData<String>
        get() = _statusCMessage
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _resetOption = MutableLiveData<String>()
    val resetOption: MutableLiveData<String>
        get() = _resetOption
    private var _username = MutableLiveData<String>()
    val username: MutableLiveData<String>
        get() = _username
     private var _typeID = MutableLiveData<String>()
     var typeID :LiveData<String> = _typeID
    var selfiePhoto: File?=null
    fun setResetOption(value:String){
        _resetOption.value=value
    }
    fun setUsername(value:String){
        _username.value=value
    }
    fun setTypeID(value:String){
        _typeID.value=value
    }
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    init {
        _status.value=null
        _isObserving.value = false
        _statusCode.value=null
        loadIdentityTypes()

    }
    /**get saving account dropdown items*/
    private fun loadIdentityTypes(){
        uiScope.launch {
            val typeRequest= SaccoApi.retrofitService.getIdentityType()
            try {
                val typeResponse=typeRequest.await()
                if (typeResponse.toString().isNotEmpty()){
                    Timber.d("SAVING RESULTS $typeResponse")
                    if(typeResponse.status==1){
                        /**for the first item in the list*/
                        _identityType.value=typeResponse.data
                        _statusCode.value=typeResponse.status
                    }else if (typeResponse.status==0){
                        _statusCode.value=typeResponse.status
                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
                _statusCode.value=e.hashCode()
            }
        }
    }
    fun resetPin(idType:String,resetOption:String){
        uiScope.launch {
            val compressedSelfie = Compressor.compress(app.applicationContext, selfiePhoto!!)
            val selfieRequestBody = createMultipartRequestBody(compressedSelfie, "selfiePhoto", "image/*")
            val resetBody = createRequestBody(resetOption)
            val idBody= createRequestBody(idType)
            val resetRequest= SaccoApi.retrofitService.
            resetPinsAsync(selfieRequestBody, resetOption = resetBody, idNumber = idBody)
            try {
                val resetResponse=resetRequest.await()
                    when (resetResponse.status) {
                        1 -> {
                            _statusMessage.value=resetResponse.message
                            _username.value=resetResponse.data.username
                            _status.value=resetResponse.status
                        }
                        0 -> {
                            _statusMessage.value=resetResponse.message
                            _status.value=0
                        }
                    }


            }catch (e: Exception){
                _status.value = e.hashCode()
            }
        }

    }

    fun stopObserving(){
        _status.value=null
        _isObserving.value = false
      //  _statusCode.value=null
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}