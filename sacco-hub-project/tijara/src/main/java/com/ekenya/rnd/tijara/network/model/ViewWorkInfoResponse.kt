package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ViewWorkInfoResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<ViewWorkInfoList>
)
@Parcelize
    data class ViewWorkInfoList(
        @SerializedName("branch")
        val branch: String,
        @SerializedName("department")
        val department: String,
        @SerializedName("designation")
        val designation: String,
        @SerializedName("employer")
        val employer: String,
        @SerializedName("employmentId")
        val employmentId: Int,
        @SerializedName("employmentTerm")
        val employmentTerm: String,
        @SerializedName("workStation")
        val workStation: String,
        @SerializedName("id")
        val id: Int
    ):Parcelable
