package io.eclectics.cargilldigital.utils.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import io.eclectics.cargilldigital.data.network.ApiClientService
import io.eclectics.cargilldigital.data.repository.ffrepos.FarmForceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
    private val apiClientService: ApiClientService,
    private val repository: FarmForceRepository
) : CoroutineWorker(
    ctx,
    params
) {


    override suspend fun doWork(): Result {
        val applicationContext: Context = applicationContext
        //simulate slow work
        Log.i("TAG", "Fetching Data from Remote host")
        // WorkerUtils.sleep()
        return try {

            val data = withContext(Dispatchers.IO) {
                return@withContext repository.getAllFarmForceData()
            }
            uploadDataToServer(data)
            Timber.e("=============2============ ${SimpleDateFormat("hh:mm:ss").format(Date())}==== ")

            WorkerUtils.makeStatusNotification(
                applicationContext.getString(R.string.new_data_available),
                applicationContext
            )

            Result.success()
            //create a call to network
            /* if (sendAPI2().isSuccessful &&
                 sendAPI2().body != null
             ) {
                 // db.farmForceDao().delete()
                 Log.i(""TAG"", "data fetched from network successfully")
                 WorkerUtils.makeStatusNotification(
                     applicationContext.getString(R.string.new_data_available),
                     applicationContext
                 )
                 Result.success()

             } else {
                 Result.retry()
             }*/
        } catch (e: Throwable) {
            e.printStackTrace()
            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e("TAG", "Error fetching data", e)
            Result.failure()
        }
    }

    private fun uploadDataToServer(data: List<FarmForceData>) {
        data.forEach {
            sendAPIRequest(it)
            Timber.e("FARM  =====${it.farmerPhonenumber}")
            Timber.e("FARMERPHONENUMBER  =====${it.farmerPhonenumber}")
            Timber.e("PURCHASEID  =====${it.purchaseId}")
        }
    }

    private fun sendAPIRequest(farmForceData: FarmForceData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiClientService.paymentTransactions(
                    farmForceData
                )
                if (response.status == 0) {
                    repository.deleteFromRoobDb(farmForceData)
                    Timber.e("SUCCESSFULLY SENT DATA USING WORK MANAGER ${response.toString()}")
                } else {
                    Timber.e("SUCCESSFULLY SENT DATA USING WORK MANAGER ${response.toString()}")
                }
            } catch (e: Exception) {
                Timber.e("FAILED TO SEND DATA USING WORK MANAGER ${e.message}")
            }
        }
    }
}


/*

"{\r\n  " +
                    "\"purchaseId\": \"12345\",\r\n  " +
                    "\"paymentType\": 1,\r\n  " +
                    "\"amount\": \"23000\",\r\n  " +
                    "\"buyerPhonenumber\": \"2250703035850\",\r\n  " +
                    "\"farmerPhonenumber\": \"2250703035850\",\r\n  " +
                    "\"paymentKey\": \"2250703035850QERT\",\r\n  " +
                    "\"txnDate\": \"0001-01-01T00:00:00\",\r\n " +
                    " \"comments\": \"pay\"\r\n" +
                    "}"
 */