package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.BillerCatgData
import com.ekenya.rnd.tijara.network.model.BillerData
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.local.County
import com.ekenya.rnd.tijara.requestDTO.*
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.TijaraRoomDatabase
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories.CountyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class BillersViewModel @Inject constructor(val app: Application): AndroidViewModel(app){
    private lateinit var countyRepository: CountyRepository
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusCMessage = MutableLiveData<String>()
    val statusCMessage: LiveData<String>
        get() = _statusCMessage
    private var _accountCode = MutableLiveData<Int?>()
    val accountCode: MutableLiveData<Int?>
        get() = _accountCode
    private var _loadingAccount = MutableLiveData<Boolean?>()
    val loadingAccount: MutableLiveData<Boolean?>
        get() = _loadingAccount
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _county = MutableLiveData<List<County>>()
    var county: LiveData<List<County>>
        get() = _county
    private var _billList = MutableLiveData<List<BillerData>>()
    val billList: LiveData<List<BillerData>?>
        get() = _billList
    private var _billCatList = MutableLiveData<List<BillerCatgData>>()
    val billCatList: LiveData<List<BillerCatgData>?>
        get() = _billCatList
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>

        get() = _isObserving
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private var _savingPrimeProperties = MutableLiveData<List<SavingAccountData>>()
    val savingPrimeProperties: LiveData<List<SavingAccountData>?>
        get() = _savingPrimeProperties
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var _billerCode= MutableLiveData<String>()
    val billerCode: LiveData<String>
        get() = _billerCode
    private var _billerName= MutableLiveData<String>()
    val billerName: LiveData<String>
        get() = _billerName
    private var _billerUrl= MutableLiveData<String>()
    val billerUrl: LiveData<String>
        get() = _billerUrl
    private var _hasPresentment= MutableLiveData<Int>()
    val hasPresentment: LiveData<Int>
        get() = _hasPresentment
    private var _accountName = MutableLiveData<String>()
    val accountName: LiveData<String>
        get() = _accountName
    private var _accountNumber = MutableLiveData<String>()
    val accountNumber: LiveData<String>
        get() = _accountNumber
    private var _amount= MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount
    private var _countyName= MutableLiveData<String>()
    val countyName: LiveData<String>
        get() = _countyName
    private var _formId = MutableLiveData<Int?>()
    val formId: LiveData<Int?>
        get() = _formId
    private var _refId = MutableLiveData<String>()
    val refId: LiveData<String>
        get() = _refId
    private var _charges = MutableLiveData<String>()
    val charges: LiveData<String>
        get() = _charges
    private var _primeAccountNumber = MutableLiveData<String>()
    val primeAccountNumber: LiveData<String>
        get() = _primeAccountNumber
    private var _primeAccountName = MutableLiveData<String>()
    val primeAccountName: LiveData<String>
        get() = _primeAccountName
    fun setPrimeAccNo(value:String){
        _primeAccountNumber.value=value
    }
    fun setPrimeAccName(value:String){
        _primeAccountName.value=value
    }
    fun setBilerCode(value:String){
        _billerCode.value=value
    }
    fun setBillerName(value:String){
        _billerName.value=value
    }
    fun setBillerUrl(value:String){
        _billerUrl.value=value
    }
    fun setPresentment(value:Int){
        _hasPresentment.value=value
    }
    fun setCountyName(value:String){
        _countyName.value=value
    }
    fun setAccountName(value:String){
        _accountName.value=value
    }
    fun setAccNumber(value:String){
        _accountNumber.value=value
    }
    fun setAmount(value:String){
        _amount.value=value
    }

    init {
        val countyDao = TijaraRoomDatabase.getDatabase(app).getallCountyDao()
        countyRepository= CountyRepository(countyDao)
        county=countyRepository.allcountyDao
        _statusCode.value=null
        _status.value=null
        _loadingAccount.value=null
        _accountCode.value=null
        _isEmpty.value=false
        _isError.value=false
        _isVisible.value=false
        getBillersCategory()
        loadCounties()
        loadPrimeSavingAccount()
    }
    fun stopObserving(){
        _statusCode.value=null
        _status.value=null
        _isObserving.value = false
    }
    private fun getAllBillers(){
        uiScope.launch {
         _responseStatus.value= GeneralResponseStatus.LOADING
            /*val billerDTO= BillerDTO()
            billerDTO.org_id= Constants.ORGID*/
            val billerDetails= SaccoApi.retrofitService.getBillers()
            try {
                val billersResponse=billerDetails.await()
                    _responseStatus.value = GeneralResponseStatus.DONE
                    Timber.d("Biller LISTS RESPONSE $billersResponse")
                    if (billersResponse.data.isNotEmpty()){
                        Log.d("TAG","BILLER SIZE${billersResponse.data.size}")
                        _billList.postValue(billersResponse.data)
                        _isEmpty.value=false
                        _isVisible.value=true

                    }else{
                        _isEmpty.value=true
                        _isVisible.value=false
                    }


            }catch (e: Exception){
                _isError.value=true
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    private fun getBillersCategory(){

        uiScope.launch {
            _isObserving.value = true
            _responseStatus.value= GeneralResponseStatus.LOADING
            val billerCat= SaccoApi.retrofitService.getBillersCategories()
            try {
                val billersCatResponse=billerCat.await()
                    if (billersCatResponse.status==1) {
                        _responseStatus.value = GeneralResponseStatus.DONE
                        getAllBillers()
                        val items= arrayListOf<BillerCatgData>()
                        items.add(BillerCatgData(0,"All"))
                        items.addAll(billersCatResponse.data)
                        _billCatList.postValue(items)
                    }



            }catch (e: Exception){
                _isError.value=true
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun getFilteredBillers( billerDTO:BillerDTO){
        _billList.postValue(arrayListOf())
        if (billerDTO.categoryId == "0"){
            getAllBillers()
        } else{
            uiScope.launch {
                _isObserving.value = true
                _responseStatus.value= GeneralResponseStatus.LOADING
                val billerDetails= SaccoApi.retrofitService.getBillersFiltered(billerDTO)
                try {
                    val billersResponse=billerDetails.await()
                       if (billersResponse.status==1) {
                           _isEmpty.value=false
                           _responseStatus.value = GeneralResponseStatus.DONE
                            if (billersResponse.data.isNotEmpty()) {
                                _isEmpty.value = false
                                _billList.postValue(billersResponse.data)

                            } else {
                                Timber.d("Biller CAT Empty")
                                _isEmpty.value = true
                                _billList.postValue(arrayListOf())
                            }
                        }


                }catch (e: Exception){
                    _isError.value=true
                    _responseStatus.value= GeneralResponseStatus.ERROR
                    Timber.e("ERROR Exception${e.message}")
                }
            }
        }

    }
    //genaral lookup
    fun billerLookUp(billerLookupDTO: BillerLookupDTO){
        uiScope.launch {
            val billerDetails= SaccoApi.retrofitService.billerNoLookup(billerLookupDTO)
            try {
                val billersResponse=billerDetails.await()
                    if (billersResponse.status==1){
                        _accountNumber.value=billersResponse.data.accountNumber
                        _accountName.value=billersResponse.data.accountName
                        _amount.value=billersResponse.data.amount
                        _status.value=billersResponse.status
                        //
                    }else{
                        _statusMessage.value=billersResponse.message
                        _status.value=0
                    }

            }catch (e: Exception){
                _status.value=0
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun countyLookup(countyLookUpDTO: CountyLookUpDTO){
        uiScope.launch {
            val lookupRequest= SaccoApi.retrofitService.countyLookUp(countyLookUpDTO)

            try {
                val lookupResponse=lookupRequest.await()
                    if (lookupResponse.status==1){
                        _accountNumber.value=lookupResponse.data.accountNumber
                        _accountName.value=lookupResponse.data.accountName
                        _amount.value=lookupResponse.data.amount
                        _statusCode.value=lookupResponse.status
                    }else if (lookupResponse.status==0){
                        _statusMessage.value=lookupResponse.message
                        _statusCode.value=lookupResponse.status
                    }

            }catch (e: Exception){
                _statusCode.value=0
            }

        }
    }

    /**get saving account dropdown items*/
    private fun loadCounties(){
        _loadingAccount.value=true
        uiScope.launch {
            val countyRequest= SaccoApi.retrofitService.getCountiesAsync()
            try {
                val countyRespnse=countyRequest.await()
                    if(countyRespnse.status==1){
                        /**for the first item in the list*/
                        _county.value=countyRespnse.data.counties
                        _loadingAccount.value=false
                        _accountCode.value=1
                    }else{
                        _loadingAccount.value=false
                        _accountCode.value=0
                    }

            }catch (e: java.lang.Exception){
                _loadingAccount.value=false
                _accountCode.value=0
            }
        }
    }
    fun saveCounty(county: List<County> )=viewModelScope.launch(Dispatchers.IO) {
        countyRepository.upsert(county)
    }
    /**get saving account dropdown items*/
    private fun loadPrimeSavingAccount(){
        _loadingAccount.value=true
        uiScope.launch {
            val savingAccDTO= SavingAccDTO()
            savingAccDTO.isTransactional=1
            val savingProperties= SaccoApi.retrofitService.getSavingAcc(savingAccDTO)
            Timber.d("Saving Account$savingAccDTO")
            try {
                val savingAccResult=savingProperties.await()
                    Timber.d("SAVING RESULTS $savingAccResult")
                    if(savingAccResult.status==1){
                        /**for the first item in the list*/
                        _savingPrimeProperties.value=savingAccResult.data
                        _loadingAccount.value=false
                        _accountCode.value=1
                    }else {
                        _loadingAccount.value=false
                        _accountCode.value=0
                }
            }catch (e: Exception){
                _loadingAccount.value=false
                _accountCode.value=0
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    fun payBill(payBillDTO: PayBillDTO){
        uiScope.launch {
            val billProperties= SaccoApi.retrofitService.payBill(payBillDTO)
            try {
                val billResponse=billProperties.await()
                    if (billResponse.status==1){
                        _formId.value=billResponse.data.formId
                        _charges.value= billResponse.data.charges.toString()
                        _statusCode.value=1

                    }else if (billResponse.status==0){
                        _statusMessage.value=billResponse.message
                        _statusCode.value=billResponse.status

                }
            }catch (e: HttpException){
                _responseStatus.value= GeneralResponseStatus.ERROR
                if (e.code()==403){
                    _statusCode.value=403
                }else {
                    _statusCode.value = 400
                }
            }

        }
    }
    fun billCommitCommit(){
        uiScope.launch {
            val statementDTO=StatementDTO()
            statementDTO.formId= _formId.value!!
            val airtimeRequest= SaccoApi.retrofitService.payBillCommit(statementDTO)

            try {
                val airtimeCResponse=airtimeRequest.await()

                    if (airtimeCResponse.status==1){
                        _refId.value=airtimeCResponse.data.transactionCode
                        _status.value=1

                    }else if (airtimeCResponse.status==0){
                        _statusCMessage.value=airtimeCResponse.message
                        _status.value=airtimeCResponse.status

                    }

            }catch (e: HttpException){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _status.value=e.code()
            }

        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}