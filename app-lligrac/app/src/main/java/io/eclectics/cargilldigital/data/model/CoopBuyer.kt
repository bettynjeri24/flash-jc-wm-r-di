package io.eclectics.cargilldigital.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

object CoopBuyer {
    @Entity(tableName = "coopbuyerlist")
    data class BuyerList(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("id") val id:Int,
        @SerializedName("buyerid") val  cocoaBuyerId:String,
        @SerializedName("firstName") val  firstName:String,
        @SerializedName("middleName") val  middleName:String,
        @SerializedName("lastName") val  lastName:String,
        @SerializedName("balanceOnRequest") val  balanceOnRequest:String?,
        @SerializedName("emailAddress") var  emailAddress: String?,
        @SerializedName("phonenumber") var  phoneNumber:String?,
        @SerializedName("otherPhoneNumber") var  otherPhoneNumber:String?,
        @SerializedName("cooperativeid") var cooperativeid: String,
        @SerializedName("employeeNumber") var employeeNumber:String?,
        @SerializedName("userid") var userid:String,
        @SerializedName("createdAt") var  createdAt: String?,
        @SerializedName("sectionName") var sectionid: String,
        @SerializedName("coopRegAccount") var coopRegAccount:String?

    )
}