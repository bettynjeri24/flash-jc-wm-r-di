package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ParkingLookUpResponse(
    @SerializedName("data")
    val `data`: ParkingLookUpData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class ParkingLookUpData(
        @SerializedName("Amount")
        val amount: Int, // 300
        @SerializedName("BankName")
        val bankName: Any, // null
        @SerializedName("billerCode")
        val billerCode: String, // NRBC_DAILY_PARKING
        @SerializedName("BranchName")
        val branchName: Any, // null
        @SerializedName("ChargeID")
        val chargeID: Int, // 10
        @SerializedName("ChargeName")
        val chargeName: Any, // null
        @SerializedName("Commission")
        val commission: Int, // 0
        @SerializedName("DurationDescription")
        val durationDescription: Any, // null
        @SerializedName("DurationID")
        val durationID: Int, // 1
        @SerializedName("EndDate")
        val endDate: Any, // null
        @SerializedName("ExpiryDate")
        val expiryDate: Any, // null
        @SerializedName("ID")
        val iD: String, // 83fae8de-6e26-ec11-9467-7427ea2f7f59
        @SerializedName("IsDaily")
        val isDaily: Boolean, // true
        @SerializedName("LocalReceiptNumber")
        val localReceiptNumber: Any, // null
        @SerializedName("MerchantID")
        val merchantID: String, // NCC
        @SerializedName("Names")
        val names: Any, // null
        @SerializedName("Narration")
        val narration: Any, // null
        @SerializedName("PaidBy")
        val paidBy: Any, // null
        @SerializedName("Payee")
        val payee: Any, // null
        @SerializedName("PaymentDetails")
        val paymentDetails: Any, // null
        @SerializedName("PaymentTypeID")
        val paymentTypeID: Int, // 1
        @SerializedName("PhoneNumber")
        val phoneNumber: String, // 254718194920
        @SerializedName("Pin")
        val pin: Any, // null
        @SerializedName("PlateNumber")
        val plateNumber: String, // KCK098
        @SerializedName("ReceiptNumber")
        val receiptNumber: Any, // null
        @SerializedName("ReferenceNumber")
        val referenceNumber: Any, // null
        @SerializedName("SourceIP")
        val sourceIP: Any, // null
        @SerializedName("StartDate")
        val startDate: Any, // null
        @SerializedName("Tendered")
        val tendered: Int, // 0
        @SerializedName("TransactionDate")
        val transactionDate: Any, // null
        @SerializedName("TransactionID")
        val transactionID: String, // 1042950
        @SerializedName("ZoneCodeID")
        val zoneCodeID: Int, // 10
        @SerializedName("ZoneName")
        val zoneName: Any // null
    )
