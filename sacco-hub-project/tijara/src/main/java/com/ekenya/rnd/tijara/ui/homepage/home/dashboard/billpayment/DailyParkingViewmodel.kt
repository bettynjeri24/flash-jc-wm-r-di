package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.*
import com.ekenya.rnd.tijara.requestDTO.ParkingLookUpDTO
import com.ekenya.rnd.tijara.requestDTO.ParkingTDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories.ParkingZoneRepository
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.TijaraRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception

class DailyParkingViewmodel(application: Application) : AndroidViewModel(application) {
    val app=application
    private lateinit var parkingZoneRepository: ParkingZoneRepository
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusCodeParking = MutableLiveData<Int?>()
    val statusCodeParking: MutableLiveData<Int?>
        get() = _statusCodeParking
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _airtimeResponse = MutableLiveData<AirtimeData>()
    val airtimeResponse: LiveData<AirtimeData>
        get() = _airtimeResponse
    private var _vehicleTypes = MutableLiveData<List<VehiclesTypes>>()
    val vehicleTypes: LiveData<List<VehiclesTypes>?>
        get() = _vehicleTypes
    private var _duration = MutableLiveData<List<ParkingDuration>>()
    val duration: LiveData<List<ParkingDuration>?>
        get() = _duration
    private var _parkingZone = MutableLiveData<List<ParkingZone>>()
    var parkingZone: LiveData<List<ParkingZone>>
        get() = _parkingZone
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
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
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        val parkingZoneDao = TijaraRoomDatabase.getDatabase(application).getParkingZoneDao()
        parkingZoneRepository= ParkingZoneRepository(parkingZoneDao)
        parkingZone=parkingZoneRepository.allParkingZone
        _statusCode.value=null
        _statusCodeParking.value=null
        _status.value=null
        _isObserving.value = false
        loadVehiclesTypes()

    }
    fun parkingLookup(parkingLookUpDTO: ParkingLookUpDTO){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val lookupRequest= SaccoApi.retrofitService.parkingLookUp(parkingLookUpDTO)

            try {
                val lookupResponse=lookupRequest.await()
                    Timber.d("LOOKUP RESPONSE $lookupResponse")
                    if (lookupResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _statusCode.value=lookupResponse.status
                    }else if (lookupResponse.status==0){
                        _statusMessage.value=lookupResponse.message
                        _statusCode.value=lookupResponse.status

                }
            }catch (e: HttpException){
                Timber.e("ERROR ${e.message}")
                _statusCode.value=e.code()
            }

        }
    }
    fun airtimeCommit(statementDTO: StatementDTO){
        uiScope.launch {
            val airtimeRequest= SaccoApi.retrofitService.airtimeCommit(statementDTO)

            try {
                val airtimeCResponse=airtimeRequest.await()
                    Timber.d("AIRTIME COMMIT RESPONSE $airtimeCResponse")
                    if (airtimeCResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _status.value=1
                        Constants.AIRTIMEREFID=airtimeCResponse.data.transactionCode

                    }else if (airtimeCResponse.status==0){
                        _statusCMessage.value=airtimeCResponse.message
                        _statusCode.value=airtimeCResponse.status
                    }

            }catch (e: HttpException){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _status.value=e.code()
            }

        }
    }


    /**get saving account dropdown items*/
    private fun loadVehiclesTypes(){
        uiScope.launch {
            val parkingTDTO=ParkingTDTO()
            parkingTDTO.billerCode=Constants.BILLERCODE
            val vehiclesRequest= SaccoApi.retrofitService.getVehicleTypes(parkingTDTO)
            try {
                val vehicleRespnse=vehiclesRequest.await()
                    Timber.d("SAVING RESULTS $vehicleRespnse")
                    if(vehicleRespnse.status==1){
                        /**for the first item in the list*/
                        _statusCodeParking.value=vehicleRespnse.status
                        if (vehicleRespnse.data.vehiclesTypes.isNotEmpty()){
                            _vehicleTypes.value=vehicleRespnse.data.vehiclesTypes
                        }
                        if (vehicleRespnse.data.parkingDurations.isNotEmpty()){
                            _duration.value=vehicleRespnse.data.parkingDurations
                        }
                        if (vehicleRespnse.data.parkingZones.isNotEmpty()){
                            _parkingZone.value=vehicleRespnse.data.parkingZones
                        }
                    }else if (vehicleRespnse.status==0){
                        _statusCodeParking.value=vehicleRespnse.status

                }
            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    fun saveParkingZone(parkingZone: ParkingZone )=viewModelScope.launch(Dispatchers.IO) {
        parkingZoneRepository.upsert(parkingZone)
    }

    fun stopObserving(){
        _statusCode.value=null
        _statusCodeParking.value=null
        _status.value=null
        _isObserving.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}