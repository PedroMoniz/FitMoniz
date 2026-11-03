package com.moniz.data_db.converters

import androidx.room.TypeConverter

class CommonTypeConverters {

    private val separator = ","

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return list.joinToString(separator = separator)
    }

    @TypeConverter
    fun fromStringToList(data: String): List<String> {
        if (data.isEmpty()) return emptyList()
        return data.split(separator)
    }
}
