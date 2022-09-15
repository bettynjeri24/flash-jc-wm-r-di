
package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ekenya.rnd.tijara.network.model.ParkingZone
import com.ekenya.rnd.tijara.network.model.local.County
import com.ekenya.rnd.tijara.network.model.local.NewSaccoDataEntity
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.CountyDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.NewSaccoDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.ParkingZoneDao
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.dao.SaccoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [ParkingZone::class,County::class, SaccoDetailEntity::class,NewSaccoDataEntity::class], version = 2,exportSchema = false)
abstract class TijaraRoomDatabase : RoomDatabase() {

    abstract fun getParkingZoneDao(): ParkingZoneDao
    abstract fun getallCountyDao(): CountyDao
    abstract fun getAllSaccoDao(): SaccoDao
    abstract fun getNewSaccoDao(): NewSaccoDao
    companion object {
        @Volatile
        private var INSTANCE: TijaraRoomDatabase? = null

        fun getDatabase(
            context: Context
        ): TijaraRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TijaraRoomDatabase::class.java,
                    "parking_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class NotesDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.getParkingZoneDao())

                    }
                }
            }
        }
        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        fun populateDatabase(parkingZoneDao: ParkingZoneDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
//            noteDao.deleteAll()
//            var word = Notes("Sermon by Rev Rubiri","Description",System.currentTimeMillis())
//            noteDao.insert(word)
//            var word = Notes("DESIRE (Luke 4:30)","For everything that you desire god",System.currentTimeMillis())
//            noteDao.insert(word)
        }
    }

}
