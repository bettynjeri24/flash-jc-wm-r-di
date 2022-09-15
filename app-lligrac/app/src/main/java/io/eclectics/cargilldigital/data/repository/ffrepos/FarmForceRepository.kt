package io.eclectics.cargilldigital.data.repository.ffrepos


import io.eclectics.cargilldigital.data.db.CargillDatabase
import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import io.eclectics.cargilldigital.data.network.ApiClientService
import io.eclectics.cargilldigital.data.repository.baserepo.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject

class FarmForceRepository @Inject constructor(
    private val api: ApiClientService,
    private val db: CargillDatabase
) : BaseRepository {

    suspend fun loginUser(
        requestBody: RequestBody,
    ) = apiRequestByResource {
        api.login(requestBody = requestBody)
    }

    suspend fun paymentTransactions(requestBody: RequestBody) =
        apiRequestByResource {
            api.paymentTransactions(requestBody)
        }

    // SAVE USER IN ROOM
    suspend fun saveUserInRoom(user: FarmForceData) =
        withContext(Dispatchers.IO) { db.farmForceDao().insertData(user) }
// GET USER IN ROOM


    fun getAllFarmForceDataWithFlow(): Flow<List<FarmForceData>> {
        return db.farmForceDao().getAllFarmForceDataWithFlow()
    }

    fun getAllFarmForceData(): List<FarmForceData> {
        return db.farmForceDao().getAllFarmForceData()
    }

    fun deleteFromRoobDb(farmForceData: FarmForceData) {
        CoroutineScope(Dispatchers.IO).launch {
            db.farmForceDao().delete(farmForceData)
        }
    }

}
