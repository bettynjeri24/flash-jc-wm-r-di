package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ParkingZone
import com.ekenya.rnd.tijara.network.model.local.County
import com.ekenya.rnd.tijara.network.model.local.NewSaccoDataEntity
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.CountyDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.NewSaccoDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.ParkingZoneDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.SaccoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewSaccoRepository(val newSaccoDao: NewSaccoDao) {

    /**fun to get all saved items in our db*/
    val allNewSaccoDetails:LiveData<List<NewSaccoDataEntity>> = newSaccoDao.getAllSaccoDetails()
/**fun from DAO for saving zone*/

     fun insertNewSacco(saccos: List<NewSaccoDataEntity>) {
    GlobalScope.launch(Dispatchers.IO) {
        newSaccoDao.insertSacco(saccos)
    }
}

}