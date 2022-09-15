package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.EmpTermList
import com.ekenya.rnd.tijara.network.model.EmployerList
import com.ekenya.rnd.tijara.requestDTO.WorkDTO

import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class WorkDetailsViewModel @Inject constructor(application: Application):AndroidViewModel(application) {
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
    private var _employerListProperties = MutableLiveData<List<EmployerList>>()
    val employerListProperties: LiveData<List<EmployerList>?>
        get() = _employerListProperties
    private var _empTermListProperties = MutableLiveData<List<EmpTermList>>()
    val empTermListProperties: LiveData<List<EmpTermList>?>
        get() = _empTermListProperties
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    init {
        _statusCode.value=null
        getEmployer()
        getEmpTerms()

    }
    fun getWorkInfo(workDTO: WorkDTO){
        uiScope.launch {
            val getWorkProperties= SaccoApi.retrofitService.workDetails(workDTO)
            try {
                val workResponse=getWorkProperties.await()
                if (workResponse.toString().isNotEmpty()){
                    Timber.d("WORK RESPONSE $workResponse")
                    if (workResponse.status==1){
                        _statusCode.value=workResponse.status
                        _responseStatus.value= GeneralResponseStatus.DONE
                    }else if (workResponse.status==0){
                        _statusCode.value=workResponse.status
                        _statusMessage.value=workResponse.message
                    }
                }
            }catch (e:HttpException){
                _responseStatus.value= GeneralResponseStatus.ERROR
                if (e.code()==500){
                    _statusCode.value=e.code()
                }
            }
        }

    }
    private fun getEmployer(){
        uiScope.launch {
            val companiesEmployer = SaccoApi.retrofitService.getEmployer()

            try {
                val employerResult=companiesEmployer.await()
                if (employerResult.toString().isNotEmpty()){
                    Timber.d("EMPLOYER RESULTS $employerResult")
                    if(employerResult.status==1){
                        /**for the first item in the list*/
                        val first= EmployerList("0","email@gmail.com",0,1,"Select Company",0)
                        val firstItem=ArrayList<EmployerList>()
                        firstItem.add(first)
                        firstItem.addAll(employerResult.data)
                        _employerListProperties.value=firstItem


                    }else{

                    }
                }
            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR EMPLOYER ${e.message}")
            }
        }

    }
    private fun getEmpTerms(){
        uiScope.launch {
            val empTermProperties= SaccoApi.retrofitService.getEmpTerm()
            try {
                val termResult=empTermProperties.await()
                if (termResult.toString().isNotEmpty()){
                    Timber.d("EMP TERM RESULTS $termResult")
                    if(termResult.status==1){
                        /**for the first item in the list*/
                        val first= EmpTermList("0",0,0,"Select employment term",0)
                        val firstItem=ArrayList<EmpTermList>()
                        firstItem.add(first)
                        firstItem.addAll(termResult.data)
                        _empTermListProperties.value=firstItem


                    }else{

                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR EMP TERM ${e.message}")
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