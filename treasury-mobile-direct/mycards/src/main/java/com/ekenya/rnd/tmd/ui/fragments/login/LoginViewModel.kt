package com.ekenya.rnd.tmd.ui.fragments.login

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.tmd.data.repository.LoginRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    fun login(userName: String, pin: String) = loginRepository.login(
        userName = userName,
        passWord = pin
    )
}
