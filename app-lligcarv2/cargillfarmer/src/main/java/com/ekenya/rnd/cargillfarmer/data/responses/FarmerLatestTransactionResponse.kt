package com.ekenya.rnd.cargillfarmer.data.responses


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class FarmerLatestTransactionResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?="", // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var farmerLatestTransactionData: List<FarmerLatestTransactionData>? = null
)

@Entity
data class FarmerLatestTransactionData(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    var id: Int, // 68
    @SerializedName("sendorwalletid")
    var sendorwalletid: String?, // 3818ad25-50b0-4590-9ba6-00a036501fa6
    @SerializedName("recipientwalletid")
    var recipientwalletid: String?, // 0ac3e245-1b0f-4e3e-a14d-dd2aec3260a6
    @SerializedName("sendorwalletaccnumber")
    var sendorwalletaccnumber: String?, // 5169-6414-4523
    @SerializedName("recipientwalletaccnumber")
    var recipientwalletaccnumber: String?, // 7732-2845-1168
    @SerializedName("sendoraccountbalance")
    var sendoraccountbalance: Int?, // 6100
    @SerializedName("recipientaccountbalance")
    var recipientaccountbalance: Int?, // 7600
    @SerializedName("datecreated")
    var datecreated: String?, // 2022-05-18T22:39:32.13
    @SerializedName("sendoruserid")
    var sendoruserid: String?, // 7ec59e64-ab97-4dd5-aecb-319cb73c455c
    @SerializedName("recipientuserid")
    var recipientuserid: String?, // 304a3280-a0bd-4635-a69b-c8c5b6401696
    @SerializedName("walletTransactionType")
    var walletTransactionType: Int?, // 1
    @SerializedName("sendorPhoneNumber")
    var sendorPhoneNumber: String?, // 2250701686379
    @SerializedName("recipientPhoneNumber")
    var recipientPhoneNumber: String?, // 2250703035850
    @SerializedName("status")
    var status: Boolean?, // true
    @SerializedName("transactiontype")
    var transactiontype: Int?, // 1
    @SerializedName("reasons")
    var reasons: String?, // send
    @SerializedName("amount")
    var amount: String?, // 200
    @SerializedName("unixtimestamp")
    var unixtimestamp: Int?, // 1652902772
    @SerializedName("recipientName")
    var recipientName: String?, // oscar muigai
    @SerializedName("sendorName")
    var sendorName: String?, // brice kanute
    @SerializedName("transactionCode")
    var transactionCode: String?, // X5KQYANND1VP
    @SerializedName("farmForceReferenceCode")
    var farmForceReferenceCode: String? // null
)
