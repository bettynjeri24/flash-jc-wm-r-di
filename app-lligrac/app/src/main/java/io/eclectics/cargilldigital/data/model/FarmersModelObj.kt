package io.eclectics.cargill.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class FarmersModel(
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val location: String?,
    val farmSize: String?,
    val lastCollectionDate: String?,
    val totalWeightCollected: String?,
    val lastWeightCollected: String?,
    val dateJoined: String?,
    val emailAddress:String?,
    val produceQuality: Int,
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(phoneNumber)
        parcel.writeString(location)
        parcel.writeString(farmSize)
        parcel.writeString(lastCollectionDate)
        parcel.writeString(totalWeightCollected)
        parcel.writeString(lastWeightCollected)
        parcel.writeString(dateJoined)
        parcel.writeInt(produceQuality)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FarmersModel> {
        override fun createFromParcel(parcel: Parcel): FarmersModel {
            return FarmersModel(parcel)
        }

        override fun newArray(size: Int): Array<FarmersModel?> {
            return arrayOfNulls(size)
        }
    }

}
//farmer model2 fro demo

@Entity(tableName = "farmermodelobj")
data class FarmerModelObj(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id: Int,
    @SerializedName("farmerid") val farmerId: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("middleName")val middleName: String?,
    @SerializedName("lastName")val lastName: String?,
    @SerializedName("phoneNumber")val phoneNumber: String?,
    @SerializedName("address")val location: String?,
    @SerializedName("govtIdNumber") val govtIdNumber: String?,
    @SerializedName("emailAddress")val emailAddress: String?,
    @SerializedName("otherPhoneNumber")val otherPhoneNumber: String?,
    @SerializedName("cooperativeid")val cooperativeid: String?,
    @SerializedName("producerCode")val producerCode: String?,
    //@SerializedName("cooperativeId")val cooperativeId: String?,  datecreated gender
    @SerializedName("datecreated") val datecreated: String?,
    @SerializedName("nationalId") val nationalId: String?,
    @SerializedName("certificationnumber")val certificationnumber: String?,
    @SerializedName("village")val village: String?,
    @SerializedName("sectionid")val sectionid: String?,
    @SerializedName("active")val active: Boolean?
)


data class FarmerTransaction(
    @SerializedName("id") val id:Int,
    @SerializedName("walletid") val  walletid:String,
    @SerializedName("transactionCode") val  transactionId:String,
    @SerializedName("transactionType") val  transactionType:Int,
    @SerializedName("debitAccount") val  debitAccount:String,
    @SerializedName("creditAccount") val  creditAccount:String,
    @SerializedName("reasons") val  reasons:String?,
    @SerializedName("datecreated") val  datetime:String,
    @SerializedName("transactionChannel") val  transactionChannel:String,
    @SerializedName("recipientPhoneNumber") val  phonenumber: String,
    @SerializedName("sendorPhoneNumber") val  sendorPhoneNumber:String,
    @SerializedName("userid") val  userid:String?,
    @SerializedName("amount") val  amount:String?,
    @SerializedName("transactionDirection") val  transactionDirection: String,
    @SerializedName("status") var status: String,
    @SerializedName("walletTransactionType") var walletTransactionType:String,
    @SerializedName("sendorName") var sendorName:String,
    @SerializedName("recipientName") var recipientName:String,
    @SerializedName("channelRefNo") val channelRefNo:String?
)
data class RecentTransaction(
    @SerializedName("id") val  id:Int,
    @SerializedName("recipientwalletid") val  recipientwalletid:String,
    @SerializedName("datecreated") val  datecreated:String,
    @SerializedName("walletTransactionType") val  walletTransactionType:Int,
    @SerializedName("sendorPhoneNumber") val  sendorPhoneNumber:String,
    @SerializedName("recipientPhoneNumber") val  recipientPhoneNumber:String,
    @SerializedName("status") val  status: Boolean,
    @SerializedName("transactiontype") val  transactiontype:Int?,
    @SerializedName("reasons") val  reasons:String?,
    @SerializedName("amount") var amount: String,
    @SerializedName("sendorName") var sendorName: String,
    @SerializedName("recipientName") val recipientName:String?
)
