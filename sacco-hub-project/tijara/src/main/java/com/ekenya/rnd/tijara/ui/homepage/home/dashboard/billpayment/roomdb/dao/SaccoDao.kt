package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.ekenya.rnd.tijara.network.model.local.County
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity

@Dao
interface SaccoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertSacco(county:List<SaccoDetailEntity>)

    @Query("SELECT * FROM sacco_details")
    fun getAllSaccoDetails(): LiveData<List<SaccoDetailEntity>>
    @Query("SELECT * FROM sacco_details")
    fun getSaccoDetails():List<SaccoDetailEntity>

    @Query("DELETE FROM sacco_details")
     fun deleteAllSaccoDetails()

}