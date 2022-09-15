package io.eclectics.cargilldigital.data.db.dao

import androidx.room.*
import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmForceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(user: FarmForceData)

    @Query("SELECT * FROM farmforcedata ORDER BY purchaseId DESC")
    fun getAllFarmForceDataWithFlow(): Flow<List<FarmForceData>>

    @Query("SELECT * FROM farmforcedata ORDER BY purchaseId DESC")
    fun getAllFarmForceData(): List<FarmForceData>


    @Delete
    fun delete(user: FarmForceData)
}