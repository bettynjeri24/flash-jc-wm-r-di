package com.ekenya.rnd.dashboard.datadashboard.model

import com.google.gson.annotations.SerializedName


data class TrafficLookupResponseData (
    @SerializedName("region") var region : String? = null,
    @SerializedName("license_grade") var licenseGrade         : String? = null,
    @SerializedName("license_number") var licenseNumber        : String? = null,
    @SerializedName("driver_id") var driverID : String? = null,
    @SerializedName("driver_name") var driverName : String? = null,
)