package com.ekenya.rnd.cargillfarmer.data.db.dao

import androidx.room.*
import com.ekenya.rnd.cargillfarmer.data.responses.FarmerLatestTransactionData
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerTransactionsDao {

    @Query("SELECT * FROM farmerlatesttransactiondata ORDER BY id DESC")
    fun getAll(): Flow<List<FarmerLatestTransactionData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(list: List<FarmerLatestTransactionData>)

    @Delete
    fun delete(user: FarmerLatestTransactionData)

    @Query("DELETE FROM farmerlatesttransactiondata")
    fun deleteItems()

    @Query("DELETE FROM farmerlatesttransactiondata WHERE id = :id")
    fun deleteById(id: Int)

}