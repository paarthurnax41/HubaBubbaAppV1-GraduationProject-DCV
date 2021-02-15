package com.example.hubabubbaapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.hubabubbaapp.data.Repository
import com.example.hubabubbaapp.data.room.BoredEntity
import com.example.hubabubbaapp.data.room.FavoriteEntity
import com.example.hubabubbaapp.model.BoredResult
import com.example.hubabubbaapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    //                                     ROOM DATABASE
    val readActivities: LiveData<List<BoredEntity>> = repository.local.readDatabase().asLiveData()
    val readFavoriteActivitys: LiveData<List<FavoriteEntity>> =
        repository.local.readFavoriteActivitys().asLiveData()

    private fun insertActivity(boredEntity: BoredEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertActivity(boredEntity)
        }

    fun insertFavoriteActivity(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteActivity(favoriteEntity)
        }


    fun deleteFavoriteActivity(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteActivity(favoriteEntity)
        }


    fun deleteAllFavoriteActivity() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteActivitys()
        }


    //                                      RETRO   FIT
    var boredResponse: MutableLiveData<NetworkResult<BoredResult>> = MutableLiveData()

    fun getActivitys(queries: Map<String, String>) = viewModelScope.launch {
        getActivitysSafeRequest(queries)
    }

    private suspend fun getActivitysSafeRequest(queries: Map<String, String>) {
        if (hasInternetConnection()) {
            val response = repository.remote.getActivity(queries)
            boredResponse.value = handleBoredResponse(response)

            val boredActivity = boredResponse.value!!.data
            if (boredActivity != null) {
                offlineCacheActivity(boredActivity)
            }

        } else {
            boredResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheActivity(boredActivity: BoredResult) {

        val boredEntity = BoredEntity(boredActivity)
        insertActivity(boredEntity)
    }

    private fun handleBoredResponse(response: Response<BoredResult>): NetworkResult<BoredResult>? {

        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.body()!!.activity.isNullOrEmpty() -> {
                return NetworkResult.Error("Activitys not found for specific Filter")
            }
            response.isSuccessful -> {
                val boredResult = response.body()
                return NetworkResult.Success(boredResult!!)
            }
            else -> {
                return NetworkResult.Error(response.errorBody().toString())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager: ConnectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeConnection = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeConnection) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            else -> false
        }
    }
}