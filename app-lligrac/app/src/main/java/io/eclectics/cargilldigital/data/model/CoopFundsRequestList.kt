package io.eclectics.cargilldigital.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "coopFundsRequestList")
class CoopFundsRequestList (
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id:Int,
    @SerializedName("bookingId") var bookingId:String,
    @SerializedName("phonenumber") val  phonenumber:String,
    @SerializedName("cooperativeid") val  cooperativeid:String?,
    @SerializedName("buyerId") val  buyerid:String,
    @SerializedName("coopId") val  coopId:String?,
    @SerializedName("firstName") val  firstName:String,
    @SerializedName("lastName") val  lastName:String,
    @SerializedName("dateRequested") val  dateOfRequest:String?,
    @SerializedName("amountRequested") var  amountRequested: Int?,
    @SerializedName("amountApproved") var  amountApproved:String?,
    @SerializedName("requestServiceStatus") var  requestServiceStatus: String?,
    @SerializedName("stageOneApproval") var stageOneApproval: String?,
    @SerializedName("dateServiced") var dateServiced:String?,
    @SerializedName("status") var servicedStatus:Boolean?,
    @SerializedName("isArchived") var isArchived:Boolean,
    @SerializedName("reasons") var reasons:String

    /*
     "buyerId": "534b0e69-0104-4e81-ac33-2ceb664c8f1a",
            "firstName": "Oscar",
            "lastName": "Muigai",
            "phonenumber": "2250703035850",
            "amountRequested": 4700,
            "dateRequested": "2022-04-18T17:47:30.07",
            "reasons": "request funds top up",
            "status": 1
     */

        )