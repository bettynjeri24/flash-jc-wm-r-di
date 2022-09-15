package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ParkingZone
import com.ekenya.rnd.tijara.network.model.local.County
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.CountyDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.ParkingZoneDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CountyRepository(val countyDao: CountyDao) {

    /**fun to get all saved items in our db*/
    val allcountyDao:LiveData<List<County>> = countyDao.getAllCounty()
/**fun from DAO for saving zone*/

     fun upsert(county: List<County>) {
    GlobalScope.launch(Dispatchers.IO) {
        countyDao.upsertCounty(county)
    }
}


}