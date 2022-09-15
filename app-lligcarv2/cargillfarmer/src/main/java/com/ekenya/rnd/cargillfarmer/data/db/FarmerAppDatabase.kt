package com.ekenya.rnd.cargillfarmer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ekenya.rnd.cargillfarmer.data.db.dao.FarmerTransactionsDao
import com.ekenya.rnd.cargillfarmer.data.db.dao.MyCashOutChannelsDataDao
import com.ekenya.rnd.cargillfarmer.data.responses.FarmerLatestTransactionData
import com.ekenya.rnd.cargillfarmer.data.responses.MyCashOutChannelsData

@Database(
    entities = [
        FarmerLatestTransactionData::class,
        MyCashOutChannelsData::class
    ],
    version = 1,
    exportSchema = false
)
// @TypeConverters(CustomTypeConverter::class)
abstract class FarmerAppDatabase : RoomDatabase() {

    abstract fun farmerTransactionsDao(): FarmerTransactionsDao
    abstract fun myCashOutChannelsDataDao(): MyCashOutChannelsDataDao

    companion object {
        @Volatile
        private var INSTANCE: FarmerAppDatabase? = null

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(Any()) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            FarmerAppDatabase::class.java,
            "cargill-database-farmer.db"
        )
            .build()
    }
}
