package com.ekenya.rnd.dashboard.datadashboard.dao

import androidx.room.*
import com.ekenya.rnd.dashboard.datadashboard.model.AccountBalance
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsAccount
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
@Dao
interface DatabaseDao {
    @Query("SELECT * FROM userdata")
    suspend fun getAll(): UserData
    @Query("SELECT * FROM accountbalance")
    suspend fun getWalletBalance(): AccountBalance
    @Query("SELECT * FROM savingsaccount")

    suspend fun getSavingsAccounts(): List<SavingsAccount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: UserData)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWalletBalance(balance: AccountBalance)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavingsAccount(savingsAccount: SavingsAccount)

    @Delete
    suspend fun delete(user: UserData)
}