package io.eclectics.cargilldigital.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "buyerpendingpayment")
class BuyerPendingPayment (
    @PrimaryKey(autoGenerate = false)
    @SerializedName("farmerIdx") val  id:Int,
    @SerializedName("buyerphonenumber") val  buyerphonenumber:String,
    @SerializedName("farmerPhonenumber") val  farmerphonenumber:String,
    @SerializedName("firstName") val  firstName:String,
    @SerializedName("lastName") val  lastName:String,
    @SerializedName("fullAmount") val  fullAmount:String,
    @SerializedName("unitprice") val  unitprice:String,
    @SerializedName("amountPaid") val  amountPaid:String,
    @SerializedName("paymenttype") val  paymenttype:String,
    @SerializedName("farmForceRef") val  payementrefcode:String,
    @SerializedName("date") val  date:String,
    @SerializedName("paymentstatus") val  paymentstatus:Boolean,
    @SerializedName("paymentDescription") val  paymentDescription:String
)
object BuyerPending{

  /*  fun pendingList():List<BuyerPendingPayment>{
        var arrayList = ArrayList<BuyerPendingPayment>()
        var p1 = BuyerPendingPayment("Evans Mwangi","45000","20000","Cocoa purchase","XTR454656","1")
        var p2 = BuyerPendingPayment("Morris Murega","80000","30000","Cocoa purchase","XTR454656","1")
        var p3 = BuyerPendingPayment("Janet Imurah","6700","4200","Cocoa purchase","XTR454656","1")
        var p4 = BuyerPendingPayment("Christine Chiromo","45000","20000","Cocoa purchase","XTR454656","1")


        arrayList.add(p1)
        arrayList.add(p2)
        arrayList.add(p3)
        arrayList.add(p4)

        return arrayList

    }*/
}