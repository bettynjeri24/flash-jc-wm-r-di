package com.ekenya.rnd.common.data.db

import androidx.room.TypeConverter
import com.ekenya.rnd.common.data.db.entity.CargillUserChannelData
import com.ekenya.rnd.common.data.db.entity.CargillUserLogoData
import com.ekenya.rnd.common.data.db.entity.CargillUserTransactionData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CommonCustomTypeConverter {

    @TypeConverter
    fun fromJsonUserTransactionData(listToString: String): List<CargillUserTransactionData> {
        val listTypeToken = object : TypeToken<List<CargillUserTransactionData>>() {}.type
        return Gson().fromJson(listToString, listTypeToken)
    }

    @TypeConverter
    fun toJsonUserTransactionData(listFromString: List<CargillUserTransactionData>): String {
        return Gson().toJson(listFromString)
    }

    @TypeConverter
    fun fromJsonUserChannelData(listToString: String): List<CargillUserChannelData> {
        val listTypeToken = object : TypeToken<List<CargillUserChannelData>>() {}.type
        return Gson().fromJson(listToString, listTypeToken)
    }

    @TypeConverter
    fun toJsonUserChannelData(listFromString: List<CargillUserChannelData>): String {
        return Gson().toJson(listFromString)
    }
    @TypeConverter
    fun fromJsonUserLogoData(listToString: String): List<CargillUserLogoData> {
        val listTypeToken = object : TypeToken<List<CargillUserLogoData>>() {}.type
        return Gson().fromJson(listToString, listTypeToken)
    }

    @TypeConverter
    fun toJsonUserLogoData(listFromString: List<CargillUserLogoData>): String {
        return Gson().toJson(listFromString)
    }

}
