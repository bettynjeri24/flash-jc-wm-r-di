package com.ekenya.rnd.cargillfarmer.data.responses


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MyCashOutChannelsResponse(
    @SerializedName("statusDescription")
    var statusDescription: String? ="", // Successfull
    @SerializedName("statusCode")
    var statusCode: Int?, // 0
    @SerializedName("data")
    var myCashOutChannelsData: List<MyCashOutChannelsData>? =null
)
@Parcelize
@Entity
data class MyCashOutChannelsData(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    var id: Int, // 112
    @SerializedName("beneficiaryName")
    var beneficiaryName: String?, // Evans
    @SerializedName("accountholderphonenumber")
    var accountholderphonenumber: String?, // 2250701686379
    @SerializedName("channelId")
    var channelId: Int?, // 2
    @SerializedName("channelType")
    var channelType: String?, // Telco
    @SerializedName("channelName")
    var channelName: String?, // MTN
    @SerializedName("channeAbbreviation")
    var channeAbbreviation: String?, // mtn
    @SerializedName("channelNumber")
    var channelNumber: String?, // 2250594851580
    @SerializedName("bankName")
    var bankName: String?, // null
    @SerializedName("cardNumber")
    var cardNumber: String?, // null
    @SerializedName("expiryDate")
    var expiryDate: String?, // null
    @SerializedName("cvc")
    var cvc: String?, // null
    @SerializedName("dateCreated")
    var dateCreated: String?, // 2022-05-27T13:22:33.71
    @SerializedName("twofactorenabled")
    var twofactorenabled: Boolean?, // false
    @SerializedName("status")
    var status: Boolean?, // true
    @SerializedName("pin")
    var pin: String? // 4601
) : Parcelable
