package com.ekenya.rnd.dashboard.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.database.DatabaseHelper
import com.ekenya.rnd.dashboard.repositories.MainRepository
import com.ekenya.rnd.dashboard.repositories.TestMainViewModel
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel

class ViewModelFactory2(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val apiHelper2: ApiHelper2
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(MainRepository(), apiHelper, apiHelper2) as T
            }
            modelClass.isAssignableFrom(TestMainViewModel::class.java) -> {
                TestMainViewModel(MainRepository(), apiHelper, apiHelper2) as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}