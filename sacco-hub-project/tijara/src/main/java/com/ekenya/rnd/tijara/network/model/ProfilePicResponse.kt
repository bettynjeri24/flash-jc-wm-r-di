package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class ProfilePicResponse(
    @SerializedName("data")
    val `data`: ProfilePicData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class ProfilePicData(
        @SerializedName("facePhotoUrl")
        val facePhotoUrl: String // https://test-portal.ekenya.co.ke/tijara-api/uploads/organizations/013/clients/MOM110/selfiePhotoPhoto.jpg
    )
