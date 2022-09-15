package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ViewNextOfKinResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<ViewNextKinList>
)
@Parcelize
    data class ViewNextKinList(
        @SerializedName("allocation")
        val allocation: String,
        @SerializedName("dob")
        val dob: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("fullName")
        val fullName: String,
        @SerializedName("nationalIdentity")
        val nationalIdentity: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("relationship")
        val relationship: String,
        @SerializedName("id")
        val id: Int
    ):Parcelable
