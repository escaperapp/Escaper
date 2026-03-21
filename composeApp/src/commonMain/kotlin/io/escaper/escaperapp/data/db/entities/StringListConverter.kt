package io.escaper.escaperapp.data.db.entities

import androidx.room.TypeConverter

class StringListConverter {

    private val separator = "|"

    @TypeConverter
    fun fromString(value: String): List<String> {
        if (value.isEmpty()) return emptyList()
        return value.split(separator)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        if (list.isEmpty()) return ""
        return list.joinToString(separator)
    }
}