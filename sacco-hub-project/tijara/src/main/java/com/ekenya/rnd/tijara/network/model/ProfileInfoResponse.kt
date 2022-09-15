package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ProfileInfoResponse(
    @SerializedName("data")
    val `data`: ProfileData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)

    data class ProfileData(
        @SerializedName("bankInfo")
        val bankInfo: List<BankInfo>,
        @SerializedName("identification")
        val identification: Identification,
        @SerializedName("nextOfKin")
        val nextOfKin: List<NextOfKin>,
        @SerializedName("personalInfo")
        val personalInfo: PersonalInfo,
        @SerializedName("residenceInfo")
        val residenceInfo: ResidenceInfo,
        @SerializedName("workInfo")
        val workInfo: List<WorkInfo>
    )
@Parcelize
        data class BankInfo(
            @SerializedName("accountName")
            val accountName: String, // Moses Ochola
            @SerializedName("accountNo")
            val accountNo: String, // 101010111322
            @SerializedName("bank")
            val bank: String, // Barclays Bank
            @SerializedName("branch")
            val branch: String ,// Barclays Queens Way
        val id:Int
        ):Parcelable

        data class Identification(
            @SerializedName("dob")
            val dob: String, // 1948-01-06
            @SerializedName("fullName")
            val fullName: String, // Moses Ochola
            @SerializedName("gender")
            val gender: String, // Male
            @SerializedName("idNumber")
            val idNumber: String // 232223335
        )
@Parcelize
        data class NextOfKin(
    val id:Int,
            @SerializedName("allocation")
            val allocation: String, // 60.00%
            @SerializedName("dob")
            val dob: String, // null
            @SerializedName("email")
            val email: String, // isaac@gmail.com
            @SerializedName("fullName")
            val fullName: String, // Isaac Ochola
            @SerializedName("gender")
            val gender: String,
            @SerializedName("nationalIdentity")
            val nationalIdentity: String, // 25249759
            @SerializedName("phone")
            val phone: String, // 0724962380
            @SerializedName("relationship")
            val relationship: String // Grand child
        ):Parcelable

        data class PersonalInfo(
            @SerializedName("email")
            val email: String, // orikohjtacktone@gmail.com
            @SerializedName("facePhotoUrl")
            val facePhotoUrl: String,
            @SerializedName("idBackUrl")
            val idBackUrl: String,
            @SerializedName("idFrontUrl")
            val idFrontUrl: String,
            @SerializedName("kraPin")
            val kraPin: String, // 21222222
            @SerializedName("memberNumber")
            val memberNumber: String, // MOM110
            @SerializedName("passportPhotoUrl")
            val passportPhotoUrl: String,
            @SerializedName("phone")
            val phone: String // 0718194920
        )

        data class ResidenceInfo(
            @SerializedName("estateName")
            val estateName: String, // Umoja 1
            @SerializedName("houseNo")
            val houseNo: String, // null
            @SerializedName("residencyType")
            val residencyType: String, // Rented
            @SerializedName("road")
            val road: String, // null
            @SerializedName("street")
            val street: String // null
        )
       @Parcelize
        data class WorkInfo(
            val id:Int,
            @SerializedName("address")
            val address: String,
            @SerializedName("branch")
            val branch: String, // null
            @SerializedName("city")
            val city: String, // Nairobi
            @SerializedName("employer")
            val employer: String, // NGO
            @SerializedName("employmentStatus")
            val employmentStatus: String // Permanent
        ):Parcelable
