package com.ekenya.rnd.cargillbuyer.data.responses


import com.google.gson.annotations.SerializedName

data class BuyerTopUpRequestsResponse(
    @SerializedName("statusDescription")
    var statusDescription: String?, // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var data: List<BuyerTopUpRequestsData>?
)
    data class BuyerTopUpRequestsData(
        @SerializedName("bookingID")
        var bookingID: String?, // IIWSRWA2
        @SerializedName("buyerId")
        var buyerId: String?, // fc48b6da-b7ed-4aeb-8726-35af83a71d2e
        @SerializedName("firstName")
        var firstName: String?, // Oscar
        @SerializedName("lastName")
        var lastName: String?, // Muigai
        @SerializedName("phonenumber")
        var phonenumber: String?, // 2250703035850
        @SerializedName("amountRequested")
        var amountRequested: Int?, // 3000
        @SerializedName("dateRequested")
        var dateRequested: String?, // 2022-06-21T18:34:02.93
        @SerializedName("reasons")
        var reasons: String?, // request funds 
        @SerializedName("status")
        var status: Boolean?, // true
        @SerializedName("amountApproved")
        var amountApproved: Any?, // null
        @SerializedName("id")
        var id: Int?, // 77
        @SerializedName("isArchived")
        var isArchived: Boolean? // false
    )
