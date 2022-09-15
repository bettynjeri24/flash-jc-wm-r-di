package io.eclectics.cargilldigital.data.repository

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import io.eclectics.cargilldigital.data.model.CoopBuyer
import io.eclectics.cargilldigital.data.db.CargillDao
import io.eclectics.cargilldigital.data.db.CargillDatabase
import io.eclectics.cargill.network.Webservice
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType

interface CoopRoomInterface {
    suspend fun saveBuyersList(activity:FragmentActivity,json: String)
    suspend fun getBuyersList(activity: FragmentActivity): LiveData<List<CoopBuyer.BuyerList>>
}
class CooperativeRoomRepository (private val service: Webservice, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : CoopRoomInterface {
    val mtype = "application/json; charset=utf-8".toMediaType()
    lateinit var cargillDao: CargillDao

    override suspend fun getBuyersList(activity: FragmentActivity): LiveData<List<CoopBuyer.BuyerList>> {
        var db = CargillDatabase.getInstance(activity)
        cargillDao = db.cargillDao
        return cargillDao.getCoopBuyerList()
    }

    override suspend fun saveBuyersList(activity: FragmentActivity, json: String) {
        try {
            var db = CargillDatabase.getInstance(activity)
            cargillDao = db.cargillDao
            var userModel: List<CoopBuyer.BuyerList> = NetworkUtility.jsonResponse(json)
            // cargillDao.deleteUserData()
            cargillDao.insertCoopBuyerList(userModel)
        }catch (ex:Exception){
            LoggerHelper.loggerError("errordbsave","error saving buyer list login data ${ex.message}")
        }
    }
}