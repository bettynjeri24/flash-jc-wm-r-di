package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class PersonalInfoResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: PersonalInfoList
)
    data class PersonalInfoList(
        @SerializedName("country")
        val country: String,
        @SerializedName("dateJoined")
        val dateJoined: String,
        @SerializedName("dob")
        val dob: String,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("fullName")
        val fullName: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("lastName")
        val lastName: String,
        @SerializedName("middleName")
        val middleName: String,
        @SerializedName("nationalIdentity")
        val nationalIdentity: String,
        @SerializedName("number")
        val number: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("email")
        val email: String,
    )
