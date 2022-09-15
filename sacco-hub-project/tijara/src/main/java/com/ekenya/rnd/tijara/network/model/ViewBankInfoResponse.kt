package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ViewBankInfoResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<ViewBankInfoList>
)
@Parcelize
    data class ViewBankInfoList(
        @SerializedName("accountName")
        val accountName: String,
        @SerializedName("accountNo")
        val accountNo: String,
        @SerializedName("bank")
        val bank: String,
        @SerializedName("branch")
        val branch: String,
        @SerializedName("isDefaultAccount")
        val isDefaultAccount: String,
        @SerializedName("id")
        val id: Int

    ):Parcelable
