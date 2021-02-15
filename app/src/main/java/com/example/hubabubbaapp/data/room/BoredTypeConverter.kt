package com.example.hubabubbaapp.data.room

import androidx.room.TypeConverter
import com.example.hubabubbaapp.model.BoredResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BoredTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun boredResultToString(boredResult: BoredResult): String {
        return gson.toJson(boredResult)
    }

    @TypeConverter
    fun stringToBoredResult(data: String): BoredResult {
        //  I dont know what the listType line of code does , used google for that one and CP it
        val listType = object : TypeToken<BoredResult>() {}.type
        return gson.fromJson(data, listType)
    }

}