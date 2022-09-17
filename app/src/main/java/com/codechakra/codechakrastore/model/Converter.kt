package com.codechakra.codechakrastore.model

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class Converter() {

    @TypeConverter
    fun fromListToString(list: ArrayList<String>): String {
        var str = ""
        for (data in list) {
            str += "${data},"
        }
        return str
    }

    @TypeConverter
    fun fromStringToList(str: String): ArrayList<String>{
        val list = str.split(",") as ArrayList<String>
        return list
    }
}