package com.ekenya.lamparam.activities.onboarding

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ekenya.lamparam.network.Resource
import com.ekenya.lamparam.repository.OnboardingRepository
import com.ekenya.lamparam.ui.onboarding.registration.RegFields
import com.ekenya.lamparam.user.UserDataRepository
import com.ekenya.lamparam.utilities.GlobalMethods
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import javax.inject.Inject

/**
 * Handles data flowing across onBoarding screens
 */
class OnBoardingViewModel @Inject constructor(
    private val repository: OnboardingRepository,
    private val globalMethods: GlobalMethods,
    private val userDataRepository: UserDataRepository) : ViewModel() {

    private val _userData = MutableLiveData<RegFields>()
    val userData: LiveData<RegFields>
    get() = _userData

    /**
     * Sets new value of user
     */
    fun setUser() {
        _userData.value = RegFields(
            "", "", "", "", "",
            "", "", "", "", "", "",""
        )
    }

    /**
     * Sets the otp for a user
     */
    fun setOtp(otp: String){
        _userData.value?.otp = otp
    }

    /**
     * Sets phone number during registration
     */
    fun setUserPhone(phone: String) {
        _userData.value?.phoneNumber = phone
    }

    /**
     * Sets personal info during registration
     */
    fun setPersonalInfo(name: String, dob: String, address: String, selfieImg: Bitmap?) {
        _userData.value?.let {
            it.fullName = name
            it.dateOfBirth = dob
            it.address = address

        }

    }

    /**
     * Sets personal info during registration
     */
    fun setUserIDInfo(docNumber: String, docType: String) {
        _userData.value?.let {
            it.nationalID = docNumber
            it.idType = docType
        }
    }

    fun setAltDetails(referralCode: String, email: String, altMobile: String, occupation: String) {
        _userData.value?.let{
            it.referralCode = referralCode
            it.emailAddress = email
            it.altPhoneNumber = altMobile
            it.occupation = occupation
        }
    }

    /**
     * Send request to get app token and store it in shared preferences
     */
    fun getAppToken() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            emit(
                Resource.success(
                    data = repository.appToken("harry","password")
                )
            ) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Send request to get app token and store it in shared preferences
     */
    fun accountLookUp(phoneNumber: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("PhoneNumber", phoneNumber)
            requestBody.put("ReqType", "AccountLookup")
            requestBody.put("Channel", "APP")
            requestBody.put("MTI", "0100")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")

            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Sends the registration request
     */
    fun register(user: RegFields) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("EmailAddress", user.emailAddress)
            requestBody.put("FullName", user.fullName)
            requestBody.put("Address", user.address)
            requestBody.put("AccountType", "wallet")
            requestBody.put("PhoneNumber", user.phoneNumber)
            requestBody.put("AlternatePhoneNumber", user.altPhoneNumber)
            requestBody.put("ReqType", "CustomerRegistration")
            requestBody.put("ServiceName", "CustomerRegistration")
            requestBody.put("Channel", "APP")
            requestBody.put("MTI", "0100")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")
            requestBody.put("DOB", user.dateOfBirth)
            requestBody.put("Language", userDataRepository.language)
            requestBody.put("IDType", user.idType)
            requestBody.put("IDNo", user.nationalID)
            requestBody.put("ReferralCode", user.referralCode)
            requestBody.put("Occupation", user.occupation)


            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Sends the login request
     */
    fun login(phone: String, pass: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("PhoneNumber", phone)
            requestBody.put("Pin", (globalMethods.hashPwd(pass)))
            requestBody.put("ReqType", "CustomerLogin")
            requestBody.put("Channel", "APP")
            requestBody.put("MTI", "0100")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")

            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Sends the change PIN request
     */
    fun changePIN(oldPIN: String, newPIN: String, phone: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("PhoneNumber", phone)
            requestBody.put("OldPin", oldPIN)
            requestBody.put("NewPin", globalMethods.hashPwd(newPIN))
            requestBody.put("ReqType", "CustomerChangePin")
            requestBody.put("Channel", "APP")
            requestBody.put("MTI", "0100")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")

            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Sends block user request
     */
    fun blockCustomer() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("PhoneNumber", userDataRepository.phoneNumber)
            requestBody.put("ReqType", "BlockCustomer")
            requestBody.put("Channel", "APP")
            requestBody.put("MTI", "0100")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")

            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Sends change language request
     */
    fun changeLanguage() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("PhoneNumber", userDataRepository.phoneNumber)
            requestBody.put("ReqType", "CustomerChangeLanguage")
            requestBody.put("Language", userDataRepository.language)
            requestBody.put("Channel", "APP")
            requestBody.put("MTI", "0100")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")

            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Sends mini-statement request
     */
    fun miniStatement(debitAccount: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("PhoneNumber", userDataRepository.phoneNumber)
            requestBody.put("SenderName", userDataRepository.firstName)
            requestBody.put("ReqType", "MiniStatement")
            requestBody.put("ServiceName", "MiniStatement")
            requestBody.put("Channel", "APP")
            requestBody.put("DebitAccount", debitAccount)
            requestBody.put("MTI", "0200")
            requestBody.put("Currency", "ZK")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")

            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Sends checkBalance request
     */
    fun checkBalance(debitAccount: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("PhoneNumber", userDataRepository.phoneNumber)
            requestBody.put("ReqType", "BalanceEnquiry")
            requestBody.put("ServiceName", "BalanceEnquiry")
            requestBody.put("Channel", "APP")
            requestBody.put("DebitAccount", debitAccount)
            requestBody.put("MTI", "0200")
            requestBody.put("Currency", "ZK")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")

            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }

    /**
     * Sends funds transfer request
     */
    fun fundsTransfer(
        receiverPhone: String, receiverName: String, debitAccount: String, creditAccount: String,
        amount: String ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null)) //for the loading stuff
        try {
            val requestBody = JSONObject()
            requestBody.put("PhoneNumber", userDataRepository.phoneNumber)
            requestBody.put("ReceiverPhoneNumber", receiverPhone)
            requestBody.put("ReqType", "FundsTransfer")
            requestBody.put("SenderName", userDataRepository.firstName)
            requestBody.put("RecipientName", receiverName)
            requestBody.put("ServiceName", "FundsTransfer")
            requestBody.put("Channel", "APP")
            requestBody.put("DebitAccount", debitAccount)
            requestBody.put("CreditAccount", creditAccount)
            requestBody.put("Amount", amount)
            requestBody.put("MTI", "0200")
            requestBody.put("Currency", "ZK")
            requestBody.put("Timestamp", globalMethods.getCurrentTime())
            requestBody.put("TransactionReference", globalMethods.transactionReference())
            requestBody.put("DeviceID", userDataRepository.imei)
            requestBody.put("IMSI", "")

            emit(Resource.success(data = repository.sendRequest(requestBody))) //for the success stuff
        } catch (e: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = e.message ?: "Error Occurred!"
                )
            ) //for the error
        }
    }



}