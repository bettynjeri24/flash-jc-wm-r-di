package com.ekenya.rnd.dashboard.database

import com.ekenya.rnd.dashboard.datadashboard.model.AccountBalance
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsAccount
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData


class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun getUsers(): UserData = appDatabase.databaseDao().getAll()
    override suspend fun getWalletBalance(): AccountBalance = appDatabase.databaseDao().getWalletBalance()
    override suspend fun getSavingsAccount(): List<SavingsAccount> = appDatabase.databaseDao().getSavingsAccounts()


    override suspend fun insert(users: UserData) = appDatabase.databaseDao().insertAll(users)
    override suspend fun insertWalletBalance(balance: AccountBalance) = appDatabase.databaseDao().insertWalletBalance(balance)
    override suspend fun insertSavingsAccount(savingsAccount: SavingsAccount) = appDatabase.databaseDao().insertSavingsAccount(savingsAccount)


}