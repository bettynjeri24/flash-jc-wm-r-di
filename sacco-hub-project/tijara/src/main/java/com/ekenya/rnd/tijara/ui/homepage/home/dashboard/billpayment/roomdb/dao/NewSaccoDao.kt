package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.ekenya.rnd.tijara.network.model.local.County
import com.ekenya.rnd.tijara.network.model.local.NewSaccoDataEntity
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity

@Dao
interface NewSaccoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertSacco(county:List<NewSaccoDataEntity>)

    @Query("SELECT * FROM new_sacco_data")
    fun getAllSaccoDetails(): LiveData<List<NewSaccoDataEntity>>
    @Query("SELECT * FROM new_sacco_data")
    fun getSaccoDetails():List<NewSaccoDataEntity>

    @Query("DELETE FROM new_sacco_data")
     fun deleteAllSaccoDetails()

}