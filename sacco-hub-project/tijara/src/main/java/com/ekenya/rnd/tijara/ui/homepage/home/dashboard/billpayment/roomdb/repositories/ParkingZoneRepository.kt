package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.ekenya.rnd.tijara.network.model.ParkingZone
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.ParkingZoneDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ParkingZoneRepository(private val parkingZoneDao: ParkingZoneDao) {

    /**fun to get all saved items in our db*/
    val allParkingZone:LiveData<List<ParkingZone>> = parkingZoneDao.getAllZone()

/**fun from DAO for saving zone*/

   fun upsert(parkingZone: ParkingZone){
    GlobalScope.launch(Dispatchers.IO) {
        parkingZoneDao.insert(parkingZone)
    }

    }
}