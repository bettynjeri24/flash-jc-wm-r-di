package com.ekenya.lamparam.ui.onboarding.registration

/**
 * Fields sent during registration
 */
data class RegFields(
    var emailAddress: String = "",
    var fullName: String = "",
    var address: String = "",
    var accountType: String = "wallet",
    var phoneNumber: String = "",
    var altPhoneNumber: String = "",
    var dateOfBirth: String = "",
    var idType: String = "",
    var nationalID: String = "",
    var referralCode: String = "",
    var occupation: String = "",
    var otp: String = "",

)