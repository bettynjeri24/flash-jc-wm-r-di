package com.ekenya.rnd.common.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ekenya.rnd.common.data.db.dao.CargillUserDao
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData


@Database(
    entities = [
        CargillUserLoginResponseData::class,
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(CommonCustomTypeConverter::class)
abstract class CommonAppDatabase : RoomDatabase() {

    abstract fun userDataDao(): CargillUserDao

    companion object {
        @Volatile
        private var INSTANCE: CommonAppDatabase? = null

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(Any()) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CommonAppDatabase::class.java,
            "cargill-database-user.db"
        )
            .build()

    }
}
