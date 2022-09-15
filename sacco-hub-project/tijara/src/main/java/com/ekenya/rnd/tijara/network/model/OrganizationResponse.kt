package com.ekenya.rnd.tijara.network.model


import com.google.gson.annotations.SerializedName

data class OrganizationResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<SaccoList>
)
    data class SaccoList(
        @SerializedName("activation_remarks")
        val activationRemarks: Any,
        @SerializedName("alt_email")
        val altEmail: Any,
        @SerializedName("alt_phone")
        val altPhone: Any,
        @SerializedName("business_class")
        val businessClass: Int,
        @SerializedName("code")
        val code: String,
        @SerializedName("contact_person")
        val contactPerson: String,
        @SerializedName("country_id")
        val countryId: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("created_by")
        val createdBy: Any,
        @SerializedName("email")
        val email: String,
        @SerializedName("head_office_location")
        val headOfficeLocation: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("is_suspended")
        val isSuspended: Int,
        @SerializedName("logo")
        val logo: Any,
        @SerializedName("name")
        val name: String,
        @SerializedName("isSacco")
        val isSacco: Boolean, // true
        @SerializedName("notes")
        val notes: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("postal_address")
        val postalAddress: String,
        @SerializedName("reg_date")
        val regDate: String,
        @SerializedName("reg_no")
        val regNo: String,
        @SerializedName("suspended_by")
        val suspendedBy: Any,
        @SerializedName("suspension_date")
        val suspensionDate: Any,
        @SerializedName("suspension_remarks")
        val suspensionRemarks: Any,
        @SerializedName("tax_pin")
        val taxPin: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("updated_by")
        val updatedBy: Int,
        @SerializedName("website")
        val website: String
    )
