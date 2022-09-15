package com.ekenya.rnd.cargillfarmer.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ekenya.rnd.cargillfarmer.data.responses.MyCashOutChannelsData

@Dao
interface MyCashOutChannelsDataDao {
    @Query("SELECT * FROM mycashoutchannelsdata ORDER BY id DESC")
    fun getAll(): LiveData<List<MyCashOutChannelsData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(list: List<MyCashOutChannelsData>)

    @Delete
    fun delete(user: MyCashOutChannelsData)

    @Query("DELETE FROM mycashoutchannelsdata")
    fun deleteAllItems()

    @Query("DELETE FROM mycashoutchannelsdata WHERE id = :id")
    fun deleteById(id: Int)

}