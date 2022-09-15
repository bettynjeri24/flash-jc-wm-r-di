package com.ekenya.rnd.dashboard.database

import com.ekenya.rnd.dashboard.datadashboard.model.AccountBalance
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsAccount
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData


interface DatabaseHelper {

    suspend fun getUsers(): UserData
    suspend fun getWalletBalance(): AccountBalance
    suspend fun getSavingsAccount(): List<SavingsAccount>
    suspend fun insert(users: UserData)
    suspend fun insertWalletBalance(balance: AccountBalance)
    suspend fun insertSavingsAccount(savingsAccount: SavingsAccount)

}