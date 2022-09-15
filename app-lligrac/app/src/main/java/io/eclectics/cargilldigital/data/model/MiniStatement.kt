package io.eclectics.cargilldigital.data.model

import com.google.gson.annotations.SerializedName

class MiniStatement(
    @SerializedName("id") val id:Int,
    @SerializedName("walletid") val  walletid:String,
    @SerializedName("transactionCode") val  transactionId:String,
    @SerializedName("transactionType") val  transactionType:Int,
    @SerializedName("debitAccount") val  debitAccount:String,
    @SerializedName("creditAccount") val  creditAccount:String,
    @SerializedName("reasons") val  reasons:String?,
    @SerializedName("datecreated") val  datetime:String,
    @SerializedName("transactionChannel") val  transactionChannel:String,
    @SerializedName("recipientPhoneNumber") val  phonenumber: String,
    @SerializedName("sendorPhoneNumber") val  sendorPhoneNumber:String,
    @SerializedName("userid") val  userid:String?,
    @SerializedName("amount") val  amount:String?,
    @SerializedName("transactionDirection") val  transactionDirection: String,
    @SerializedName("status") var status: String,
    @SerializedName("walletTransactionType") var walletTransactionType:String,
    @SerializedName("sendorName") var sendorName:String,
    @SerializedName("recipientName") var recipientName:String,
    @SerializedName("channelRefNo") val channelRefNo:String?
)