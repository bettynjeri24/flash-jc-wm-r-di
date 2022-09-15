package com.ekenya.rnd.authcargill.data.repository


import androidx.annotation.WorkerThread
import com.ekenya.rnd.authcargill.data.network.AuthApiClientService
import com.ekenya.rnd.authcargill.data.responses.AccountIdLookupMobileResponse
import com.ekenya.rnd.authcargill.data.responses.SetPinResponse
import com.ekenya.rnd.common.data.db.CommonAppDatabase
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApiClientService,
    private val appDatabase:CommonAppDatabase
) : BaseRepository {

    suspend fun loginUser(
        requestBody: RequestBody,
    ) = apiRequestByResource {
        api.login(requestBody = requestBody)
    }

// USED FOR MOBILE LOOK UP


    suspend fun mobileNumberLookUp(requestBody: RequestBody) =
        apiRequestByResource {
            api.mobileLookUp(requestBody)
        }

// SAVE USER IN ROOM


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveUserInRoom(user: CargillUserLoginResponseData) =
        withContext(Dispatchers.IO) { appDatabase.userDataDao().insertData(user) }

// GET USER IN ROOM


    fun getUserInRoom(): Flow<CargillUserLoginResponseData> {
        return appDatabase.userDataDao().getUser()
    }

// SET COOPARATIVE ID


    suspend fun cooparativeIdLookUp(requestBody: RequestBody): AccountIdLookupMobileResponse {
        return apiRequestByResponse { api.cooparativeIdLookUp(requestBody) }
    }

    suspend fun cooparativeIdLookUp2(requestBody: RequestBody) =
        apiRequestByResource {
            api.cooparativeIdLookUp2(requestBody)
        }

// SET NEW ACCOUNT PIN FOR FIRST ID


    suspend fun setNewAccountPin(requestBody: RequestBody): SetPinResponse {
        return apiRequestByResponse { api.setNewAccountPin(requestBody) }
    }

// SET NEW ACCOUNT PIN FOR FIRST ID


    suspend fun verifyOtpForAccountSetUp(requestBody: RequestBody): AccountIdLookupMobileResponse {
        return apiRequestByResponse { api.verifyOtpForAccountSetUp(requestBody) }
    }


// DELETE USER FROM LOCAL DATABASE ie ROOM-DB

    //fun deleteUserInRoom(user: UserData) = appDatabase.userDataDao().delete(user)

/*    fun deleteOtherDatabaseDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.checkInVisitorDataDao().deleteAllCheckedInVisitor()
            appDatabase.checkOutVisitorDataDao().deleteAllCheckedOutVisitor()
            appDatabase.officeDetailsDao().deleteAllfficeDetails()
            appDatabase.notificationDao().deleteAllNotification()
        }

    }*/



}
