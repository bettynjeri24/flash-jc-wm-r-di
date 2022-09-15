package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.GenderItems
import com.ekenya.rnd.tijara.network.model.RShipList
import com.ekenya.rnd.tijara.requestDTO.GenderDTO
import com.ekenya.rnd.tijara.requestDTO.NextKinDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class NextKinViewModel @Inject constructor(application:Application):AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _genderListProperties = MutableLiveData<List<GenderItems>>()
    val genderListProperties: LiveData<List<GenderItems>?>
        get() = _genderListProperties
    private var _rShipListProperties = MutableLiveData<List<RShipList>>()
    val rShipListProperties: LiveData<List<RShipList>?>
        get() = _rShipListProperties
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    init {
        _statusCode.value=null
        getRship()
        loadGenderNames()

    }
    fun nextOfKinInfo(nextKinDTO: NextKinDTO){
        uiScope.launch {
            Timber.d("RELATIONSHIP REQUEST$nextKinDTO")
            val rshipProperties= SaccoApi.retrofitService.nextKin(nextKinDTO)
            try {
                val rshipResponse=rshipProperties.await()
                if (rshipResponse.toString().isNotEmpty()){
                    Timber.d("RSHIP RESPONSE $rshipResponse")
                    if (rshipResponse.status==1){
                        _statusCode.value=rshipResponse.status
                    }else if (rshipResponse.status==0){
                        _statusCode.value=rshipResponse.status
                        _statusMessage.value=rshipResponse.message
                    }
                }
            }catch (e:HttpException){
                if (e.code()==422){
                    _statusCode.value=e.code()
                }
            }
        }

    }
    private fun getRship(){
        uiScope.launch {
            val rshipTypes= SaccoApi.retrofitService.getRshipTypes()
            try {
                val typeResult=rshipTypes.await()
                if (typeResult.toString().isNotEmpty()){
                    Timber.d("RELATIONSHIP TYPES RESULTS $typeResult")
                    if(typeResult.status==1){
                        /**for the first item in the list*/
                        val first= RShipList("0",0,0,"Select Relationship",0)
                        val firstItem=ArrayList<RShipList>()
                        firstItem.add(first)
                        firstItem.addAll(typeResult.data)
                        _rShipListProperties.value=firstItem


                    }else{

                    }
                }
            }catch (e: Exception){
                Timber.e("RELATIONSHIP ERROR  ${e.message}")
            }
        }

    }
    /**get Gender lists*/
    fun loadGenderNames(){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val genderDTO= GenderDTO()
            genderDTO.org_id= Constants.ORGID
            val genderProperties= SaccoApi.retrofitService.getGender(genderDTO)
            try {
                val genderResult=genderProperties.await()
                if (genderResult.toString().isNotEmpty()){
                    Timber.d("GENDER RESULTS $genderResult")
                    if(genderResult.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        /**for the first item in the list*/
                        val first= GenderItems("F",0,1,"Select Gender",0)
                        val firstItem=ArrayList<GenderItems>()
                        firstItem.add(first)
                        firstItem.addAll(genderResult.data)

                        _genderListProperties.value=firstItem


                    }else{

                    }
                }
            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR GENDER ${e.message}")
            }
        }
    }
    fun stopObserving(){
        _statusCode.value=null
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}