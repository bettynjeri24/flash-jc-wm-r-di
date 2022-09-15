package io.eclectics.cargilldigital.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserChannelData
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserLogoData
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserTransactionData


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
