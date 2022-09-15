package io.eclectics.cargilldigital.data.model

import com.google.gson.annotations.SerializedName

object CoopEvalue {
    class EvalueList(
        @SerializedName("id") val id:Int,
        @SerializedName("accountName") val  accountName:String,
        @SerializedName("accountNumber") val  accountNumber:String,
        @SerializedName("amountBooked") val  amountBooked:Int,
        @SerializedName("coopId") val  coopId:String,
        @SerializedName("dateOfBooking") val  dateOfBooking:String?,
        @SerializedName("bookedBy") var  bookedBy: String?,
        @SerializedName("status") var  status:Boolean?,
        @SerializedName("reasonForBooking") var  reasonForBooking:String?,
        @SerializedName("processId") var  processId: String?,
        @SerializedName("stageOneApproval") var stageOneApproval: String?,
        @SerializedName("stageTwoApproval") var stageTwoApproval:String?,
        @SerializedName("poolRequestId") var poolRequestId:String,
        @SerializedName("bookingId") var bookingId:String,
        @SerializedName("stageTwoApprovalDate") var  stageTwoApprovalDate: String?

    )
}