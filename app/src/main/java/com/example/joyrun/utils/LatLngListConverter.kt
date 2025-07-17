package com.example.joyrun.utils

import androidx.room.TypeConverter
import com.amap.api.maps2d.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LatLngListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromLatLngList(list: List<LatLng>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toLatLngList(data: String?): List<LatLng> {
        if (data.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<LatLng>>() {}.type
        return gson.fromJson(data, listType)
    }
}
