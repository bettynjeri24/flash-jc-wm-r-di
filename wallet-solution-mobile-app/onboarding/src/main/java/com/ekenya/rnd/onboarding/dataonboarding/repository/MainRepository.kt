package com.ekenya.rnd.onboarding.dataonboarding.repository

import com.ekenya.rnd.onboarding.dataonboarding.api.ApiHelper
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.onboarding.dataonboarding.model.RegistrationResponse2

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun registerUser(userData: MainDataObject): RegistrationResponse2 {

        return apiHelper.registerUser(userData)
    }
}