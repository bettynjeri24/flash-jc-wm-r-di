package com.ekenya.rnd.onboarding.dataonboarding.api

import com.ekenya.rnd.common.data.model.MainDataObject

class ApiHelper(private val apiService: ApiService) {
    suspend fun getUsers() = apiService.getUsers()
    suspend fun registerUser( userData: MainDataObject) = apiService.registerUser(userData)
}