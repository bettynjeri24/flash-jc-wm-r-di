package com.ekenya.rnd.dashboard.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.model.AccountBalance
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsAccount
import com.ekenya.rnd.dashboard.database.DatabaseHelper
import com.ekenya.rnd.dashboard.utils.getReleaseMode
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import kotlinx.coroutines.launch


class RoomDBViewModel(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) :
    ViewModel() {

    private val users = MutableLiveData<Resource<UserData>>()
    private val accountBalance = MutableLiveData<Resource<AccountBalance>>()
    private val savingsAccounts = MutableLiveData<Resource<List<SavingsAccount>>>()

    init {
        fetchUsers()
        fetchWalletBalance()
        fetchSavingsAccounts()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            users.postValue(Resource.loading(null))
            /* try {
                 val usersFromDb = dbHelper.getUsers()
                 if (usersFromDb.isEmpty()) {
                     val usersFromApi = apiHelper.getUsers()
                     val usersToInsertInDB = mutableListOf<User>()

                     for (apiUser in usersFromApi) {
                         val user = User(
                             apiUser.id,
                             apiUser.name,
                             apiUser.email,
                             apiUser.avatar
                         )
                         usersToInsertInDB.add(user)
                     }

                     dbHelper.insertAll(usersToInsertInDB)

                     users.postValue(Resource.success(usersToInsertInDB))

                 } else {
                     users.postValue(Resource.success(usersFromDb))
                 }


             } catch (e: Exception) {
                 users.postValue(Resource.error("Something Went Wrong", null))
             }*/

            val usersFromDb = dbHelper.getUsers()

            users.postValue(Resource.success(usersFromDb))

        }
    }

    private fun fetchSavingsAccounts() {
        viewModelScope.launch {
            savingsAccounts.postValue(Resource.loading(null))
            try {
                val savingsAccountsFromDb = dbHelper.getSavingsAccount()

                savingsAccounts.postValue(Resource.success(savingsAccountsFromDb))


            } catch (e: Exception) {
                savingsAccounts.postValue(Resource.error(null, "Something Went Wrong"))
            }
        }
    }

    private fun fetchWalletBalance() {
        viewModelScope.launch {
            accountBalance.postValue(Resource.loading(null))
            try {
                val walletBalanceFromDb = dbHelper.getWalletBalance()

                accountBalance.postValue(Resource.success(walletBalanceFromDb))


            } catch (e: Exception) {
                accountBalance.postValue(Resource.error(null, "Something Went Wrong"))
            }
        }
    }

    fun getUsers(): LiveData<Resource<UserData>> {
        return users
    }

    fun getWalletBalance(): LiveData<Resource<AccountBalance>> {
        return accountBalance
    }

    fun getSavingsAccounts(): LiveData<Resource<List<SavingsAccount>>> {
        return savingsAccounts
    }

    fun insertUser() {
        viewModelScope.launch {
            dbHelper.insert(
                UserData(
                    "afafd",
                    "afafd",
                    "afafd",
                    "afafd",
                    "afafd",
                    "33689154",
                    "afafd",
                    "afafd",
                version = getReleaseMode())
            )
        }
    }

    fun insertSavingsAccount(savingsAccount: SavingsAccount) {
        viewModelScope.launch {
            dbHelper.insertSavingsAccount(savingsAccount)


        }
    }

    fun insertWalletBalance(balance: AccountBalance) {
        viewModelScope.launch {
            if (!balance.account_name.isNullOrBlank()) {
                dbHelper.insertWalletBalance(balance)


            }
        }
    }

}