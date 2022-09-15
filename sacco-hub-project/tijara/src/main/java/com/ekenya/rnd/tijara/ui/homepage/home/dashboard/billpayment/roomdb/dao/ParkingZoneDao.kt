package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ekenya.rnd.tijara.network.model.ParkingZone

@Dao
interface ParkingZoneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(parkingZone:ParkingZone)

    @Query("SELECT * FROM zone_table")
    fun getAllZone(): LiveData<List<ParkingZone>>

}