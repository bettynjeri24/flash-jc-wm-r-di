package io.eclectics.cargilldigital.data.repository

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import io.eclectics.cargilldigital.data.model.BuyerPendingPayment
import io.eclectics.cargilldigital.data.model.CoopFundsRequestList
import io.eclectics.cargilldigital.data.model.UserLogginData
import io.eclectics.cargilldigital.data.db.CargillDao
import io.eclectics.cargilldigital.data.db.CargillDatabase
import io.eclectics.cargill.model.FarmerModelObj
import io.eclectics.cargill.network.Webservice
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType

interface RoomInterface {
    //Save cooler issue offline
    //suspend fun saveOffCoolerIssue(activity: FragmentActivity, json: String)
    //Login management
   // suspend fun loginRequest(activity:FragmentActivity,json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun saveLoginData(activity:FragmentActivity,json: String)
    suspend fun saveFarmerList(activity:FragmentActivity,json: String)
    suspend fun getFarmerList(activity:FragmentActivity):LiveData<List<FarmerModelObj>>
    suspend fun getFundsRequestList(activity: FragmentActivity):LiveData<List<CoopFundsRequestList>>
    suspend fun saveFundsRequestList(activity: FragmentActivity,json: String)
    suspend fun getPendingPaymentRequest(activity: FragmentActivity):LiveData<List<BuyerPendingPayment>>
    suspend fun savePendingBuyerRequestList(activity: FragmentActivity,json: String)
}
class BuyerRoomRepository (private val service: Webservice, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : RoomInterface {
    val mtype = "application/json; charset=utf-8".toMediaType()
    lateinit var cargillDao: CargillDao

    override suspend fun getPendingPaymentRequest(activity: FragmentActivity): LiveData<List<BuyerPendingPayment>> {
        var db = CargillDatabase.getInstance(activity)
        cargillDao = db.cargillDao
        return cargillDao.getAllPendingPayments()
    }

    override suspend fun savePendingBuyerRequestList(activity: FragmentActivity, json: String) {
        try {
            var db = CargillDatabase.getInstance(activity)
             cargillDao = db.cargillDao
            var userModel: List<BuyerPendingPayment> = NetworkUtility.jsonResponse(json.toString())
            cargillDao.deleteAllPendingPayment()
            cargillDao.insertPendingPayments(userModel)
        }catch (ex:Exception){}
    }

    override suspend fun saveFarmerList(activity: FragmentActivity, json: String) {
        try {
            var db = CargillDatabase.getInstance(activity)
            cargillDao = db.cargillDao
            var userModel: List<FarmerModelObj> = NetworkUtility.jsonResponse(json.toString())
             cargillDao.deleteFarmerList()
            cargillDao.insertFarmerList(userModel)
        }catch (ex:Exception){}
    }
    override suspend fun getFarmerList(activity: FragmentActivity): LiveData<List<FarmerModelObj>> {
        var db = CargillDatabase.getInstance(activity)
        cargillDao = db.cargillDao
        return cargillDao.getFarmersList()
    }

    override suspend fun saveLoginData(activity: FragmentActivity, json: String) {
        try {
            var db = CargillDatabase.getInstance(activity)
            cargillDao = db.cargillDao
            var userModel: UserLogginData = NetworkUtility.jsonResponse(json.toString())
           // cargillDao.deleteUserData()
            cargillDao.insertUserdata(userModel)
        }catch (ex:Exception){
            LoggerHelper.loggerError("errordbsave","error saving user login data")
        }
    }
    /**
     * Buyer funds request
     */
    override suspend fun getFundsRequestList(activity: FragmentActivity): LiveData<List<CoopFundsRequestList>> {
        var db = CargillDatabase.getInstance(activity)
        cargillDao = db.cargillDao
        return cargillDao.getBuyerFundsReqList()
    }

    override suspend fun saveFundsRequestList(activity: FragmentActivity, json: String) {
        try {
            var db = CargillDatabase.getInstance(activity)
            cargillDao = db.cargillDao
            var fundRequestList: List<CoopFundsRequestList> = NetworkUtility.jsonResponse(json.toString())
             cargillDao.deleteFundsReqList()
            cargillDao.insertBuyerFundsReqList(fundRequestList)
        }catch (ex:Exception){
            LoggerHelper.loggerError("errordbsave","error saving user funds request data")
        }
    }

  /*  override suspend fun saveOffCoolerIssue(activity: FragmentActivity, json: String) {
        var outletList: OfflineCoolerStatus = NetworkUtility.jsonResponse(json.toString())
        var db = CargillDatabase.getInstance(activity)
        cargillDao = db.cargillDao
        cargillDao.insertCoolerIssue(outletList)
    }*/
}