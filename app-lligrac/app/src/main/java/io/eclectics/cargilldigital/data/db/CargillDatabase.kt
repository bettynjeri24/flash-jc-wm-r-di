package io.eclectics.cargilldigital.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.eclectics.cargilldigital.data.model.*
import io.eclectics.cargill.model.FarmerModelObj
import io.eclectics.cargilldigital.data.db.dao.CargillUserDao
import io.eclectics.cargilldigital.data.db.dao.FarmForceDao
import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserLoginResponseData
import java.util.concurrent.Executors

@Database(
    entities = [UserLogginData::class,
        FarmerModelObj::class,
        FarmerAccount.BeneficiaryAccObj::class,
        SendMoney.ChannelListObj::class,
        CoopBuyer.BuyerList::class,
        BuyerPendingPayment::class,
        CoopFundsRequestList::class,
        FarmForceData::class,
        CargillUserLoginResponseData::class,
    ],//CoolerInfo::class,OfflineCoolerStatus::class
    version = 3,
    exportSchema = false
)
//@TypeConverters(Converters::class)
@TypeConverters(CommonCustomTypeConverter::class)
abstract class CargillDatabase : RoomDatabase() {
    abstract val cargillDao: CargillDao
    abstract fun farmForceDao(): FarmForceDao
    abstract fun userDataDao(): CargillUserDao
    private val NUMBER_OF_THREADS = 4
    val databaseWriteExecutor =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    companion object {
        @Volatile
        private var INSTANCE: CargillDatabase? = null

        fun getInstance(context: Context): CargillDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CargillDatabase::class.java,
                        "cargill_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(Any()) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

//        operator fun invoke(context: Context) = INSTANCE ?: synchronized(Any()) {
//            INSTANCE ?: buildDatabase(context).also {
//                INSTANCE = it
//            }
//        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CargillDatabase::class.java,
                "cargill-database-ff.db"
            ).build()
    }
}

