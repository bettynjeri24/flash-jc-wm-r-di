package io.eclectics.cargilldigital.data.repository

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import io.eclectics.cargilldigital.data.model.FarmerAccount
import io.eclectics.cargilldigital.data.model.SendMoney
import io.eclectics.cargilldigital.data.db.CargillDao
import io.eclectics.cargilldigital.data.db.CargillDatabase
import io.eclectics.cargill.network.Webservice
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType

interface FarmerRoomInterface {
    //Save cooler issue offline
    //suspend fun saveOffCoolerIssue(activity: FragmentActivity, json: String)
    //Login management
    // suspend fun loginRequest(activity:FragmentActivity,json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun saveChannelList(activity: FragmentActivity, json: String)
    suspend fun getChannelList(activity: FragmentActivity): LiveData<List<SendMoney.ChannelListObj>>
    suspend fun saveBeneficiaryAccList(activity: FragmentActivity, json: String)
    suspend fun getBeneficiaryAccList(activity: FragmentActivity): LiveData<List<FarmerAccount.BeneficiaryAccObj>>
}
class FarmerRoomRepository (private val service: Webservice, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : FarmerRoomInterface {
    val mtype = "application/json; charset=utf-8".toMediaType()
    lateinit var cargillDao: CargillDao

    override suspend fun saveBeneficiaryAccList(activity: FragmentActivity, json: String) {
        try {
            var db = CargillDatabase.getInstance(activity)
            cargillDao = db.cargillDao
            var userModel: List<FarmerAccount.BeneficiaryAccObj> = NetworkUtility.jsonResponse(json.toString())
            cargillDao.deleteAllBeneficiaryAcc()
            LoggerHelper.loggerError("accsaved","account savaed successsfully")
            cargillDao.insertBeneficiaryAcc(userModel)
        } catch (ex: Exception) {
        }
    }

    override suspend fun getBeneficiaryAccList(activity: FragmentActivity): LiveData<List<FarmerAccount.BeneficiaryAccObj>> {
        var db = CargillDatabase.getInstance(activity)
        cargillDao = db.cargillDao
        return cargillDao.getBeneficiaryAccList()
    }

    override suspend fun saveChannelList(activity: FragmentActivity, json: String) {
        try {
            var db = CargillDatabase.getInstance(activity)
            cargillDao = db.cargillDao
            var userModel: List<SendMoney.ChannelListObj> = NetworkUtility.jsonResponse(json.toString())
            cargillDao.deleteChannelList()
            cargillDao.insertChannelList(userModel)
        } catch (ex: Exception) {
        }
    }

    override suspend fun getChannelList(activity: FragmentActivity): LiveData<List<SendMoney.ChannelListObj>> {
        var db = CargillDatabase.getInstance(activity)
        cargillDao = db.cargillDao
        return cargillDao.getChannelList()
    }
}
