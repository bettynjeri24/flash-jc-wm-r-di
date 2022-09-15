package com.ekenya.rnd.tijara.network.model.local


import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.google.gson.annotations.SerializedName

data class ClientBasicInfoFinalResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("items")
    val items: List<SaccoDetail>,
    @SerializedName("message")
    val message: String, // Data Successfully Validated
    @SerializedName("status")
    val status: Int // 1
)
    data class Data(
        @SerializedName("facePhotoUrl")
        val facePhotoUrl: Boolean, // false
        @SerializedName("associatedOrgs")
        val associatedOrgs: List<AssociatedOrg>,
        @SerializedName("idBackUrl")
        val idBackUrl: Boolean, // false
        @SerializedName("idFrontUrl")
        val idFrontUrl: Boolean, // false
        @SerializedName("username")
        val username: String,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("passportPhotoUrl")
        val passportPhotoUrl: String // /var/www/html/tijara-api/uploads/organizations/013/clients/MN194S/passportPhoto.svg
    )
    data class AssociatedOrg(
        @SerializedName("firstName")
        val firstName: String, // Jacktone
        @SerializedName("name")
        val name: String, // FAULU
        @SerializedName("org_id")
        val orgId: Int, // 12
        @SerializedName("username")
        val username: String,// 634a
        @SerializedName("isSacco")
        val isSacco: Boolean, // true
    )