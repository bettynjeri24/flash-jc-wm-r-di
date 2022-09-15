package com.ekenya.rnd.cargillfarmer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.cargillfarmer.data.db.FarmerAppDatabase
import com.ekenya.rnd.cargillfarmer.data.network.CargillFarmerApiService
import com.ekenya.rnd.cargillfarmer.data.responses.AddBeneficiaryAccountResponse
import com.ekenya.rnd.cargillfarmer.data.responses.FarmerLatestTransactionData
import com.ekenya.rnd.cargillfarmer.data.responses.MyCashOutChannelsData
import com.ekenya.rnd.cargillfarmer.data.responses.RemoveAccountResponse
import com.ekenya.rnd.common.data.db.CommonAppDatabase
import com.ekenya.rnd.common.data.db.CommonCargillDataPreferences
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.data.repository.BaseRepository
import com.ekenya.rnd.common.utils.custom.Coroutines
import com.ekenya.rnd.common.utils.custom.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

class FarmerRepository @Inject constructor(
    private val api: CargillFarmerApiService,
    private val appDatabase: FarmerAppDatabase,
    private val commonAppDatabase: CommonAppDatabase,
    private val cargillDataPreferences: CommonCargillDataPreferences
) : BaseRepository {

    suspend fun balanceInquiry(
        requestBody: RequestBody
    ) = apiRequestByResponse {
        api.balanceInquiry(requestBody = requestBody)
    }

    // ***************************************************************************************
    suspend fun latestTransactions(
        requestBody: RequestBody
    ) = apiRequestByResponse {
        api.latestTransactions(requestBody = requestBody)
    }

    // ***************************************************************************************
    suspend fun myCashOutChannels(
        requestBody: RequestBody
    ) = apiRequestByResponse {
        api.myCashOutChannels(requestBody = requestBody)
    }

    // *****************************AddBeneficiaryAccountResponse**********************************************************
    suspend fun addBeneficiaryAccount(
        requestBody: RequestBody
    ): AddBeneficiaryAccountResponse {
        return apiRequestByResponse {
            api.addBeneficiaryAccount(requestBody = requestBody)
        }
    }

    // ***************************************************************************************
    suspend fun farmercashout(
        requestBody: RequestBody
    ) = apiRequestByResponse {
        api.farmercashout(requestBody = requestBody)
    }

    // ***************************************************************************************
    suspend fun saveInRoom(user: List<FarmerLatestTransactionData>) =
        withContext(Dispatchers.IO) { appDatabase.farmerTransactionsDao().insertAll(user) }

    // ***************************************************************************************
    fun getRoomData(): Flow<List<FarmerLatestTransactionData>> {
        return appDatabase.farmerTransactionsDao().getAll()
    }
    // ************************************CUSTOM REPOSITORY***************************************************

    /**
     * CUSTOM REPOSITORY
     */

    private val myCashOutChannelsData = MutableLiveData<List<MyCashOutChannelsData>>()

    // Initiate data to be observe forever
    init {
        myCashOutChannelsData.observeForever {
            saveMyCashOutChannelsDataToRoom(it)
        }
    }

    // Save data and time stamp in room database and Preferences

    private fun saveMyCashOutChannelsDataToRoom(list: List<MyCashOutChannelsData>) {
        Coroutines.io {
            cargillDataPreferences.SAVETIMESTAMP_CHECK_IN(LocalDateTime.now().toString())
            appDatabase.myCashOutChannelsDataDao().insertAll(list)
        }
    }

    // Fetching data from server
    private suspend fun fetchCashOutChannelsData(requestBody: RequestBody) {
        val getLastSavedAt = cargillDataPreferences.FETCHTIMESTAMP_CHECK_IN()
        Timber.d("GETLASTSAVEDATA G $getLastSavedAt")
        if (getLastSavedAt == null || isFetchNeeded(LocalDateTime.parse(getLastSavedAt))) {
            appDatabase.myCashOutChannelsDataDao().deleteAllItems()
            val response = apiRequestByResponse { api.myCashOutChannels2(requestBody) }
            if (response.statusCode == 0) {
                myCashOutChannelsData.postValue(response.myCashOutChannelsData!!)
                Timber.d("GETLASTSAVEDATA 2 $getLastSavedAt")
            } else {
                throw ApiExceptions("Please try again")
            }
        } else {
            Timber.d("GETLASTSAVEDATA 3 $getLastSavedAt")
        }
    }

    suspend fun getDataFromRoom(requestBody: RequestBody): LiveData<List<MyCashOutChannelsData>> {
        return withContext(Dispatchers.IO) {
            fetchCashOutChannelsData(requestBody)
            appDatabase.myCashOutChannelsDataDao().getAll()
        }
    }

    /****************************************************************************************/

    /**
     * REMOVE ACCOUNT
     */
    suspend fun removeBeneficiary(requestBody: RequestBody): RemoveAccountResponse {
        return apiRequestByResponse { api.removeBeneficiary(requestBody = requestBody) }
    }

    /****************************************************************************************/

    /**
     * REMOVE ACCOUNT
     */
    suspend fun verifyBeneficiaryAccount(
        requestBody: RequestBody
    ) = apiRequestByResponse {
        api.verifyBeneficiaryAccount(requestBody = requestBody)
    }

    /**
     * GET USER IN ROOM
     **/
    fun getUserInRoom(): Flow<CargillUserLoginResponseData> {
        return commonAppDatabase.userDataDao().getUser()
    }
}
