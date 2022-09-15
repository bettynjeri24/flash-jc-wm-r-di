package com.ekenya.rnd.dashboard.datadashboard.model

import com.google.gson.annotations.SerializedName


data class TrafficChargesResponseData (
    @SerializedName("receipt_reference") var receipt_reference : String? = null,
    @SerializedName("offense_id") var offenseID         : String? = null,
    @SerializedName("region") var region : String? = null,
    @SerializedName("license_grade") var licenseGrade : String? = null,
    @SerializedName("license_number") var licenseNumber : String? = null,
    @SerializedName("amount") var amount : String? = null,
)