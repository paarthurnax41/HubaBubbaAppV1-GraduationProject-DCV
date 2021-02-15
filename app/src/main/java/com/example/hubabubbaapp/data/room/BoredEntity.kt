package com.example.hubabubbaapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hubabubbaapp.model.BoredResult
import com.example.hubabubbaapp.util.Constants.Companion.ACTIVITY_TABLE

@Entity(tableName = ACTIVITY_TABLE)
class BoredEntity(var boredResult: BoredResult) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}