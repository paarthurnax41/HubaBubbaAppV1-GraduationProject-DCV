package com.example.hubabubbaapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [BoredEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BoredTypeConverter::class)
abstract class BoredDatabase : RoomDatabase() {

    abstract fun boredDao(): BoredDao
}