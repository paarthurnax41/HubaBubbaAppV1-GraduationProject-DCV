package com.example.hubabubbaapp.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface BoredDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(boredEntity: BoredEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteActivity(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM activities_table ORDER BY id ASC")
    fun readActivity(): Flow<List<BoredEntity>>

    @Query("SELECT * FROM favorites_table ORDER BY id asc")
    fun readFavoriteActivity(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavoriteActivity(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorites_table")
    suspend fun deleteAllFavoriteActivitys()
}