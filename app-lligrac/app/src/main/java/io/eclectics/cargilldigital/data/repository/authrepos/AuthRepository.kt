package io.eclectics.cargilldigital.data.repository.authrepos


import androidx.annotation.WorkerThread
import io.eclectics.cargilldigital.data.db.CargillDatabase
import io.eclectics.cargilldigital.data.network.ApiClientService
import io.eclectics.cargilldigital.data.repository.baserepo.BaseRepository
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserLoginResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiClientService,
    private val appDatabase: CargillDatabase
) : BaseRepository {

    suspend fun loginUser(
        requestBody: RequestBody,
    ) = apiRequestByResource {
        api.login(requestBody = requestBody)
    }
// USED FOR MOBILE LOOK UP

    suspend fun cooparativeIdLookUp(requestBody: RequestBody) =
        apiRequestByResource {
            api.cooparativeIdLookUp(requestBody)
        }



    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun saveUserInRoom(user: CargillUserLoginResponseData) =
        withContext(Dispatchers.IO) { appDatabase.userDataDao().insertData(user) }

// GET USER IN ROOM


    fun getUserInRoom(): Flow<CargillUserLoginResponseData> {
        return appDatabase.userDataDao().getUser()
    }
}
