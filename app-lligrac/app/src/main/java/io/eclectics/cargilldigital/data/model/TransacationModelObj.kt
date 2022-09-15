package io.eclectics.cargilldigital.data.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class TransacationModelObj (
    @SerializedName("id") val id:Int,
    @SerializedName("walletid") val  walletid:String,
    @SerializedName("transactionId") val  transactionId:String,
    @SerializedName("transactionType") val  transactionType:Int,
    @SerializedName("debitAccount") val  debitAccount:String,
    @SerializedName("creditAccount") val  creditAccount:String,
    @SerializedName("poolrequestid") val  poolrequestid:String,
    @SerializedName("datetime") val  datetime:String,
    @SerializedName("transactionChannel") val  transactionChannel:String,
    @SerializedName("phonenumber") val  phonenumber: JsonObject,
    @SerializedName("userid") val  userid:String?,
    @SerializedName("transactionDirection") val  transactionDirection: String,
    @SerializedName("channel") var channel: String,
    @SerializedName("channelRefNo") val channelRefNo:String?,
    @SerializedName("token") val token:String,
    @SerializedName("locked") val locked:String?
)