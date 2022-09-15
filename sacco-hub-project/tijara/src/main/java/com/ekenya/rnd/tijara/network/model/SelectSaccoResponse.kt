package com.ekenya.rnd.tijara.network.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
data class SelectSaccoResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val `data`: List<SaccoDetail>,
    @SerializedName("message")
    val message: String //"There are (2) organizations linked to your phone number. Please select one to login"
)
@Parcelize
    data class SaccoDetail(
        @SerializedName("name")
        val name: String,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("username")
        val username: String, //Stima
        @SerializedName("org_id")
        val orgId: Int,//200
        @SerializedName("isSacco")
        val isSacco: Boolean, // true
    ):Parcelable
