package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ekenya.rnd.tijara.network.model.ParkingZone
import com.ekenya.rnd.tijara.network.model.local.County

@Dao
interface CountyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun upsertCounty(county:List<County>)

    @Query("SELECT * FROM county_table")
    fun getAllCounty(): LiveData<List<County>>

}