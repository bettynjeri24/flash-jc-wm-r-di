package com.ekenya.rnd.tijara.network.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ParkingResponse(
    @SerializedName("data")
    val `data`: ParkingData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class ParkingData(
        @SerializedName("billerCode")
        val billerCode: String, // NRBC_DAILY_PARKING
        @SerializedName("ParkingDurations")
        val parkingDurations: List<ParkingDuration>,
        @SerializedName("ParkingZones")
        val parkingZones: List<ParkingZone>,
        @SerializedName("PaymentTypes")
        val vehiclesTypes: List<VehiclesTypes>
    )
    data class ParkingDuration(
        @SerializedName("Description")
        val description: String, // ONE_MONTH
        @SerializedName("ID")
        val iD: Int // 1
    )
@Entity(tableName = "zone_table")
        data class ParkingZone(
    @PrimaryKey(autoGenerate = true)
            @SerializedName("ID")
            val iD: Int, // 10
            @SerializedName("Name")
            val name: String // CBD
        )

        data class VehiclesTypes(
            @SerializedName("ID")
            val iD: Int, // 10
            @SerializedName("IncomeTypeID")
            val incomeTypeID: Int, // 0
            @SerializedName("Name")
            val name: String, // PRIVATE
            @SerializedName("UnitCost")
            val unitCost: Int // 0
        )