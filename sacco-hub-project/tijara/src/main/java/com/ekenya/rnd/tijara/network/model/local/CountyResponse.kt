package com.ekenya.rnd.tijara.network.model.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CountyResponse(
    @SerializedName("data")
    val `data`: CountyData,
    @SerializedName("message")
    val message: String, // Success
    @SerializedName("status")
    val status: Int // 1
)
    data class CountyData(
        @SerializedName("billerCode")
        val billerCode: String, // COUNTY_PAYMENT
        @SerializedName("counties")
        val counties: List<County>
    )
        @Entity(tableName = "county_table")
        data class County(
            @PrimaryKey(autoGenerate = true)
            @SerializedName("ID")
            val iD: Int, // 1
            @SerializedName("Name")
            val name: String, // Kitale
            @SerializedName("Url")
            val url: String // http://enzoia.jambopay.co.ke:8080
        ){
            override fun toString(): String {
                return name
            }
        }
