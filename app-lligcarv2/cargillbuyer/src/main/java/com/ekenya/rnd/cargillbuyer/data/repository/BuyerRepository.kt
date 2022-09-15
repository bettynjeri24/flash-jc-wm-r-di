package com.ekenya.rnd.cargillbuyer.data.repository

import androidx.annotation.WorkerThread
import com.ekenya.rnd.cargillbuyer.data.network.CargillBuyerApiService
import com.ekenya.rnd.common.data.db.CommonAppDatabase
import com.ekenya.rnd.common.data.db.CommonCargillDataPreferences
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject


class BuyerRepository @Inject constructor(
    private val api: CargillBuyerApiService,
    //private val appDatabase: FarmerAppDatabase,
    private val commonAppDatabase: CommonAppDatabase,
    private val cargillDataPreferences: CommonCargillDataPreferences
) : BaseRepository {

    /**
     * SAVE USER IN ROOM
     **/
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveUserInRoom(user: CargillUserLoginResponseData) =
        withContext(Dispatchers.IO) { commonAppDatabase.userDataDao().insertData(user) }

    /**
     * GET USER IN ROOM
     **/
    fun getUserInRoom(): Flow<CargillUserLoginResponseData> {
        return commonAppDatabase.userDataDao().getUser()
    }

    /**
     * balanceInquiry
     **/
    suspend fun balanceInquiry(
        requestBody: RequestBody,
    ) = apiRequestByResponse {
        api.balanceInquiry(requestBody = requestBody)
    }

    /**
     * ffPendingPayments
     **/
    suspend fun ffPendingPayments(
        requestBody: RequestBody,
    ) = apiRequestByResponse {
        api.ffPendingPayments(requestBody = requestBody)
    }

    /**
     * ffPendingPayments
     **/
    suspend fun latestTransactions(
        requestBody: RequestBody,
    ) = apiRequestByResponse {
        api.latestTransactions(requestBody = requestBody)
    }

    /**
     * ffPendingPayments
     **/
    suspend fun ffPayments(
        requestBody: RequestBody,
    ) = apiRequestByResponse {
        api.ffPayments(requestBody = requestBody)
    }

    /**
     * ffPendingPayments
     **/
    suspend fun buyertopuprequests(
        requestBody: RequestBody,
    ) = apiRequestByResponse {
        api.buyertopuprequests(requestBody = requestBody)
    }

    /**
     * ffPendingPayments
     **/
    suspend fun getfarmerslist(
        requestBody: RequestBody
    ) = apiRequestByResponse {
        api.getfarmerslist(requestBody = requestBody)
    }
    /**
     * ffPendingPayments
     **/
    suspend fun requestFarmerPurchase(
        requestBody: RequestBody
    ) = apiRequestByResponse {
        api.requestFarmerPurchase(requestBody = requestBody)
    }
    /**
     * ffPendingPayments
     **/
    suspend fun requestFundsTopUp(
        requestBody: RequestBody
    ) = apiRequestByResponse {
        api.requestFundsTopUp(requestBody = requestBody)
    }

}