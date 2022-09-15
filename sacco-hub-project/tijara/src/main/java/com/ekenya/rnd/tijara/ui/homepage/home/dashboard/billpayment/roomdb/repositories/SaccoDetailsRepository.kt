package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ParkingZone
import com.ekenya.rnd.tijara.network.model.local.County
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.CountyDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.ParkingZoneDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.SaccoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaccoDetailsRepository(val saccoDao: SaccoDao) {

    /**fun to get all saved items in our db*/
    val allSaccoDetails:LiveData<List<SaccoDetailEntity>> = saccoDao.getAllSaccoDetails()
/**fun from DAO for saving zone*/
  fun insertSacco(saccos: List<SaccoDetailEntity>) {
    GlobalScope.launch(Dispatchers.IO) {
        saccoDao.insertSacco(saccos)
    }
    }
     fun deleteSaccos(){
         GlobalScope.launch(Dispatchers.IO) {
             saccoDao.deleteAllSaccoDetails()
         }
    }
    fun getSaccoDetails():List<SaccoDetailEntity>{
      return  saccoDao.getSaccoDetails()

    }
}