package com.ekenya.rnd.dashboard.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.database.DatabaseHelper
import com.ekenya.rnd.dashboard.repositories.MainRepository
import com.ekenya.rnd.dashboard.viewmodels.*


class ViewModelFactory(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        /* if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
             return LoginViewModel(MainRepository(),apiHelper) as T
         }
         else*/
        return if (modelClass.isAssignableFrom(MobileWalletViewModel::class.java)) {
            MobileWalletViewModel(MainRepository(), apiHelper) as T
        } else if (modelClass.isAssignableFrom(TransactionConfirmationViewModel::class.java)) {
            TransactionConfirmationViewModel(/*MainRepository(),apiHelper*/) as T
        } else if (modelClass.isAssignableFrom(ConfirmSendingMoneyViewModel::class.java)) {
            ConfirmSendingMoneyViewModel(/*MainRepository(),apiHelper*/) as T
        } else if (modelClass.isAssignableFrom(AuthorizeTransactionViewModel::class.java)) {
            AuthorizeTransactionViewModel(/*MainRepository(),apiHelper*/) as T
        } else if (modelClass.isAssignableFrom(RoomDBViewModel::class.java)) {
            RoomDBViewModel(apiHelper, dbHelper) as T
        } else if (modelClass.isAssignableFrom(SavingsViewModel::class.java)) {
            SavingsViewModel() as T
        } else
            throw IllegalArgumentException("Unknown class name")
    }

}