package com.ekenya.lamparam.network

import com.google.gson.annotations.SerializedName

/**
 * Main Response class
 */
class DefaultResponse(
    @SerializedName("expiry") var tokenExpiry: String,
    @SerializedName("token") var token: String,
    @SerializedName("Password") var pwd: String,
    @SerializedName("OTP") var otp: String,
    //
    @SerializedName("MTI") var mti: String,
    @SerializedName("TransactionReference") var transactionRef: String,
    @SerializedName("Timestamp") var timeStamp: String,
    @SerializedName("ReqType") var requestType: String,
    @SerializedName("ResponseCode") var responseCode: String,
    @SerializedName("Channel") var channel: String,
    @SerializedName("ResponseMessage") var responseMessage: String,
    @SerializedName("ServiceName") var serviceName: String,
    @SerializedName("PhoneNumber") var phoneNumber: String,
    @SerializedName("CorrelationID") var correlationID: String,
    @SerializedName("Profile") var userProfile: UserProfile, //user's profile details
    @SerializedName("AccountDetails") var accountLookup: AccountDetails, //user's profile details
    @SerializedName("MiniStatementData") var ministatement: List<Ministatement>, //a list of ministatement data
    @SerializedName("Balances") var balances: Balances, //user's profile details


) {

    /**
     * Retrieve a user's details on login
     */
    inner class UserProfile(
        @SerializedName("Language") val language: String,
        @SerializedName("FirstName") var firstName: String,
        @SerializedName("WalletAccount") var walletAccount: String,
        @SerializedName("LastLoginDate") var lastLoginDate: String,
        @SerializedName("PhoneNumber") var phoneNumber: String,
        @SerializedName("LastName") var lastName: String,
        @SerializedName("Gender") var gender: String,
        @SerializedName("SecondName") var secondName: String,
        @SerializedName("EmailAddress") var emailAddress: String,
        @SerializedName("IDNo") var idNumber: String,
    )

    /**
     * Retrieve a user's details on account lookup
     */
    inner class AccountDetails(
        @SerializedName("WalletAccount") val phoneNumber: String,
        @SerializedName("AccountName") var accountName: String,
    )

    /**
     * Retrieve a user's mini-statement data
     */
    inner class Ministatement(
        @SerializedName("Language") val language: String,
        @SerializedName("FirstName") var firstName: String,
        @SerializedName("WalletAccount") var walletAccount: String,
        @SerializedName("LastLoginDate") var lastLoginDate: String,
        @SerializedName("PhoneNumber") var phoneNumber: String,
    )

    /**
     * Retrieve a user's account balance
     */
    inner class Balances(
        @SerializedName("AvailableBalance") val availableBalance: String,
        @SerializedName("ActualBalance") var actualBalance: String,
    )

}
