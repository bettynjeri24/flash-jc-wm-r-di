package com.ekenya.rnd.dashboard.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ekenya.rnd.dashboard.datadashboard.dao.DatabaseDao
import com.ekenya.rnd.dashboard.datadashboard.model.AccountBalance
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsAccount
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData

@Database(entities = [UserData::class,SavingsAccount::class,AccountBalance::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

}