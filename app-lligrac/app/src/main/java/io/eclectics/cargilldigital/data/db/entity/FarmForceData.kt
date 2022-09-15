package io.eclectics.cargilldigital.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class FarmForceData(
    @PrimaryKey
    @SerializedName("purchaseId")
    var purchaseId: String, // 12345
    @SerializedName("paymentType")
    var paymentType: Int?, // 1
    @SerializedName("amount")
    var amount: String?, // 23000
    @SerializedName("buyerPhonenumber")
    var buyerPhonenumber: String?, // 2250703035850
    @SerializedName("farmerPhonenumber")
    var farmerPhonenumber: String?, // 2250703035850
    @SerializedName("paymentKey")
    var paymentKey: String?, // 2250703035850QERT
    @SerializedName("txnDate")
    var txnDate: String?, // 0001-01-01T00:00:00
    @SerializedName("comments")
    var comments: String? // pay
)