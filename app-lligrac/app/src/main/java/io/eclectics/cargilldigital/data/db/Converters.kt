package io.eclectics.cargilldigital.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import io.eclectics.cargilldigital.data.model.Section
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toUserInfo(userInfo:String):Section{
        val type=object :TypeToken<Section>(){}.type
        return Gson().fromJson(userInfo,type)
    }
    @TypeConverter
    fun toUserInfoJson(userInfo:Section):String{
        val type=object :TypeToken<Section>(){}.type
        return Gson().toJson(userInfo,type)
    }

/*
    @TypeConverter
    fun listToJson(value: List<Section>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Section>::class.java).toList()
*/

}