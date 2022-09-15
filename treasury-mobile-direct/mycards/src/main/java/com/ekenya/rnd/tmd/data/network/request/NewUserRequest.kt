package com.ekenya.rnd.tmd.data.network.request

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class NewUserRequest(

    @field:SerializedName("firstName")
    var firstName: String? = null,

    @field:SerializedName("lastName")
    var lastName: String? = null,

    @field:SerializedName("phoneNumber")
    var phoneNumber: String? = null,

    @field:SerializedName("gender")
    var gender: String? = null,

    @field:SerializedName("nationalId")
    var nationalId: String? = null,

    @field:SerializedName("nationality")
    var nationality: String? = null,

    @field:SerializedName("dateOfBirth")
    var dateOfBirth: String? = null,

    @field:SerializedName("language")
    var language: String? = null,

    @field:SerializedName("middleName")
    var middleName: String? = null,

    @field:SerializedName("email")
    var email: String? = null,

    @field:SerializedName("maritalStatus")
    var maritalStatus: String? = null
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
