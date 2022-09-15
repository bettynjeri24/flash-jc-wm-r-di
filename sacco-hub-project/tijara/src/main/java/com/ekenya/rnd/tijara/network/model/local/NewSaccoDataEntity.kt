package com.ekenya.rnd.tijara.network.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "new_sacco_data")
data class NewSaccoDataEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int, // 20
    val isSacco: Boolean, // false
    val name: String, // Muungano
    val website: String? // null
)