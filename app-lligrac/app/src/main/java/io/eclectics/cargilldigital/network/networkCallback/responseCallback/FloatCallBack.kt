package io.eclectics.cargill.network.networkCallback.responseCallback

import com.google.gson.annotations.SerializedName

data class FloatCallBack(
    @SerializedName( "id") var id:String,
    @SerializedName( "accountNumber")  var accountNumber:String,
@SerializedName("debit") var debit:String,
@SerializedName("credit") var credit:String,
@SerializedName("balance") var balance:String,
@SerializedName("date") var date:String,
@SerializedName("accountId")var accountId:String
)