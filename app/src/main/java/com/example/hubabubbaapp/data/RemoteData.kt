package com.example.hubabubbaapp.data

import com.example.hubabubbaapp.model.BoredResult
import com.example.hubabubbaapp.network.BoredApi
import retrofit2.Response
import javax.inject.Inject

class RemoteData @Inject constructor(
    private val boredApi: BoredApi
) {
    suspend fun getActivity(queries: Map<String, String>): Response<BoredResult> {
        return boredApi.getActivity(queries)
    }
}