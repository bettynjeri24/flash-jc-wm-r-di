package com.ekenya.rnd.cargillbuyer.data.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FarmerDetailsResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Success
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var `data`: List<FarmerDetailsData>?
)
@Parcelize
data class FarmerDetailsData(
    @SerializedName("id")
    var id: Int?, // 1138
    @SerializedName("farmerid")
    var farmerid: String?, // 0ec96dd5-7570-4f94-96c1-dc460327aff8
    @SerializedName("firstName")
    var firstName: String?, // Marc
    @SerializedName("middleName")
    var middleName: String?,
    @SerializedName("lastName")
    var lastName: String?, // Zogoury
    @SerializedName("govtIdNumber")
    var govtIdNumber: String?, // null
    @SerializedName("emailAddress")
    var emailAddress: String?, // marc.zogoury@gmail.com
    @SerializedName("phoneNumber")
    var phoneNumber: String?, // 2250554954258
    @SerializedName("otherPhoneNumber")
    var otherPhoneNumber: String?, // null
    @SerializedName("gender")
    var gender: String?, // Male
    @SerializedName("datecreated")
    var datecreated: String?, // 2022-06-22T00:00:00
    @SerializedName("cooperativeid")
    var cooperativeid: String?, // 1415688e-da10-4947-9621-b5b8249d1165
    @SerializedName("producerCode")
    var producerCode: String?, // FF4524152454
    @SerializedName("address")
    var address: String?, // Yamoussoukro
    @SerializedName("nationalId")
    var nationalId: String?, // null
    @SerializedName("dateOfBirth")
    var dateOfBirth: String?, // null
    @SerializedName("language")
    var language: String?, // null
    @SerializedName("certificationnumber")
    var certificationnumber: String?, // CERT-3425435
    @SerializedName("village")
    var village: String?, // null
    @SerializedName("cluster")
    var cluster: String?, // null
    @SerializedName("district")
    var district: String?, // Yamoussoukro
    @SerializedName("region")
    var region: String?, // null
    @SerializedName("country")
    var country: String?, // null
    @SerializedName("isactive")
    var isactive: Boolean?, // false
    @SerializedName("totalLandArea")
    var totalLandArea: String?, // null
    @SerializedName("sectionid")
    var sectionid: Int?, // 1013
    @SerializedName("coopregaccount")
    var coopregaccount: String?, // 12121234
    @SerializedName("farmforceid")
    var farmforceid: String? // FF4524152454
) : Parcelable
