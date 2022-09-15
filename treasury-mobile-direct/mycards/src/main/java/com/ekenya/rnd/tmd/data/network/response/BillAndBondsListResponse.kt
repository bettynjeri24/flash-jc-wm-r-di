package com.ekenya.rnd.tmd.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class BillAndBondsListResponse(

    @field:SerializedName("data")
    val data: BillsAndBondsData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class Pageable(

    @field:SerializedName("paged")
    val paged: Boolean? = null,

    @field:SerializedName("pageNumber")
    val pageNumber: Int? = null,

    @field:SerializedName("offset")
    val offset: Int? = null,

    @field:SerializedName("pageSize")
    val pageSize: Int? = null,

    @field:SerializedName("unpaged")
    val unpaged: Boolean? = null,

    @field:SerializedName("sort")
    val sort: Sort? = null
)

data class BillsAndBondsData(

    @field:SerializedName("number")
    val number: Int? = null,

    @field:SerializedName("last")
    val last: Boolean? = null,

    @field:SerializedName("numberOfElements")
    val numberOfElements: Int? = null,

    @field:SerializedName("size")
    val size: Int? = null,

    @field:SerializedName("totalPages")
    val totalPages: Int? = null,

    @field:SerializedName("pageable")
    val pageable: Pageable? = null,

    @field:SerializedName("sort")
    val sort: Sort? = null,

    @field:SerializedName("content")
    val content: List<ContentItem?>? = null,

    @field:SerializedName("first")
    val first: Boolean? = null,

    @field:SerializedName("totalElements")
    val totalElements: Int? = null,

    @field:SerializedName("empty")
    val empty: Boolean? = null
)

@Parcelize
data class ContentItem(

    @field:SerializedName("interestRate")
    val interestRate: Double? = null,

    @field:SerializedName("bidExpiryDate")
    val bidExpiryDate: String? = null,

    @field:SerializedName("taxable")
    val taxable: Boolean? = null,

    @field:SerializedName("allowRollOver")
    val allowRollOver: Boolean? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("createdOn")
    val createdOn: String? = null,

    @field:SerializedName("maturityPeriod")
    val maturityPeriod: Int? = null,

    @field:SerializedName("softDelete")
    val softDelete: Boolean? = null,

    @field:SerializedName("offerDate")
    val offerDate: String? = null,

    @field:SerializedName("targetBondAmount")
    val targetBondAmount: Double? = null,

    @field:SerializedName("bondReferenceNumber")
    val bondReferenceNumber: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("competitive")
    val competitive: Boolean? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Parcelable

data class Sort(

    @field:SerializedName("unsorted")
    val unsorted: Boolean? = null,

    @field:SerializedName("sorted")
    val sorted: Boolean? = null,

    @field:SerializedName("empty")
    val empty: Boolean? = null
)
