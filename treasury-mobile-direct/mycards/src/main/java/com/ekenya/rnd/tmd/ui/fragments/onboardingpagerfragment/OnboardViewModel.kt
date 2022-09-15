package com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekenya.rnd.tmd.data.network.request.NewUserRequest
import com.ekenya.rnd.tmd.data.network.request.OtpRequest
import com.ekenya.rnd.tmd.data.repository.RegisterUserRepo
import java.io.File
import javax.inject.Inject

class OnboardViewModel @Inject constructor(
    val registerUserRepo: RegisterUserRepo
) : ViewModel() {

    val newUserRequest: MutableLiveData<NewUserRequest> = MutableLiveData(
        NewUserRequest(
            language = "English",
            nationality = "Kenyan",
            maritalStatus = "Single"
        )
    )
    val multiParts = MutableLiveData<MutableList<File>>(mutableListOf())

    fun sendUserRegistration() = registerUserRepo.registerUser(
        newUserRequest.value!!,
        multiParts.value!!
    )

    fun sendOtp(otpvalue: String) = registerUserRepo.sentOtp(
        OtpRequest(
            newUserRequest.value?.phoneNumber,
            otpValue = otpvalue,
            otpType = "DEVICE_VERIFICATION"

        )
    )
}
