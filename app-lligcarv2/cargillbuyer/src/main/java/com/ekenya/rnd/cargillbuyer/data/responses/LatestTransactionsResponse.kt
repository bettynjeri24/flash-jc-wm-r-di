package com.ekenya.rnd.cargillbuyer.data.responses


import com.google.gson.annotations.SerializedName

data class LatestTransactionsResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var data: List<LatestTransactionsData>?
)

data class LatestTransactionsData(
    @SerializedName("id")
    var id: Int?, // 194
    @SerializedName("sendorwalletid")
    var sendorwalletid: String?, // 7f193e8a-d46c-4acd-baeb-2cd25a7fa4ae
    @SerializedName("recipientwalletid")
    var recipientwalletid: String?, // c4b648d1-5b71-4c28-bba4-dc1d5e8724ee
    @SerializedName("sendorwalletaccnumber")
    var sendorwalletaccnumber: String?, // 9380-6991-2486
    @SerializedName("recipientwalletaccnumber")
    var recipientwalletaccnumber: String?, // 7587-9831-9126
    @SerializedName("sendoraccountbalance")
    var sendoraccountbalance: Int?, // 1738810
    @SerializedName("recipientaccountbalance")
    var recipientaccountbalance: Int?, // 14000
    @SerializedName("datecreated")
    var datecreated: String?, // 2022-06-22T18:34:52.03
    @SerializedName("sendoruserid")
    var sendoruserid: String?, // fc48b6da-b7ed-4aeb-8726-35af83a71d2e
    @SerializedName("recipientuserid")
    var recipientuserid: String?, // fc48b6da-b7ed-4aeb-8726-35af83a71d2e
    @SerializedName("walletTransactionType")
    var walletTransactionType: Int?, // 1
    @SerializedName("sendorPhoneNumber")
    var sendorPhoneNumber: String?, // 2250703035850
    @SerializedName("recipientPhoneNumber")
    var recipientPhoneNumber: String?, // 2250703035850
    @SerializedName("status")
    var status: Boolean?, // true
    @SerializedName("transactiontype")
    var transactiontype: Int?, // 10
    @SerializedName("reasons")
    var reasons: String?, // Purchase Cocoa
    @SerializedName("amount")
    var amount: String?, // 1000
    @SerializedName("unixtimestamp")
    var unixtimestamp: Int?, // 1655912092
    @SerializedName("recipientName")
    var recipientName: String?, // Oscar Muigai
    @SerializedName("sendorName")
    var sendorName: String?, // Société Coopérative Agricole Adzopé Nord (SOCAAN)
    @SerializedName("transactionCode")
    var transactionCode: String?, // HX9ZZ3A9LSJS
    @SerializedName("farmForceReferenceCode")
    var farmForceReferenceCode: String? // G5005445
)
