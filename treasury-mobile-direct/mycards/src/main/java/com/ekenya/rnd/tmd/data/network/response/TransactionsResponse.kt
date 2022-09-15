package com.ekenya.rnd.tmd.data.network.response

import com.google.gson.annotations.SerializedName

data class TransactionsResponse(

    @field:SerializedName("data")
    val data: TransactionsData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class TransactionsData(

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
    val content: List<TransactionItem?>? = null,

    @field:SerializedName("first")
    val first: Boolean? = null,

    @field:SerializedName("totalElements")
    val totalElements: Int? = null,

    @field:SerializedName("empty")
    val empty: Boolean? = null
)

data class TransactionItem(

    @field:SerializedName("cif")
    val cif: String? = null,

    @field:SerializedName("amount")
    val amount: Int? = null,

    @field:SerializedName("transactionReference")
    val transactionReference: String? = null,

    @field:SerializedName("mobileNumber")
    val mobileNumber: String? = null,

    @field:SerializedName("sourceOfFunds")
    val sourceOfFunds: String? = null,

    @field:SerializedName("valueDate")
    val valueDate: String? = null,

    @field:SerializedName("accountNumber")
    val accountNumber: String? = null,

    @field:SerializedName("createdOn")
    val createdOn: String? = null,

    @field:SerializedName("drcr")
    val drcr: String? = null,

    @field:SerializedName("softDelete")
    val softDelete: Boolean? = null,

    @field:SerializedName("narration")
    val narration: String? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
