package com.example.hubabubbaapp.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hubabubbaapp.data.DataStoreRepository
import com.example.hubabubbaapp.util.Constants
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_MAXPRICE
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_MINPRICE
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_PARTICIPANT
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var type = DEFAULT_TYPE
    private var minprice = DEFAULT_MINPRICE.toString()
    private var maxprice = DEFAULT_MAXPRICE.toString()
    private var participant = DEFAULT_PARTICIPANT.toString()
    val readTypePriceAndParticipant = dataStoreRepository.readTypePriceParticipant

    fun saveTypePriceAndParticipant(
        type: String,
        typeId: Int,
        minPrice: Double,
        minPriceId: Int,
        participant: Int,
        participantId: Int,
        maxPrice: Double,
        maxPriceId: Int
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveTypePriceParticipant(
                type,
                typeId,
                minPrice,
                minPriceId,
                participant,
                participantId,
                maxPrice,
                maxPriceId
            )
        }


    fun applyQueries(): HashMap<String, String> {

        viewModelScope.launch {
            readTypePriceAndParticipant.collect { value ->
                type = value.selectedType
                participant = value.selectedParticipant.toString()
                minprice = value.selectedMinPrice.toString()
                maxprice = value.selectedMaxPrice.toString()
            }
        }

        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_MINPRICE] = minprice
        queries[Constants.QUERY_MAXPRICE] = maxprice
        queries[Constants.QUERY_TYPE] = type
        queries[Constants.QUERY_PARTICIPANT] = participant.toString()
        return queries

    }
}