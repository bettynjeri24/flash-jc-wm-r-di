package com.ekenya.rnd.tijara.network.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "sacco_details")
data class SaccoDetailEntity(
    val name: String,
    val firstName: String,
    val username: String, //Stima
    @PrimaryKey(autoGenerate = false)
    val orgId: Int,//200
    val isSacco: Boolean, // true
)