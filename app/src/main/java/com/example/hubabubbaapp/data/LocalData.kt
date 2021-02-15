package com.example.hubabubbaapp.data

import com.example.hubabubbaapp.data.room.BoredDao
import com.example.hubabubbaapp.data.room.BoredEntity
import com.example.hubabubbaapp.data.room.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalData @Inject constructor(
    private val boredDao: BoredDao
) {
    fun readDatabase(): Flow<List<BoredEntity>> {
        return boredDao.readActivity()
    }

    fun readFavoriteActivitys(): Flow<List<FavoriteEntity>> {
        return boredDao.readFavoriteActivity()
    }

    suspend fun insertActivity(boredEntity: BoredEntity) {
        boredDao.insertActivity(boredEntity)
    }

    suspend fun insertFavoriteActivity(favoriteEntity: FavoriteEntity) {
        boredDao.insertFavoriteActivity(favoriteEntity)
    }

    suspend fun deleteFavoriteActivity(favoriteEntity: FavoriteEntity) {
        boredDao.deleteFavoriteActivity(favoriteEntity)
    }

    suspend fun deleteAllFavoriteActivitys() {
        boredDao.deleteAllFavoriteActivitys()
    }
}