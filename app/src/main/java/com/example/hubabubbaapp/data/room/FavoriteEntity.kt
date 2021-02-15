package com.example.hubabubbaapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hubabubbaapp.model.BoredResult
import com.example.hubabubbaapp.util.Constants.Companion.FAVORITES_TABLE

@Entity(tableName = FAVORITES_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: BoredResult
)